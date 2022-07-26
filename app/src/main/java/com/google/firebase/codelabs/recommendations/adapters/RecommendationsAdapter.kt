package com.google.firebase.codelabs.recommendations.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.codelabs.recommendations.R
import com.google.firebase.codelabs.recommendations.client.RecommendationClient

/**
 * Used to populate the recommendations results.
 */
class RecommendationsAdapter :
    ListAdapter<RecommendationClient.Result, RecommendationsAdapter.ViewHolder>
        (
        RecommendationsDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_recommendation, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = getItem(position)
        holder.contentView.text = result.item.title
        holder.confidenceView.text = String.format("%.5f", result.confidence)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contentView: TextView = view.findViewById(R.id.title)
        val confidenceView: TextView = view.findViewById(R.id.confidence)

        init {
        }

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}

private class RecommendationsDiffCallback : DiffUtil.ItemCallback<RecommendationClient.Result>() {

    override fun areItemsTheSame(
        oldItem: RecommendationClient.Result,
        newItem: RecommendationClient.Result
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: RecommendationClient.Result,
        newItem: RecommendationClient.Result
    ): Boolean {
        return oldItem == newItem
    }
}
