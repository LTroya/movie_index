package com.ltroya.movieindex.moviedetail;

public class GetMovieInteractorImpl implements GetMovieInteractor{
    private MovieDetailRepository repository;

    public GetMovieInteractorImpl(MovieDetailRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(String movieId) {
        repository.getMovie(movieId);
    }
}
