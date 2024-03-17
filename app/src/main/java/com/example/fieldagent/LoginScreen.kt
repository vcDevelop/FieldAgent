package com.example.fieldagent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.fieldagent.databinding.ActivityLoginScreenBinding
import com.google.firebase.auth.FirebaseAuth

class LoginScreen : AppCompatActivity() {
    private lateinit var binding: ActivityLoginScreenBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var passET: EditText
    private lateinit var togglePassBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        passET = findViewById(R.id.passET)
        togglePassBtn = findViewById(R.id.togglePassBtn)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.createacount.setOnClickListener{
            val intent = Intent(this, SignupScreen::class.java)
            startActivity(intent)
        }
        binding.loginButton.setOnClickListener{
            val email = binding.EmailET.text.toString()
            val pass = binding.passET.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() ) {
                firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener{
                    if (it.isSuccessful) {
                        val intent = Intent(this, Dashboard::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Fill The Details", Toast.LENGTH_SHORT).show()
            }
        }
        togglePassBtn.setOnClickListener {
            // Toggle the visibility of the password field
            if (passET.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                passET.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                togglePassBtn.setImageResource(R.drawable.eye)
            } else {
                passET.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

            }
        }


    }

    fun ForgotPassword(view: View){
        val intent = Intent(this, forgot_password::class.java)
        startActivity(intent)
    }
}
