package com.google.firebase.codelabs.recommendations.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.codelabs.recommendations.client.RecommendationClient
import com.google.firebase.codelabs.recommendations.adapters.RecommendationsAdapter
import com.google.firebase.codelabs.recommendations.data.Movie
import com.google.firebase.codelabs.recommendations.databinding.FragmentRecommendationsBinding
import com.google.firebase.codelabs.recommendations.utils.Config
import com.google.firebase.codelabs.recommendations.viewmodels.LikedMoviesViewModel
import kotlinx.coroutines.launch

/**
 * A fragment containing the list of generated movie recommendations for the items in the Liked
 * page.
 */
class RecommendationsFragment : Fragment() {

    private var config = Config()
    private var client: RecommendationClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        client = RecommendationClient(requireContext(), config)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRecommendationsBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = RecommendationsAdapter()
        binding.list.adapter = adapter
        val viewModel: LikedMoviesViewModel = ViewModelProvider(requireActivity()).get(
            LikedMoviesViewModel::class.java
        )
        viewModel.movies.observe(viewLifecycleOwner) {
            recommend(it.filter { movie -> movie.liked }.toList(), adapter)
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            client?.load()
        }
    }

    override fun onStop() {
        lifecycleScope.launch {
            client?.unload()
        }
        super.onStop()
    }

    /** Sends selected movie list and get recommendations.  */
    private fun recommend(movies: List<Movie>, adapter: RecommendationsAdapter) {
        lifecycleScope.launch {

            // Run inference with TF Lite.
            Log.d(TAG, "Run inference with TFLite model.")
            client?.recommend(movies)?.run {
                Log.d(TAG, toString())
                adapter.submitList(this)
            }
        }
    }

    companion object {
        private val TAG = "RecommendationsFragment";
    }
}