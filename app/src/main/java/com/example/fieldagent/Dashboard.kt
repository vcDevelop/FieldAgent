package com.example.fieldagent

import android.content.Intent
import android.graphics.Typeface
import android.health.connect.datatypes.units.Length
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fieldagent.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.TimerTask

class Dashboard : AppCompatActivity() {
    private lateinit var clientRecyclerView: RecyclerView
    private lateinit var userList:ArrayList<User>
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var showName:TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private lateinit var Numberofclients: TextView
    private lateinit var NumberofApprove: TextView
    private lateinit var NumberofReject: TextView
    private lateinit var logout_button: ImageView
    private var isBackPressedOnce = false


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


        userList = arrayListOf()
        auth = FirebaseAuth.getInstance()
        fStore=FirebaseFirestore.getInstance()
        clientRecyclerView = findViewById(R.id.clientRecyclerView)
        loadClients()
        calculateViewedClients()
        getClientApprovalCount()
        getClientRejectCount()
        val userId = auth.currentUser?.uid
        val logoutIcon: ImageView = findViewById(R.id.logout_icon)
        logoutIcon.setOnClickListener {
            logoutUser()

        }

        showName = findViewById(R.id.AgentName)
        if(userId != null) {
            val userRef = fStore.collection("users").document(userId)

            userRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {

                        val name = documentSnapshot.getString("name")
                        showName.text = name
                    }

                }
        }else {
            showName.text = "User not found"
        }

        binding.AddClient.setOnClickListener{
            val intent = Intent(this,Add_Client::class.java)
            startActivity(intent)
        }

        onBackPressedDispatcher.addCallback(this) {
            if (isBackPressedOnce) {
                // Exit the app on second back press
                finishAffinity() // This will close all activities and exit the app
            } else {
                isBackPressedOnce = true
                loadClients()
                calculateViewedClients()
                // Show a toast message or other visual cue (optional)
                Toast.makeText(this@Dashboard, "Press back again to exit", Toast.LENGTH_SHORT).show()

                // Reset the flag after a delay (optional for double back press)
                val exitTimer = object : TimerTask() {
                    override fun run() {
                        isBackPressedOnce = false
                    }
                }
                val handler = Handler()
                handler.postDelayed(exitTimer, 2000) // Delay in milliseconds
            }
        }

    }


    private fun loadClients() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            fStore.collection("users").document(userId)
                .collection("clients")
                .get()
                .addOnSuccessListener { result ->
                    val clientCount = result.size()
                    Numberofclients=findViewById(R.id.TotalClients)
                    Numberofclients.text=clientCount.toString()
                    val clientIds = result.documents.map { it.id }
                    setupRecyclerView(clientIds)
                    calculateViewedClients()
                    getClientApprovalCount()
                    getClientRejectCount()
                }
                .addOnFailureListener { exception ->
                    Log.e("LoadClients", "Error loading clients", exception)
                }
        }
    }

    private fun setupRecyclerView(clientIds: List<String>) {
        val adapter = ClientAdapter(clientIds, onItemClickListener =  { clientId ->
            val intent = Intent(this, CustDetail::class.java)
            intent.putExtra("clientId", clientId)
            startActivity(intent)
        },onButtonClickListener = { clientId ->
            val intent = Intent(this,ReimbursementScreen::class.java)
            intent.putExtra("clientId", clientId)
            startActivity(intent)

        },onDetailsButtonClickListener  = { clientId ->
            val intent = Intent(this, ReimbursementDetails::class.java)
            intent.putExtra("clientId", clientId)
            updateClientStatusAndViewCount(clientId)
            startActivity(intent)

        },onDeleteButtonClickListener  = { clientId ->
            deleteReimbursementData(clientId)
            getClientApprovalCount()
            getClientRejectCount()
            loadClients()

        }
        )

        clientRecyclerView.layoutManager = LinearLayoutManager(this)
        clientRecyclerView.adapter = adapter
    }

    private fun updateClientStatusAndViewCount(clientId: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val clientRef = fStore.collection("users").document(userId)
                .collection("clients").document(clientId)
            clientRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    val status = documentSnapshot.getString("status")
                    if (status == "viewed") {
                        clientRef.update("status", "viewed")
                            .addOnSuccessListener {
                                Log.d("UpdateStatusAndView", "Client status updated to 'viewed' successfully")
                            }
                            .addOnFailureListener { e ->
                                Log.e("UpdateStatusAndView", "Error updating client status", e)
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("UpdateStatusAndView", "Error getting client status", e)
                }
        }
    }


    private fun calculateViewedClients() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            var approvedCount = 0
            var rejectedCount = 0
            var viewedCount = 0
            var completedQueries = 0 // Counter to track completed queries

            val approvedQuery = fStore.collection("users").document(userId)
                .collection("clients")
                .whereEqualTo("status", "approved")

            val rejectedQuery = fStore.collection("users").document(userId)
                .collection("clients")
                .whereEqualTo("status", "rejected")

            val viewedQuery = fStore.collection("users").document(userId)
                .collection("clients")
                .whereEqualTo("status", "viewed")

            // Execute all queries concurrently
            approvedQuery.get().addOnSuccessListener { querySnapshot ->
                approvedCount = querySnapshot.size()
                completedQueries++
                updateUIIfAllQueriesCompleted(approvedCount, rejectedCount, viewedCount, completedQueries)
            }.addOnFailureListener { exception ->
                Log.e("ApprovedClientsCount", "Error getting approved clients", exception)
            }

            rejectedQuery.get().addOnSuccessListener { querySnapshot ->
                rejectedCount = querySnapshot.size()
                completedQueries++
                updateUIIfAllQueriesCompleted(approvedCount, rejectedCount, viewedCount, completedQueries)
            }.addOnFailureListener { exception ->
                Log.e("RejectedClientsCount", "Error getting rejected clients", exception)
            }

            viewedQuery.get().addOnSuccessListener { querySnapshot ->
                viewedCount = querySnapshot.size()
                completedQueries++
                updateUIIfAllQueriesCompleted(approvedCount, rejectedCount, viewedCount, completedQueries)
            }.addOnFailureListener { exception ->
                Log.e("ViewedClientsCount", "Error getting viewed clients", exception)
            }
        }
    }

    private fun updateUIIfAllQueriesCompleted(
        approvedCount: Int,
        rejectedCount: Int,
        viewedCount: Int,
        completedQueries: Int
    ) {
        // Check if all queries have completed
        if (completedQueries == 3) {
            // Update the UI with the counts
            setTotalOpenClients(approvedCount, rejectedCount, viewedCount)
        }
    }


    private fun setTotalOpenClients(
        approvedCount: Int,
        rejectedCount: Int,
        viewedCount: Int
    ) {
        val totalOpenClientsTextView = findViewById<TextView>(R.id.TotalOpenClients)

        // Calculate the total count of open clients
        val totalOpenCount =viewedCount - (approvedCount + rejectedCount)


        // Update the TextView with the total count of open clients
        totalOpenClientsTextView.text = viewedCount.toString()
    }




    private fun deleteReimbursementData(clientId: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            fStore.collection("users").document(userId)
                .collection("clients").document(clientId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "client data deleted successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Log.e("DeleteReimbursement", "Error deleting client data", exception)
                }
        }
    }
    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this,LoginScreen::class.java)
        startActivity(intent)
        finish()
    }
    private fun getClientApprovalCount() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            fStore.collection("users").document(userId)
                .collection("clients")
                .whereEqualTo("status", "approved") // Assuming "status" is the field containing the approval status
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val approvedCount = querySnapshot.size()
                    NumberofApprove=findViewById(R.id.TotalApprove)
                    NumberofApprove.text=approvedCount.toString()
                    Log.d("ClientApprovalCount", "Number of approved clients: $approvedCount")
                }
                .addOnFailureListener { exception ->
                    Log.e("ClientApprovalCount", "Error getting approved clients", exception)
                }
        }
    }
    private fun getClientRejectCount() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            fStore.collection("users").document(userId)
                .collection("clients")
                .whereEqualTo("status", "rejected") // Assuming "status" is the field containing the rejection status
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val rejectedCount = querySnapshot.size()
                    NumberofReject=findViewById(R.id.TotalReject)
                    NumberofReject.text=rejectedCount.toString()
                    Log.d("ClientRejectionCount", "Number of rejected clients: $rejectedCount")
                }
                .addOnFailureListener { exception ->
                    Log.e("ClientRejectionCount", "Error getting rejected clients", exception)
                }
        }
    }




}