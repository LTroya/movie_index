package com.ltroya.movieindex.moviedetail;

import com.ltroya.movieindex.MovieIndexApp;
import com.ltroya.movieindex.api.MovieService;
import com.ltroya.movieindex.db.DBRepository;
import com.ltroya.movieindex.entities.Movie;
import com.ltroya.movieindex.libs.base.EventBus;
import com.ltroya.movieindex.moviedetail.events.MovieDetailEvent;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailRepositoryImpl implements MovieDetailRepository {
    private final String TAG = getClass().getSimpleName();
    private final String API_KEY = MovieIndexApp.API_KEY;

    private MovieService service;
    private EventBus eventBus;
    private DBRepository repository;

    public MovieDetailRepositoryImpl(MovieService service, EventBus eventBus, DBRepository repository) {
        this.service = service;
        this.eventBus = eventBus;
        this.repository = repository;
    }

    @Override
    public void getMovie(String movieId) {
        Call<Movie> call = service.getMovie(movieId, API_KEY);
        Callback<Movie> callback = new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    Movie movie = response.body();
                    boolean exists = repository.exists(movie);
                    movie.setKeep(exists);
                    post(MovieDetailEvent.GET_EVENT, movie);
                } else {
                    post(MovieDetailEvent.GET_EVENT, response.message());
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                post(MovieDetailEvent.GET_EVENT, t.getLocalizedMessage());
            }
        };

        call.enqueue(callback);
    }

    private void post(int type, String error) {
        post(type, error, null);
    }

    private void post(int type, Movie movie) {
        post(type, null, movie);
    }

    private void post(int type, String error, Movie movie) {
        MovieDetailEvent event = new MovieDetailEvent();
        event.setType(type);
        event.setError(error);
        event.setMovie(movie);
        eventBus.post(event);
    }
}
