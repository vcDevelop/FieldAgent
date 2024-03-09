package com.example.fieldagent
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar
import java.util.Locale

class ReimbursementScreen : AppCompatActivity() {
    private lateinit var fStore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private lateinit var clientId: String
    private var siteVisitDateEditText = "Date"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reimbursement_screen)
        fStore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser?.uid ?: ""
        clientId = intent.getStringExtra("clientId") ?: ""
    }

    fun openDatePicker(view: View) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, dayOfMonth)
                val currentDate = Calendar.getInstance()

                if (selectedCalendar <= currentDate) {
                    val selectedDate = String.format(Locale.getDefault(), "%02d %s %d", dayOfMonth, getMonthName(month), year)
                    (view as Button).text = selectedDate
                    Toast.makeText(this, selectedDate, Toast.LENGTH_SHORT).show()
                    siteVisitDateEditText=selectedDate
                }
                else {
                    Toast.makeText(this, "Please select a date before or equal to today", Toast.LENGTH_SHORT).show()
                }
            },
            currentYear, currentMonth, currentDay
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun getMonthName(month: Int): String {
        val monthNames = arrayOf(
            "JAN", "FEB", "MAR", "APR",
            "MAY", "JUN", "JUL", "AUG",
            "SEP", "OCT", "NOV", "DEC"
        )
        return monthNames[month]
    }

    // Function to handle "Request" button click
    fun onRequestButtonClick(view: View) {
        val siteVisitDateEditText = "Date"
        val expenseNameEditText = findViewById<EditText>(R.id.expenseNameEditText)
        val numberOfDaysEditText = findViewById<EditText>(R.id.numberOfDaysEditText)
        val receivingBillEditText = findViewById<EditText>(R.id.RecevingBill)
        val amountEditText = findViewById<EditText>(R.id.Amount)

        val siteVisitDate = siteVisitDateEditText.toString()
        val expenseName = expenseNameEditText.text.toString()
        val numberOfDays = numberOfDaysEditText.text.toString()
        val receivingBill = receivingBillEditText.text.toString()
        val amount = amountEditText.text.toString()
        val status = ""


        // Add data to Firestore
        if( findViewById<EditText>(R.id.expenseNameEditText).text.toString().isEmpty() ||
            findViewById<EditText>(R.id.numberOfDaysEditText).text.toString().isEmpty() ||
            findViewById<EditText>(R.id.RecevingBill).text.toString().isEmpty() ||
            findViewById<EditText>(R.id.Amount).text.toString().isEmpty()
        ){

            Toast.makeText(this, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()

        }
        else{
            addReimbursementDataToFirestore(siteVisitDate, expenseName, numberOfDays, receivingBill, amount,status)
            expenseNameEditText.text.clear()
            numberOfDaysEditText.text.clear()
            receivingBillEditText.text.clear()
            amountEditText.text.clear()
        }


    }

    // Function to add data to Firestore
    private fun addReimbursementDataToFirestore(siteVisitDate: String, expenseName: String, numberOfDays: String, receivingBill: String, amount: String,status: String) {
        // Create a map to represent the data
        val reimbursementData = hashMapOf(
            "siteVisitDate" to siteVisitDate,
            "expenseName" to expenseName,
            "numberOfDays" to numberOfDays,
            "receivingBill" to receivingBill,
            "amount" to amount,
            "status" to status
        )

        fStore.collection("users").document(userId)
            .collection("clients").document(clientId)
            .collection("reimburse").add(reimbursementData)
            .addOnSuccessListener {
                Toast.makeText(this, "Request added successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error adding data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}