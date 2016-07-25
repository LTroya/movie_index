package com.ltroya.movieindex.moviedetail.events;

import com.ltroya.movieindex.entities.Movie;

public class MovieDetailEvent {
    private int type;
    private Movie movie;
    private String error;

    public static final int GET_EVENT = 0;

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

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
