package com.ltroya.movieindex.db;

import com.ltroya.movieindex.entities.Movie;

import java.util.List;

import io.realm.RealmResults;

public interface DBRepository {
    /**
     * If the movie exists, it will be removed, otherwise
     * the movie wil be saved on bd.
     *
     * @param movie that will be saved
     */
    void handleMovie(Movie movie);

    /**
     * Get all movies in the DB
     *
     * @return {@link Movie}
     */
    void getMovies();

    /**
     * Check if a movie exists on DB
     *
     * @param movie that will be checked
     * @return {@link Boolean}
     */
    boolean exists(Movie movie);

    /**
     * Set keep to true if the movie exists on DB, otherwise
     * keep will be set to false
     * @param movieList that will be checked
     * @return {@link List<Movie>}
     */
    List<Movie> exists(List<Movie> movieList);
}
