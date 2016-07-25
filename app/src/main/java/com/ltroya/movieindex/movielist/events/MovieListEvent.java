package com.ltroya.movieindex.movielist.events;

import com.ltroya.movieindex.entities.Movie;

import java.util.List;

public class MovieListEvent {
    private int type;
    private String error;
    private List<Movie> movieList;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
