package com.example.querizz_app.presentation.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.querizz_app.adapter.SumAdapter
import com.example.querizz_app.data.api.config.ApiConfig
import com.example.querizz_app.data.pref.UserPreference
import com.example.querizz_app.data.repository.Repository
import com.example.querizz_app.data.response.ApiResponse
import com.example.querizz_app.data.response.DataItem
import com.example.querizz_app.databinding.ActivityHomeBinding
import com.example.querizz_app.presentation.add.AddSumActivity
import com.example.querizz_app.presentation.view.ViewModelFactory
import com.example.querizz_app.presentation.welcome.WelcomeActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding

    private lateinit var sumAdapter: SumAdapter

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory(Repository(ApiConfig.getApiService(getTokenUser()), UserPreference.getInstance(this)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvSummary.layoutManager = LinearLayoutManager(this)
        sumAdapter = SumAdapter()
        binding.rvSummary.adapter = sumAdapter
        checkAuthority()
        binding.logout.setOnClickListener {
            viewModel.logout()
            val intent = Intent(this@HomeActivity, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.fabUpload.setOnClickListener {
            navigateToUpload()
        }
    }

    private fun checkAuthority() {
        viewModel.getSession().observe(this) { user ->
            if (user.token.isNullOrEmpty()) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                viewModel.getSummaries().observe(this) { response ->
                    when(response) {
                        is ApiResponse.Loading -> {
                            showLoading(true)
                        }
                        is ApiResponse.Success -> {
                            showLoading(false)
                            response.data.let {sumResponse ->
                                sumResponse.data?.let { dataItems ->
                                    val filteredList = dataItems.filterNotNull()
                                    setSummaryData(filteredList)
                                }
                            }
                        }
                        is ApiResponse.Error -> {
                            showLoading(false)
                            showToast(response.error)
                        }
                    }
                }
                binding.tvHome.text = "Hello ${user.name}"
            }
        }
    }

    private fun navigateToUpload() {
        val intent = Intent(this@HomeActivity, AddSumActivity::class.java)
        startActivity(intent)
    }

    private fun setSummaryData(sumList : List<DataItem>) {
        val adapter = SumAdapter()
        adapter.submitList(sumList)
        binding.rvSummary.adapter = adapter
    }

    private fun getTokenUser(): String {
        val sharedPreferences = getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)
        return sharedPreferences.getString(USER_TOKEN, "") ?: ""
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val USER_PREF = "user_preferences"
        private const val USER_TOKEN = "user_token"
    }
}