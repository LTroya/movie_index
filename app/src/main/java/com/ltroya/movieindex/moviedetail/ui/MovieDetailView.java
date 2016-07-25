package com.ltroya.movieindex.moviedetail.ui;

import com.ltroya.movieindex.entities.Movie;

public interface MovieDetailView {
    void showProgress();
    void hideProgress();

    void showContentElements();
    void hideContentElements();

    void setMovie(Movie movie);
    void onError(String error);

    void saveMovie();
    void removeMovie();

    void showMessage(int movie_detail_save_favorite_movie);
}
