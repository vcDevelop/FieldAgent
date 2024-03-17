package com.example.fieldagent

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ClientAdapter(
    private val clients: List<String>,
    private val onDetailsButtonClickListener: (String) -> Unit,
    private val onDeleteButtonClickListener: (String) -> Unit,
    private val onButtonClickListener: (String) -> Unit,
    private val onItemClickListener: (String) -> Unit
) : RecyclerView.Adapter<ClientAdapter.ClientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ClientViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        val clientId = clients[position]
        holder.bind(clientId)
    }

    override fun getItemCount(): Int {
        return clients.size
    }

    inner class ClientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val clientNameTextView: TextView = itemView.findViewById(R.id.cust_name)
        private val clientInstitution: TextView = itemView.findViewById(R.id.institute_name)
        private val clientContact_no: TextView =
            itemView.findViewById(R.id.Contact_person_Number)
        private val clientIdTextView: TextView = itemView.findViewById(R.id.cust_name)
        private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        private lateinit var auth: FirebaseAuth

        fun bind(clientId: String) {
            clientIdTextView.text = clientId
            auth = FirebaseAuth.getInstance()
            val userId = auth.currentUser?.uid
            // Get client details from Firestore using client ID
            val clientRef = firestore.collection("users").document(userId.toString())
                .collection("clients").document(clientId)
            clientRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val clientName = document.getString("cust_name")
                        val institutename = document.getString("institute_name")
                        val contact = document.getString("contact_person_number")
                        clientContact_no.text = contact
                        clientNameTextView.text = "Customer Name : " + clientName
                        clientInstitution.text = institutename
                    } else {
                        clientNameTextView.text = "Client Name Not Found"
                    }
                }
                .addOnFailureListener { exception ->
                    clientNameTextView.text = "Error: ${exception.message}"
                }

            // Set click listener for the item
            itemView.setOnClickListener { onItemClickListener(clientId) }

            val button = itemView.findViewById<Button>(R.id.Paymentdash)
            val button1 = itemView.findViewById<Button>(R.id.ReimburseDetails)
            val button2 = itemView.findViewById<ImageButton>(R.id.Delete)

            button.setOnClickListener { onButtonClickListener(clientId) }
            button1.setOnClickListener { onDetailsButtonClickListener(clientId) }
            button2.setOnClickListener { onDeleteButtonClickListener(clientId) }

            // Check if the client has any reimbursement details
            val reimbursementRef =
                firestore.collection("users").document(userId.toString())
                    .collection("clients").document(clientId).collection("reimburse")
            reimbursementRef.get()
                .addOnSuccessListener { documents ->
                    val hasReimbursements = !documents.isEmpty()
                    button1.isEnabled = hasReimbursements
                }
                .addOnFailureListener { exception ->
                    Log.e("ClientViewHolder", "Error getting reimbursement details", exception)
                }
        }
    }
}
