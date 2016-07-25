package com.ltroya.movieindex.db.events;

import com.ltroya.movieindex.entities.Movie;

import java.util.List;

import io.realm.RealmResults;

public class DBEvent {
    private int type;
    private String error;
    private RealmResults<Movie> movieList;
    private Movie movie;

    public static final int SAVE_EVENT = 0;
    public static final int DELETE_EVENT = 1;
    public static final int GET_EVENT = 2;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public RealmResults<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(RealmResults<Movie> movieList) {
        this.movieList = movieList;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
