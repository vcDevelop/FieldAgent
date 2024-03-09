package com.example.fieldagent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.fieldagent.LoginScreen
import com.example.fieldagent.User
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
                if (isValidEmail(email) && isValidPhoneNumber(phn)) {
                    if (pass == confirmpass) {
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
                        Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Invalid email address or phone number", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fill The Details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        // Simple validation for a 10-digit phone number
        return phone.length == 10 && phone.all { it.isDigit() }
    }
}