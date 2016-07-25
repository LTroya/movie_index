package com.ltroya.movieindex.db;

import com.ltroya.movieindex.entities.Movie;

public class DBInteractorImpl implements DBInteractor {
    private DBRepository repository;

    public DBInteractorImpl(DBRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(Movie movie) {
        repository.handleMovie(movie);
    }

    @Override
    public void getMovies() {
        repository.getMovies();
    }
}
