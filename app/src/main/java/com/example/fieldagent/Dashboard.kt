package com.example.fieldagent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fieldagent.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject


class Dashboard : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var userList:ArrayList<User>
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var showName:TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager= LinearLayoutManager(this)

        userList = arrayListOf()
        auth = FirebaseAuth.getInstance()
        fStore=FirebaseFirestore.getInstance()
        val userId = auth.currentUser?.uid


        val collectionReference = fStore.collection("users").document(userId!!).collection("clients")

        collectionReference.get()
            .addOnSuccessListener{
              if(!it.isEmpty){
                  for (data in it.documents){
                      val user:User? = data.toObject(User::class.java)

                      if(user != null ){
                          userList.add(user)
                      }
                  }
                  recyclerView.adapter = MyAdaptor(userList)
              }
            }
            .addOnFailureListener{
                Toast.makeText(this,it.toString(),Toast.LENGTH_SHORT).show()
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
}