package com.example.querizz_app.presentation.result

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.querizz_app.databinding.ActivityResultBinding
import com.example.querizz_app.presentation.quiz.QuizActivity

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val results = intent.getStringExtra("SUMMARY_RESULTS")

        binding.tvSummary.text = results

        binding.btnGetSummarize.setOnClickListener {
            val intent = Intent(this@ResultActivity, QuizActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}