package com.example.querizz_app.presentation.quiz

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.querizz_app.R
import com.example.querizz_app.adapter.QuizAdapter
import com.example.querizz_app.data.model.QuizModel
import com.example.querizz_app.databinding.ActivityQuizBinding
import com.example.querizz_app.presentation.home.HomeActivity

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private val quizList = ArrayList<QuizModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSubmit.setOnClickListener {
            submitQuiz()
        }
        binding.rvQuiz.setHasFixedSize(true)
        quizList.addAll(getQuizList())
        showRecyclerView()
    }

    private fun getQuizList() : ArrayList<QuizModel> {
        val dataQuestion = resources.getStringArray(R.array.question)
        val dataAanswer = resources.getStringArray(R.array.a_answer)
        val dataBanswer = resources.getStringArray(R.array.b_answer)
        val dataCanswer = resources.getStringArray(R.array.c_answer)
        val dataDanswer = resources.getStringArray(R.array.d_answer)
        val dataCorrectAnswer = resources.getStringArray(R.array.correct_answer)
        val quizList = ArrayList<QuizModel>()
        for (i in dataQuestion.indices) {
            val quiz = QuizModel(dataQuestion[i], dataAanswer[i], dataBanswer[i], dataCanswer[i], dataDanswer[i], dataCorrectAnswer[i])
            quizList.add(quiz)
        }
        return quizList
    }

    private fun showRecyclerView() {
        binding.rvQuiz.layoutManager = LinearLayoutManager(this)
        val listDestinationAdapter = QuizAdapter(quizList)
        binding.rvQuiz.adapter = listDestinationAdapter
    }

    private fun submitQuiz() {
        AlertDialog.Builder(this).apply {
            setTitle("Your Result!")
            setMessage("You have completed the quiz")
            setPositiveButton("Back") { _, _ ->
                val intent = Intent(context, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }
}