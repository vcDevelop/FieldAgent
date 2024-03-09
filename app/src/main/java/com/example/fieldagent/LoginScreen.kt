package com.example.fieldagent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.fieldagent.databinding.ActivityLoginScreenBinding
import com.google.firebase.auth.FirebaseAuth

class LoginScreen : AppCompatActivity() {
    private lateinit var binding: ActivityLoginScreenBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.createacount.setOnClickListener{
            val intent = Intent(this,SignupScreen::class.java)
            startActivity(intent)
        }
        binding.loginButton.setOnClickListener{
            val email = binding.EmailET.text.toString()
            val  pass = binding.passET.text.toString()


            if(email.isNotEmpty() && pass.isNotEmpty() ){
                firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener{
                    if(it.isSuccessful){
                        val intent = Intent(this, Dashboard::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this,it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }else{

                Toast.makeText(this,"Fill The Details", Toast.LENGTH_SHORT).show()
            }

        }

    }
    fun ForgotPassword(view: View){
        val intent = Intent(this,forgot_password::class.java)
        startActivity(intent)
    }

}