package com.example.querizz_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.querizz_app.R
import com.example.querizz_app.data.model.QuizModel
import com.example.querizz_app.databinding.ItemQuizBinding

class QuizResultAdapter(private val questionList: ArrayList<QuizModel>, private val context: Context) : RecyclerView.Adapter<QuizResultAdapter.ListViewHolder>() {

    class ListViewHolder(var binding: ItemQuizBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = ItemQuizBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (question, aAnswer, bAnswer, cAnswer, dAnswer, correctAnswer) = questionList[position]
        holder.binding.tvQuestion.text = question
        holder.binding.aAnswer.text = aAnswer
        holder.binding.bAnswer.text = bAnswer
        holder.binding.cAnswer.text = cAnswer
        holder.binding.dAnswer.text = dAnswer
        holder.binding.aAnswer.isEnabled = false
        holder.binding.bAnswer.isEnabled = false
        holder.binding.cAnswer.isEnabled = false
        holder.binding.dAnswer.isEnabled = false

        val correctColor = context.resources.getDrawable(R.drawable.answer_correct) // Assuming this is your color string

        when (correctAnswer) {
            aAnswer -> holder.binding.aAnswer.setBackgroundDrawable(correctColor)
            bAnswer -> holder.binding.bAnswer.setBackgroundDrawable(correctColor)
            cAnswer -> holder.binding.cAnswer.setBackgroundDrawable(correctColor)
            dAnswer -> holder.binding.dAnswer.setBackgroundDrawable(correctColor)
        }
    }

    override fun getItemCount(): Int = questionList.size
}