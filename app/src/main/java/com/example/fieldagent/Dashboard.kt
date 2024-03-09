package com.example.fieldagent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fieldagent.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Dashboard : AppCompatActivity() {
    private lateinit var clientRecyclerView: RecyclerView
    private lateinit var userList:ArrayList<User>
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var showName:TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private lateinit var Numberofclients: TextView
    private lateinit var logout_button: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userList = arrayListOf()
        auth = FirebaseAuth.getInstance()
        fStore=FirebaseFirestore.getInstance()
        clientRecyclerView = findViewById(R.id.clientRecyclerView)
        loadClients()

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
                    Toast.makeText(this, "Total Clients: $clientCount", Toast.LENGTH_SHORT).show()
                    val clientIds = result.documents.map { it.id }
                    setupRecyclerView(clientIds)
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
            startActivity(intent)
        }
        )

        clientRecyclerView.layoutManager = LinearLayoutManager(this)
        clientRecyclerView.adapter = adapter
    }
    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this,LoginScreen::class.java)
        startActivity(intent)
        finish()
    }

}