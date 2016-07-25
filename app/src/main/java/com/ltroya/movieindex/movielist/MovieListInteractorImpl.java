package com.ltroya.movieindex.movielist;

import android.util.Log;

public class MovieListInteractorImpl implements MovieListInteractor{
    private final String TAG = MovieListInteractorImpl.class.getSimpleName();

    private MovieListRepository repository;

    public MovieListInteractorImpl(MovieListRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(int page) {
        repository.getMovies(page);
    }
}
