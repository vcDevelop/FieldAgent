package com.example.fieldagent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fieldagent.databinding.ActivitySignupScreenBinding

class SignupScreen : AppCompatActivity() {
    private val binding: ActivitySignupScreenBinding by lazy{
        ActivitySignupScreenBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.Signupbutton.setOnClickListener{
            val intent = Intent(this,Dashboard::class.java)
            startActivity(intent)
        }

    }
}