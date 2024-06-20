package com.example.querizz_app.presentation.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.querizz_app.R
import com.example.querizz_app.data.response.ResultsItem
import com.example.querizz_app.databinding.FragmentSummaryBinding

class SummaryFragment : Fragment() {

    private lateinit var binding: FragmentSummaryBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSummaryBinding.bind(view)
        val resultsItem = requireArguments().getParcelable<ResultsItem>("resultsItem")
        if (resultsItem != null) {
            sumData(resultsItem)
        }
        binding.tvSummary.text = resources.getString(R.string.result_summary)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun sumData(sum: ResultsItem) {
        binding.tvSummary.text = sum.label
    }

    companion object {
        fun newInstance(resultsItem: ResultsItem?): SummaryFragment {
            val fragment = SummaryFragment()
            val args = Bundle().apply {
                putParcelable("resultsItem", resultsItem)
            }
            fragment.arguments = args
            return fragment
        }
    }
}