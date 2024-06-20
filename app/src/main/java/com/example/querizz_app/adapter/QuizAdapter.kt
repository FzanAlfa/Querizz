package com.example.querizz_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.querizz_app.data.model.QuizModel
import com.example.querizz_app.databinding.ItemQuizBinding

class QuizAdapter(private val questionList: ArrayList<QuizModel>) : RecyclerView.Adapter<QuizAdapter.ListViewHolder>() {

    class ListViewHolder(var binding: ItemQuizBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemQuizBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = questionList.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (question, aAnswer, bAnswer, cAnswer, dAnswer) = questionList[position]
        holder.binding.tvQuestion.text = question
        holder.binding.aAnswer.text = aAnswer
        holder.binding.bAnswer.text = bAnswer
        holder.binding.cAnswer.text = cAnswer
        holder.binding.dAnswer.text = dAnswer
    }

}