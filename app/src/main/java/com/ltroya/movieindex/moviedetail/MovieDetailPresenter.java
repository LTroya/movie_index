package com.ltroya.movieindex.moviedetail;

import com.ltroya.movieindex.db.events.DBEvent;
import com.ltroya.movieindex.entities.Movie;
import com.ltroya.movieindex.moviedetail.events.MovieDetailEvent;
import com.ltroya.movieindex.moviedetail.ui.MovieDetailView;
import com.ltroya.movieindex.movielist.ui.MovieListView;

public interface MovieDetailPresenter {
    void onCreate();
    void onDestroy();

    void onEventMainThread(MovieDetailEvent event);
    void onEventMainThread(DBEvent event);

    void getMovie(String movieId);
    void keepMovie(Movie movie);

    MovieDetailView getView();
}
