package com.example.fieldagent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fieldagent.databinding.ActivityLoginScreenBinding

class LoginScreen : AppCompatActivity() {
    private val binding: ActivityLoginScreenBinding by lazy{
        ActivityLoginScreenBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.loginButton.setOnClickListener{
            val intent = Intent(this,Dashboard::class.java)
            startActivity(intent)
        }
        binding.createacount.setOnClickListener{
            val intent = Intent(this,SignupScreen::class.java)
            startActivity(intent)
        }



    }
}