// SignupScreen.kt
package com.example.fieldagent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
import android.util.Patterns
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.fieldagent.databinding.ActivitySignupScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class SignupScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySignupScreenBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private lateinit var confirmPassET: EditText
    private lateinit var passET: EditText
    private lateinit var togglePassBtn: ImageButton
    private lateinit var toggleConfirmPassBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        confirmPassET = findViewById(R.id.ConfirmpassET)
        toggleConfirmPassBtn = findViewById(R.id.toggleConfirmPassBtn)
        passET = findViewById(R.id.passET)
        togglePassBtn = findViewById(R.id.togglePassBtn)
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
                    if (isStrongPassword(pass)) {
                        if (pass == confirmpass) {
                            firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val userId = firebaseAuth.currentUser!!.uid
                                    val docRef: DocumentReference = fStore.collection("users").document(userId)
                                    val userData = User(
                                        name = name,
                                        email = email,
                                        Phn = phn
                                    )
                                    docRef.set(userData).addOnSuccessListener {
                                        val intent = Intent(this, LoginScreen::class.java)
                                        startActivity(intent)
                                        Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show()
                                        finish()
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
                        Toast.makeText(this, "Password should be strong: at least 8 characters long, containing uppercase, lowercase, numbers, and special characters", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Invalid email address or phone number", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fill The Details", Toast.LENGTH_SHORT).show()
            }
        }

        togglePassBtn.setOnClickListener {
            togglePasswordVisibility(passET, togglePassBtn)
        }

        toggleConfirmPassBtn.setOnClickListener {
            togglePasswordVisibility(confirmPassET, toggleConfirmPassBtn)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        return phone.length == 10 && phone.all { it.isDigit() }
    }

    private fun isStrongPassword(password: String): Boolean {
        val pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$".toRegex()
        return password.matches(pattern)
    }

    private fun togglePasswordVisibility(editText: EditText, toggleButton: ImageButton) {
        if (editText.inputType == TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            toggleButton.setImageResource(R.drawable.eye)
        } else {
            editText.inputType = TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

        }
        editText.setSelection(editText.text.length) // Move cursor to the end of the text
    }
}
