package com.example.fieldagent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

import com.example.fieldagent.databinding.ActivityCustDetailBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CustDetail : AppCompatActivity() {
    private lateinit var clientId: String
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cust_detail)

        clientId = intent.getStringExtra("clientId") ?: ""
        firestore = FirebaseFirestore.getInstance()

        // Call a function to retrieve and display client data
        loadClientData()
    }

    private fun loadClientData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null && clientId.isNotEmpty()) {
            firestore.collection("users").document(userId)
                .collection("clients").document(clientId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val client = documentSnapshot.toObject(Clients::class.java)
                        displayClientData(client)
                    } else {
                        // Document with the given client ID doesn't exist
                        // Handle the situation accordingly
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle errors
                    Log.e("ClientDetailActivity", "Error loading client data", exception)
                }
        }
    }

    private fun displayClientData(client: Clients?) {
        // Update the UI with the client data
        if (client != null) {

            findViewById<TextView>(R.id.customer_name).text = client.cust_name
            findViewById<TextView>(R.id.institute_name).text = client.institute_name
            findViewById<TextView>(R.id.cust_loantype).text = client.cust_loantype
            findViewById<TextView>(R.id.Contact_person_name).text = client.contact_person_name
            findViewById<TextView>(R.id.Contact_person_Number).text = client.contact_person_number
            findViewById<TextView>(R.id.address).text = client.address

            findViewById<TextView>(R.id.Selected_city).text = client.selected_city
            findViewById<TextView>(R.id.Colony).text = client.colony
            findViewById<TextView>(R.id.property_address_as_per_site).text = client.property_address_as_per_site
            findViewById<TextView>(R.id.address_matching).text = client.address_matching
            findViewById<TextView>(R.id.Jurisdiction).text = client.jurisdiction
            findViewById<TextView>(R.id.landmark).text = client.landmark
            findViewById<TextView>(R.id.locality).text = client.locality
            findViewById<TextView>(R.id.NearestRailwayStation).text = client.nearest_railway_station
            findViewById<TextView>(R.id.NearestMetroStation).text = client.nearest_metro_station
            findViewById<TextView>(R.id.NearestBusStop).text = client.nearest_bus_stop
            findViewById<TextView>(R.id.widthofroad).text = client.width_of_road
            findViewById<TextView>(R.id.NearestHospital).text = client.nearest_hospital
            findViewById<TextView>(R.id.Anynegativetothelocality).text = client.any_negative_to_locality
            findViewById<TextView>(R.id.NeighborhoodType).text = client.neighborhood_type
            findViewById<TextView>(R.id.SiteAccesstext).text = client.site_access

            findViewById<TextView>(R.id.Statusofoccupancy).text = client.status_of_occupancy
            findViewById<TextView>(R.id.occupied_by).text = client.occupied_by
            findViewById<TextView>(R.id.OccupiedSince).text = client.occupied_since
            findViewById<TextView>(R.id.Relationship).text = client.relationship

            findViewById<TextView>(R.id.North).text = client.north
            findViewById<TextView>(R.id.South).text = client.south
            findViewById<TextView>(R.id.East).text = client.east
            findViewById<TextView>(R.id.West).text = client.west

            findViewById<TextView>(R.id.level_of_maintain).text = client.level_of_maintain
            findViewById<TextView>(R.id.Age_of_Property).text = client.age_of_property
            findViewById<TextView>(R.id.Amenities).text = client.amenities

        }
    }
}