package com.example.querizz_app.presentation.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.querizz_app.data.api.config.AuthApiConfig
import com.example.querizz_app.data.model.UserModel
import com.example.querizz_app.data.pref.UserPreference
import com.example.querizz_app.data.repository.AuthRepository
import com.example.querizz_app.data.response.ApiResponse
import com.example.querizz_app.databinding.ActivityLoginBinding
import com.example.querizz_app.presentation.home.HomeActivity
import com.example.querizz_app.presentation.view.AuthViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel> {
        AuthViewModelFactory(AuthRepository(AuthApiConfig.getApiService(getTokenUser()), UserPreference.getInstance(this)))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSignIn.setOnClickListener {
            login()
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

    }

    private fun login() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        viewModel.login(email, password).observe(this@LoginActivity) { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    showLoading(true)
                }
                is ApiResponse.Success -> {
                    showLoading(false)
                    val token = response.data.token
                    val name = response.data.nama
                    viewModel.saveSession(UserModel(email, name, token!!, true))
                    viewModel.saveSessionStatus.observe(this@LoginActivity) { isSaved ->
                        setupAction("Login Success")
                    }
                }
                is ApiResponse.Error -> {
                    showLoading(false)
                }
            }
        }
    }

    private fun setupAction(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("OKE") { _, _ ->
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun getTokenUser(): String {
        val sharedPreferences = getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)
        return sharedPreferences.getString(USER_TOKEN, "") ?: ""
    }

    companion object {
        private const val USER_PREF = "user_preferences"
        private const val USER_TOKEN = "user_token"
    }
}