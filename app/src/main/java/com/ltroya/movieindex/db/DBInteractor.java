package com.ltroya.movieindex.db;

import com.ltroya.movieindex.entities.Movie;

import java.util.List;

public interface DBInteractor {
    /**
     * Save or delete a movie from DB
     *
     * @param movie that will be saved or deleted
     */
    void execute(Movie movie);

    /**
     * Get all movies from DB
     */
    void getMovies();

}
