package com.example.querizz_app.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.querizz_app.data.response.ResultsItem
import com.example.querizz_app.presentation.detail.QuizFragment
import com.example.querizz_app.presentation.detail.SummaryFragment

class SectionsPagerAdapter(
    fa: FragmentActivity,
    private val resultsItem: ResultsItem?
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SummaryFragment.newInstance(resultsItem)
            1 -> QuizFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}