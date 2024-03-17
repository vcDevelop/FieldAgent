package com.example.fieldagent

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class ReimbursementDetails : AppCompatActivity() {
    private lateinit var fStore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private lateinit var clientId: String
    private lateinit var reimburseCollectionRef: CollectionReference
    private lateinit var reimbursementDetailsRecyclerView: RecyclerView
     var backPressedCount=0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reimbursement_details)

        fStore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser?.uid ?: ""
        clientId = intent.getStringExtra("clientId") ?: ""

        reimburseCollectionRef = fStore.collection("users").document(userId)
            .collection("clients").document(clientId)
            .collection("reimburse")

        reimbursementDetailsRecyclerView = findViewById(R.id.reimbursementDetailsRecyclerView)
        retrieveReimbursementDetails()
        val approve: Button = findViewById(R.id.Approve)
        approve.setOnClickListener {
            addApprove()
        }
        val reject: Button = findViewById(R.id.Reject)
        reject.setOnClickListener {
            addReject()
        }
        onBackPressedDispatcher.addCallback(this@ReimbursementDetails) {
            val intent = Intent(this@ReimbursementDetails, Dashboard::class.java)
            startActivity(intent)
        }
    }

    private fun retrieveReimbursementDetails() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            fStore.collection("users").document(userId)
                .collection("clients").document(clientId)
                .collection("reimburse")
                .get()
                .addOnSuccessListener { result ->
                    val reimburseIds = result.documents.map { it.id }
                    setupRecyclerView(reimburseIds)
                }
                .addOnFailureListener { exception ->
                    Log.e("LoadClients", "Error loading clients", exception)
                }
        }
    }

    private fun setupRecyclerView(reimburseIds: List<String>){
        val adapter =  ReimbursementDetailsAdapter(clientId, reimburseIds,
            onDeleteButtonClickListener= { reimburseId ->
                deleteReimbursementData(reimburseId)
            }
        )
        reimbursementDetailsRecyclerView.layoutManager = LinearLayoutManager(this)
        reimbursementDetailsRecyclerView.adapter = adapter
    }

    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginScreen::class.java)
        startActivity(intent)
        finish()
    }
    private fun deleteReimbursementData(reimburseId: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            fStore.collection("users").document(userId)
                .collection("clients").document(clientId)
                .collection("reimburse").document(reimburseId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Reimbursement data deleted successfully", Toast.LENGTH_SHORT).show()
                    retrieveReimbursementDetails()
                }
                .addOnFailureListener { exception ->
                    Log.e("DeleteReimbursement", "Error deleting reimbursement data", exception)
                }
        }
    }
    private fun addApprove() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val statusData = hashMapOf(
                "status" to "approved"
            )
            fStore.collection("users").document(userId)
                .collection("clients").document(clientId)
                .update(statusData as Map<String, Any>)
                .addOnSuccessListener {
                    Toast.makeText(this, "Client updated to approved", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                    // Optionally, you can also update the UI or perform any other actions
                }
                .addOnFailureListener { exception ->
                    Log.e("AddApprove", "Error updating status", exception)
                    // Handle failure
                }
        }
    }

    private fun addReject() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val statusData = hashMapOf(
                "status" to "rejected"
            )
            fStore.collection("users").document(userId)
                .collection("clients").document(clientId)
                .update(statusData as Map<String, Any>)
                .addOnSuccessListener {
                    Toast.makeText(this, "Client updated to rejected", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                    // Optionally, you can also update the UI or perform any other actions
                }
                .addOnFailureListener { exception ->
                    Log.e("AddReject", "Error updating status", exception)
                    // Handle failure
                }
        }
    }


    }


