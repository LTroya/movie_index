package com.ltroya.movieindex.movielist;

import com.ltroya.movieindex.db.events.DBEvent;
import com.ltroya.movieindex.entities.Movie;
import com.ltroya.movieindex.movielist.events.MovieListEvent;
import com.ltroya.movieindex.movielist.ui.MovieListView;

import java.util.List;

public interface MovieListPresenter {
    void onResume();
    void onPause();
    void onDestroy();

    void getMovies(int page);
    void keepMovie(Movie movie);

    void onEventMainThread(MovieListEvent event);
    void onEventMainThread(DBEvent event);

    MovieListView getView();
}
