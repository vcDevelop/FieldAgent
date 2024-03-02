package com.example.fieldagent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fieldagent.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject


class Dashboard : AppCompatActivity() {
    private lateinit var clientRecyclerView: RecyclerView
    private lateinit var userList:ArrayList<User>
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var showName:TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
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
            // Document does not exist
            showName.text = "User not found"
        }



        binding.Paymentdash.setOnClickListener{
            val intent = Intent(this,ReimbursementScreen::class.java)
            startActivity(intent)
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
                    val clientIds = result.documents.map { it.id }
                    setupRecyclerView(clientIds)
                }
                .addOnFailureListener { exception ->
                    Log.e("LoadClients", "Error loading clients", exception)
                    // Handle errors
                }
        }
    }

//    private fun setupRecyclerView(clients: List<Clients>) {
//        val adapter = ClientAdapter(clients) { clientId ->
//            // Handle item click, open a new activity with the selected clientId
//            val intent = Intent(this, CustDetail::class.java)
//            intent.putExtra("clientId", clientId)
//            startActivity(intent)
//        }
//
//
//    }
private fun setupRecyclerView(clientIds: List<String>) {
    val adapter = ClientAdapter(clientIds) { clientId ->
        // Handle item click, open a new activity with the selected clientId
        val intent = Intent(this, CustDetail::class.java)
        intent.putExtra("clientId", clientId)
        startActivity(intent)
    }

    clientRecyclerView.layoutManager = LinearLayoutManager(this)
    clientRecyclerView.adapter = adapter
}
    }

    // Handle item click, open a new activity with the selected clientId





