package com.example.querizz_app.presentation.register

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.querizz_app.databinding.ActivityRegisterBinding
import com.example.querizz_app.data.api.config.AuthApiConfig
import com.example.querizz_app.data.pref.UserPreference
import com.example.querizz_app.data.repository.AuthRepository
import com.example.querizz_app.presentation.view.AuthViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel: RegisterViewModel by viewModels {
        AuthViewModelFactory(AuthRepository(AuthApiConfig.getApiService(getTokenUser()), UserPreference.getInstance(this)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btSignup.setOnClickListener {
            setupRegister()
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.registerResult.observe(this) {
            when (it) {
                is RegisterViewModel.RegisterResult.Success -> {
                    setupAction(it.response)

                }
                is RegisterViewModel.RegisterResult.Error -> {
                    setupAction(it.error)
                }
                is RegisterViewModel.RegisterResult.Loading -> {

                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupRegister() {
        val name = binding.etName.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        viewModel.register(name, email, password)
    }

    private fun getTokenUser(): String {
        val sharedPreferences = getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)
        return sharedPreferences.getString(USER_TOKEN, "") ?: ""
    }

    private fun setupAction(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("OKE") { _, _ ->
                finish()
            }
            create()
            show()
        }
    }

    companion object {
        private const val USER_PREF = "user_preferences"
        private const val USER_TOKEN = "user_token"
    }
}