package com.ltroya.movieindex.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieClient {
    // Examples URLs:
    // public static final String IMAGE_MOVIE_URL = "http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg";
    // public static final String POPULAR_MOVIE_URL = "http://api.themoviedb.org/3/movie/popular?api_key=64fc0847c456348ccbfbadb87c71ebe7&page=2";

    private Retrofit retrofit;
    private final static String BASE_URL = "http://api.themoviedb.org/3/";

    public MovieClient() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public MovieService getMovieService() {
        return this.retrofit.create(MovieService.class);
    }
}
