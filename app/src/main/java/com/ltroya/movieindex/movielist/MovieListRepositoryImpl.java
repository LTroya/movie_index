package com.ltroya.movieindex.movielist;

import android.util.Log;

import com.ltroya.movieindex.MovieIndexApp;
import com.ltroya.movieindex.api.MovieClient;
import com.ltroya.movieindex.api.MovieService;
import com.ltroya.movieindex.api.PopularMovieResponse;
import com.ltroya.movieindex.db.DBRepository;
import com.ltroya.movieindex.entities.Movie;
import com.ltroya.movieindex.libs.base.EventBus;
import com.ltroya.movieindex.movielist.events.MovieListEvent;

import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListRepositoryImpl implements MovieListRepository {
    private final String TAG = MovieListRepositoryImpl.class.getSimpleName();

    private EventBus eventBus;
    private MovieService service;
    private DBRepository repository;

    public MovieListRepositoryImpl(EventBus eventBus, MovieService service, DBRepository repository) {
        this.eventBus = eventBus;
        this.service = service;
        this.repository = repository;
    }

    @Override
    public void getMovies(int page) {
        Call<PopularMovieResponse> call = service.getMovies(MovieIndexApp.API_KEY, page);
        Callback<PopularMovieResponse> callback = new Callback<PopularMovieResponse>() {
            @Override
            public void onResponse(Call<PopularMovieResponse> call, Response<PopularMovieResponse> response) {
                if (response.isSuccessful()) {
                    PopularMovieResponse popularMovieResponse = response.body();
                    List<Movie> movies = popularMovieResponse.getResults();
                    repository.exists(movies);
                    post(movies);
                } else {
                    post(response.message());
                }
            }

            @Override
            public void onFailure(Call<PopularMovieResponse> call, Throwable t) {
                post(t.getLocalizedMessage());
            }
        };

        call.enqueue(callback);
    }

    private void post(String error) {
        post(error, null);
    }

    private void post(List<Movie> movies) {
        post(null, movies);
    }

    private void post(String error, List<Movie> movies) {
        MovieListEvent event = new MovieListEvent();
        event.setMovieList(movies);
        event.setError(error);
        eventBus.post(event);
    }
}
