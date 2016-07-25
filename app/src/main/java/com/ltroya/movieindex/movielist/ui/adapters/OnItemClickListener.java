package com.ltroya.movieindex.movielist.ui.adapters;

import com.ltroya.movieindex.entities.Movie;

public interface OnItemClickListener {
    void onItemClick(Movie movie);
    void onSaveButton(Movie movie);
}
