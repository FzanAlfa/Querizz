package com.example.querizz_app.presentation.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.querizz_app.R
import com.example.querizz_app.adapter.QuizResultAdapter
import com.example.querizz_app.data.model.QuizModel
import com.example.querizz_app.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {

    private lateinit var binding: FragmentQuizBinding
    private val quizList = ArrayList<QuizModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentQuizBinding.bind(view)
        binding.rvQuiz.setHasFixedSize(true)
        quizList.addAll(getQuizList())
        showRecyclerView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
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
        binding.rvQuiz.layoutManager = LinearLayoutManager(requireContext())
        val listDestinationAdapter = QuizResultAdapter(quizList, requireActivity())
        binding.rvQuiz.adapter = listDestinationAdapter
    }

}