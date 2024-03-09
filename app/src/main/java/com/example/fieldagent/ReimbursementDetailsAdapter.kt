package com.example.fieldagent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ReimbursementDetailsAdapter(private val clientId: String, private val reimburseIds: List<String>,private val onDeleteButtonClickListener: (String) -> Unit) :
    RecyclerView.Adapter<ReimbursementDetailsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.details_reimburse, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reimburseId = reimburseIds[position]
        holder.bind(reimburseId)
    }

    override fun getItemCount(): Int = reimburseIds.size
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val expenseNameTextView: TextView = itemView.findViewById(R.id.expenseNameTextView)
        val numberOfDaysTextView: TextView = itemView.findViewById(R.id.numberOfDaysTextView)
        val receivingBillTextView: TextView = itemView.findViewById(R.id.receivingBillTextView)
        val amountTextView: TextView = itemView.findViewById(R.id.amountTextView)
        private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        private lateinit var auth: FirebaseAuth

        fun bind(reimburseId: String) {
//           clientIdTextView.text = clientIdA
            auth = FirebaseAuth.getInstance()
            val userId = auth.currentUser?.uid
            // Get client details from Firestore using client ID
            val clientRef = firestore.collection("users").document(userId.toString()).collection("clients").document(clientId).collection("reimburse").document(reimburseId)
            clientRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val Expensename = document.getString("expenseName")
                        val numberOfDays = document.getString("numberOfDays")
                        val receivingBill = document.getString("receivingBill")
                        val amount = document.getString("amount")

                        expenseNameTextView.text = "Expense name : "+Expensename
                        numberOfDaysTextView.text = "Number Of Days : "+numberOfDays
                        receivingBillTextView.text = "Receiving Bill : "+receivingBill
                        amountTextView.text = "Amount : "+amount

                    } else {
                        expenseNameTextView.text = "Client Name Not Found"
                    }
                }
                .addOnFailureListener { exception ->
                    expenseNameTextView.text = "Error: ${exception.message}"
                }

            // Set click listener for the item
//           itemView.setOnClickListener { onItemClickListener(clientId) }
//
//           val button1 = itemView.findViewById<Button>(R.id.ReimburseDetails)

           val button1 = itemView.findViewById<ImageButton>(R.id.Reject)

           button1.setOnClickListener { onDeleteButtonClickListener(reimburseId) }
        }


    }

}
