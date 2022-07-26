package com.google.firebase.codelabs.recommendations.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.codelabs.recommendations.adapters.FilterType
import com.google.firebase.codelabs.recommendations.adapters.ItemClickListener
import com.google.firebase.codelabs.recommendations.adapters.MoviesAdapter
import com.google.firebase.codelabs.recommendations.data.Movie
import com.google.firebase.codelabs.recommendations.databinding.FragmentMovieListBinding
import com.google.firebase.codelabs.recommendations.viewmodels.LikedMoviesViewModel

/**
 * A fragment containing the list of available movies recognized in our model.
 */
class MovieListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMovieListBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val viewModel: LikedMoviesViewModel = ViewModelProvider(requireActivity())[LikedMoviesViewModel::class.java]
        val movieClickListener = object : ItemClickListener() {
            override fun onLike(movie: Movie) {
                viewModel.onMovieLiked(movie)
            }

            override fun onRemoveLike(movie: Movie) {
                viewModel.onMovieLikeRemoved(movie)
            }
        }
        val adapter = MoviesAdapter(movieClickListener, FilterType.NONE)
        binding.list.adapter = adapter
        viewModel.movies.observe(viewLifecycleOwner) {
            adapter.submitList(it.toList())
            adapter.notifyDataSetChanged()
        }
        setHasOptionsMenu(true)
        return binding.root
    }
}