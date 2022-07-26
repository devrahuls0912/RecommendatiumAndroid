package com.google.firebase.codelabs.recommendations.data

    data class Movie(
        val id: Int,
        val title: String,
        val genres: List<String>,
        val count: Int,
        var liked: Boolean
    )