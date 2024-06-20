package com.example.querizz_app.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.querizz_app.data.response.DataItem
import com.example.querizz_app.presentation.detail.DetailActivity
import com.example.querizz_app.databinding.ItemSummaryBinding

class SumAdapter : ListAdapter<DataItem, SumAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder (val binding: ItemSummaryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(summary: DataItem) {
            binding.tvItemTitle.text = summary.title
            binding.tvItemDescription.text = summary.subtitle
            val resultsItem = summary.results?.get(0)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_SUMMARY, resultsItem)
                intent.putExtra(DetailActivity.EXTRA_TITLE, summary.title)
                intent.putExtra(DetailActivity.EXTRA_SUBTITLE, summary.subtitle)
                itemView.context.startActivity(intent)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemSummaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val summary = getItem(position)
        if (summary != null) {
            holder.bind(summary)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}