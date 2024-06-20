package com.example.querizz_app.presentation.add

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.querizz_app.R
import com.example.querizz_app.data.response.ApiResponse
import com.example.querizz_app.databinding.ActivityAddSumBinding
import com.example.querizz_app.presentation.result.ResultActivity
import com.example.querizz_app.presentation.view.ViewModelFactory
import com.example.querizz_app.util.uriToFile
class AddSumActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSumBinding
    private var currentFileUri: Uri? = null

    private val viewModel by viewModels<AddSumViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddSumBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.fileUpload.setOnClickListener {
            pickFile()
        }

        binding.summarize.setOnClickListener {
            currentFileUri?.let { uri ->
                val title = binding.etTitle.text.toString()
                val subtitle = binding.etSubtitle.text.toString()
                uploadFile(uri, title, subtitle)
            } ?: showToast("Please select an image first")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            currentFileUri = data?.data
        }
    }

    private fun uploadFile(uri: Uri, title: String, subtitle: String) {
        viewModel.getSession()
        viewModel.uploadFile(uriToFile(uri, this), title, subtitle).observe(this) { response ->
            when(response) {
                is ApiResponse.Loading -> {
                    showLoading(true)
                }
                is ApiResponse.Success -> {
                    val dummyResults = getString(R.string.result_summary)
                    val mimeType = contentResolver.getType(uri) ?: "application/octet-stream"
                    if(mimeType == "image/jpeg" || mimeType == "image/png" || mimeType == "image/jpg") {
                        binding.ivPreview
                    }
                    val intent = Intent(this@AddSumActivity, ResultActivity::class.java).apply {
                        putExtra("SUMMARY_RESULTS", dummyResults)
                    }
                    startActivity(intent)
                    finish()
                }
                is ApiResponse.Error -> {
                    showLoading(false)
                    showToast(response.error)
                }
            }
        }
    }

    private fun pickFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(Intent.createChooser(intent, "Select a file"), PICK_FILE_REQUEST_CODE)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val PICK_FILE_REQUEST_CODE = 1
    }
}