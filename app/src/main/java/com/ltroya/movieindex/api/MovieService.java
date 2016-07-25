package com.ltroya.movieindex.api;

import com.ltroya.movieindex.entities.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {
    /**
     * Get a list of movies
     * @param apiKey of the themoviedb.org page
     * @param page of the movie list
     * @return a {@link com.ltroya.movieindex.entities.Movie} list
     */
    @GET("movie/popular")
    Call<PopularMovieResponse> getMovies(@Query("api_key") String apiKey, @Query("page") int page);

    /**
     *
     * @param movieId of the movie that will be get
     * @param apiKey of the themoviedb.org page
     * @return {@link com.ltroya.movieindex.entities.Movie}
     */
    @GET("movie/{id}")
    Call<Movie> getMovie(@Path("id") String movieId, @Query("api_key") String apiKey);
}
