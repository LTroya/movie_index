package com.ltroya.movieindex.movielist.ui;

import com.ltroya.movieindex.entities.Movie;

import java.util.List;

public interface MovieListView {
    void showProgress();
    void hideProgress();

    void showUIElements();
    void hideUIElements();

    void noInfo();

    void onError(String error);
    void setMovies(List<Movie> movies);

    void removeMovie(Movie movie);
    void saveMovie(Movie movie);
}
