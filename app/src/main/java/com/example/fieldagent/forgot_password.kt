package com.example.fieldagent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task

class forgot_password : AppCompatActivity() {
    private lateinit var send: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var email: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        send = findViewById(R.id.Forgot_Button)
        email = findViewById(R.id.EmailForgot)
        firebaseAuth = FirebaseAuth.getInstance()

        send.setOnClickListener {
            val emailAddress = email.text.toString().trim()

            if (emailAddress.isEmpty()) {
                Toast.makeText(this, "Field is Empty enter email", Toast.LENGTH_SHORT).show()
            } else {
                firebaseAuth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(object : OnCompleteListener<Void> {
                        override fun onComplete(task: Task<Void>) {
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@forgot_password,
                                    "A password reset email has been sent (if the email is registered).",
                                    Toast.LENGTH_LONG
                                ).show()
                                val intent = Intent(this@forgot_password,LoginScreen::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    this@forgot_password,
                                    "Failed to send the password reset email. Please try again later.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
            }
        }
    }
}