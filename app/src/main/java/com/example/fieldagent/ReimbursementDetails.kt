package com.example.fieldagent

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
    private lateinit var reimbursementDetailsList: List<ReimbursementDetailItem>

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


    }


