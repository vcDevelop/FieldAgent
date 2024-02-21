package com.example.fieldagent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.fieldagent.databinding.ActivitySignupScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class SignupScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySignupScreenBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

        binding.Signupbutton.setOnClickListener {
            val email = binding.EmailET.text.toString()
            val pass = binding.passET.text.toString()
            val name = binding.nameET.text.toString()
            val confirmpass = binding.ConfirmpassET.text.toString()
            val phn = binding.PhnET.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && name.isNotEmpty()) {
                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = firebaseAuth.currentUser!!.uid
                        val docRef: DocumentReference = fStore.collection("users").document(userId)

                        // Create a Users object to store user data
                        val userData = User(
                            name = name,
                            email = email,
                            Phn = phn
                            // Add other user details as needed
                        )

                        docRef.set(userData).addOnSuccessListener {
                            val intent = Intent(this, LoginScreen::class.java)
                            startActivity(intent)
                            finish() // Optional: Close the current activity
                        }.addOnFailureListener { e ->
                            Toast.makeText(this, "Error saving data to Firestore: $e", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(this, "Error creating user: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Empty Fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
