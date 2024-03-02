package com.example.fieldagent

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar
import java.util.Locale
import java.util.regex.Pattern

class Add_Client : AppCompatActivity() {
    val pattern = Pattern.compile("^[a-zA-Z ]+$").toRegex()
    private lateinit var group1CardView: CardView
    private lateinit var group2CardView: CardView
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var cardViews: MutableList<CardView>
    private lateinit var submit: Button
    private var currentGroupIndex: Int = 0
    private var GroupCount:Int=0
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var userId: String? = null
    private lateinit var site_in_date:DatePicker
    val data = HashMap<String, Any>()

    fun openDatePicker(view: View) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                // Handle the selected date
                val selectedDate = String.format(Locale.getDefault(), "%02d %s %d", dayOfMonth, getMonthName(month), year)
                (view as Button).text = selectedDate
                Toast.makeText(this, selectedDate, Toast.LENGTH_SHORT).show()
                data["site_inspection_date"] = selectedDate
            },
            currentYear, currentMonth, currentDay
        )
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
    private fun saveData(){

        if (userId != null) {
            val subcollectionPath = "clients"
            // Example: Get data from EditText in group1CardView
            data["cust_name"] = findViewById<EditText>(R.id.cust_name).text.toString()
            data["institute_name"] = findViewById<EditText>(R.id.institute_name).text.toString()
            data["cust_loantype"] = findViewById<EditText>(R.id.cust_loantype).text.toString()
            data["contact_person_name"] = findViewById<EditText>(R.id.Contact_person_name).text.toString()
            data["contact_person_number"] = findViewById<EditText>(R.id.Contact_person_Number).text.toString()
//            data["site_inspection_date"] = findViewById<EditText>(R.id.Site_visit_Date).text.toString()
            val button = findViewById<Button>(R.id.button)
            button.setOnClickListener {
                openDatePicker(button)
            }
            data["address"] = findViewById<EditText>(R.id.Address).text.toString()

            // Example: Get data from EditText in group2CardView
            val citySpinner = findViewById<Spinner>(R.id.city_spinner)
            data["selected_city"] = citySpinner.selectedItem.toString()
            data["colony"] = findViewById<EditText>(R.id.Colony).text.toString()
            data["property_address_as_per_site"] = findViewById<EditText>(R.id.property_address_as_per_site).text.toString()
            val yesNoSpinner = findViewById<Spinner>(R.id.yes_no_spinner)
            val jurisdiction = findViewById<Spinner>(R.id.Jurisdiction)
            data["jurisdiction"] = jurisdiction.selectedItem.toString()

            // Example: Get data from EditText in group3CardView
            data["landmark"] = findViewById<EditText>(R.id.landmark).text.toString()
            data["locality"] = findViewById<EditText>(R.id.locality).text.toString()
            data["nearest_railway_station"] = findViewById<EditText>(R.id.NearestRailwayStation).text.toString()
            data["nearest_metro_station"] = findViewById<EditText>(R.id.NearestMetroStation).text.toString()
            data["nearest_bus_stop"] = findViewById<EditText>(R.id.NearestBusStop).text.toString()
            data["width_of_road"] = findViewById<EditText>(R.id.widthofroad).text.toString()
            val siteAccessSpinner = findViewById<Spinner>(R.id.site_access)
            data["site_access"] = siteAccessSpinner.selectedItem.toString()
            data["neighborhood_type"] = findViewById<EditText>(R.id.NeighborhoodType).text.toString()
            data["nearest_hospital"] = findViewById<EditText>(R.id.NearestHospital).text.toString()
            data["any_negative_to_locality"] = findViewById<EditText>(R.id.Anynegativetothelocality).text.toString()

            // Example: Get data from EditText in group4CardView
            val statusOfOccupancySpinner = findViewById<Spinner>(R.id.Statusofoccupancy)
            data["status_of_occupancy"] = statusOfOccupancySpinner.selectedItem.toString()
            data["occupied_by"] = findViewById<EditText>(R.id.Occupied_by).text.toString()
            data["relationship"] = findViewById<EditText>(R.id.Relationship).text.toString()
            data["occupied_since"] = findViewById<EditText>(R.id.OccupiedSince).text.toString()

            // Example: Get data from EditText in group5CardView
            data["east"] = findViewById<EditText>(R.id.East).text.toString()
            data["west"] = findViewById<EditText>(R.id.West).text.toString()
            data["north"] = findViewById<EditText>(R.id.North).text.toString()
            data["south"] = findViewById<EditText>(R.id.South).text.toString()

            // Example: Get data from EditText in group6CardView
            data["amenities"] = findViewById<EditText>(R.id.Amenities).text.toString()
            val levelOfMaintainSpinner = findViewById<Spinner>(R.id.levelofmaintain)
            data["level_of_maintain"] = levelOfMaintainSpinner.selectedItem.toString()
            data["age_of_property"] = findViewById<EditText>(R.id.Age_of_Property).text.toString()


            // Repeat this process for other UI components and CardViews

            data["timestamp"] = FieldValue.serverTimestamp()
            data["address_matching"] = yesNoSpinner.selectedItem.toString()


            firestore.collection("users").document(userId!!)
                .collection(subcollectionPath)
                .add(data)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data added to Firestore successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error adding data to Firestore: $e", Toast.LENGTH_SHORT).show()
                }
        }
    }

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_client)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser?.uid

        cardViews = mutableListOf(
            findViewById(R.id.group1CardView),
            findViewById(R.id.group2CardView),
            findViewById(R.id.group3CardView),
            findViewById(R.id.group4CardView),
            findViewById(R.id.group5CardView),
            findViewById(R.id.group6CardView),
        )

        // Initialize CardViews and button
        group1CardView = findViewById(R.id.group1CardView)
        group2CardView = findViewById(R.id.group2CardView)
        nextButton = findViewById(R.id.nextButton)
        prevButton=findViewById(R.id.prevButton)
        submit=findViewById(R.id.submit_button)
        prevButton.isEnabled=false
        cardViews.forEachIndexed { index, cardView ->
            cardView.visibility = if (index == 0) View.VISIBLE else View.GONE
        }
        submit.setOnClickListener{
            saveData()

        }
        nextButton.setOnClickListener {
            if (!validateGroup(GroupCount)) {

            } else {
                GroupCount+=1;
                prevButton.isEnabled = true
                toggleCardVisibility(cardViews[currentGroupIndex], false)
                currentGroupIndex++
                if (currentGroupIndex < cardViews.size) {
                    toggleCardVisibility(cardViews[currentGroupIndex], true)
                } else {
                    currentGroupIndex = cardViews.size - 1
                }
                if (currentGroupIndex == cardViews.size - 1) {
                    nextButton.isEnabled = false
                    submit.isEnabled = true
                }
            }
        }

        // Set click listener for the prev button
        prevButton.setOnClickListener {
            GroupCount-=1;
            nextButton.isEnabled=true
            submit.isEnabled=false
            toggleCardVisibility(cardViews[currentGroupIndex], false)
            currentGroupIndex--
            if (currentGroupIndex >= 0) {
                toggleCardVisibility(cardViews[currentGroupIndex], true)
            } else {
                currentGroupIndex = 0
            }
            if (currentGroupIndex==0){
                prevButton.isEnabled=false
            }
        }
    }
    private fun validateGroup(GroupC:Int):Boolean{
        when(GroupC){
            0 ->{
                if (findViewById<EditText>(R.id.cust_name).text.toString().isEmpty() ||
                    findViewById<EditText>(R.id.institute_name).text.toString().isEmpty() ||
                    findViewById<EditText>(R.id.cust_loantype).text.toString().isEmpty() ||
                    findViewById<EditText>(R.id.Contact_person_name).text.toString().isEmpty() ||
                    findViewById<EditText>(R.id.Contact_person_Number).text.toString().isEmpty()||
//                    findViewById<EditText>(R.id.Site_visit_Date).text.toString().isEmpty() ||
                    findViewById<EditText>(R.id.Address).text.toString().isEmpty() ||
                    !findViewById<EditText>(R.id.cust_name).text.toString().matches(pattern) ||
                    !findViewById<EditText>(R.id.Contact_person_name).text.toString().matches(pattern)
                ) {
                    Toast.makeText(this, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
                    return false
                }
            }
            1 ->{
                if (findViewById<EditText>(R.id.Colony).text.toString().isEmpty() ||
                    findViewById<EditText>(R.id.property_address_as_per_site).text.toString().isEmpty()
                ){
                    Toast.makeText(this, "Please fill in all fields in Property Location Details", Toast.LENGTH_SHORT).show()
                    return false
                }
            }
            2 ->{
                if(findViewById<EditText>(R.id.landmark).text.toString().isEmpty() ||
                    findViewById<EditText>(R.id.locality).text.toString().isEmpty() ||
                    findViewById<EditText>(R.id.NearestRailwayStation).text.toString().isEmpty() ||
                    findViewById<EditText>(R.id.NearestMetroStation).text.toString().isEmpty() ||
                    findViewById<EditText>(R.id.NearestBusStop).text.toString().isEmpty() ||
                    findViewById<EditText>(R.id.widthofroad).text.toString().isEmpty() ||
                    findViewById<EditText>(R.id.NeighborhoodType).text.toString().isEmpty() ||
                    findViewById<EditText>(R.id.NearestHospital).text.toString().isEmpty() ||
                    findViewById<EditText>(R.id.Anynegativetothelocality).text.toString().isEmpty()
                ){
                    Toast.makeText(this, "Please fill in all fields in Location Details", Toast.LENGTH_SHORT).show()
                    return false
                }
            }
            3 ->{
                if(findViewById<EditText>(R.id.Occupied_by).text.toString().isEmpty() ||
                    findViewById<EditText>(R.id.Relationship).text.toString().isEmpty() ||
                    findViewById<EditText>(R.id.OccupiedSince).text.toString().isEmpty())
                {
                    Toast.makeText(this, "Please fill in all fields in Occupancy Details", Toast.LENGTH_SHORT).show()
                    return false
                }
            }
            4 ->{
                if(findViewById<EditText>(R.id.East).text.toString().isEmpty() ||
                    findViewById<EditText>(R.id.West).text.toString().isEmpty() ||
                    findViewById<EditText>(R.id.North).text.toString().isEmpty() ||
                    findViewById<EditText>(R.id.South).text.toString().isEmpty() )
                {
                    Toast.makeText(this, "Please fill in all fields in Boundari Details", Toast.LENGTH_SHORT).show()
                    return false
                }
            }
            5 ->{
                if (findViewById<EditText>(R.id.Amenities).text.toString().isEmpty() ||
                    findViewById<EditText>(R.id.Age_of_Property).text.toString().isEmpty())
                {
                    Toast.makeText(this, "Please fill in all fields in Feedback", Toast.LENGTH_SHORT).show()
                    return false
                }
            }
        }
        return true
    }
    private fun check_Text(edit :EditText): Boolean {
        if(findViewById<EditText>(R.id.cust_name).text.toString().matches(pattern) ) {
            findViewById<EditText>(R.id.cust_name).setError("Only Text is Allowed")
            Toast.makeText(this, "Only Text is Allowed", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    // Method to toggle visibility of a CardView
    private fun toggleCardVisibility(cardView: CardView, isVisible: Boolean) {
        cardView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

}