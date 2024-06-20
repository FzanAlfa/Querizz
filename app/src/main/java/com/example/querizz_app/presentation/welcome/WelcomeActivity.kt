package com.example.querizz_app.presentation.welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.querizz_app.databinding.ActivityWelcomeBinding
import com.example.querizz_app.presentation.login.LoginActivity
import com.example.querizz_app.presentation.register.RegisterActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btSignup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btSignin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}