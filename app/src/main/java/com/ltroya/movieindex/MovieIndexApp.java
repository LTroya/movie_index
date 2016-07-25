package com.ltroya.movieindex;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.ltroya.movieindex.db.di.DBModule;
import com.ltroya.movieindex.libs.di.LibsModule;
import com.ltroya.movieindex.moviedetail.di.DaggerMovieDetailComponent;
import com.ltroya.movieindex.moviedetail.di.MovieDetailComponent;
import com.ltroya.movieindex.moviedetail.di.MovieDetailModule;
import com.ltroya.movieindex.moviedetail.ui.MovieDetailView;
import com.ltroya.movieindex.movielist.di.DaggerMovieListComponent;
import com.ltroya.movieindex.movielist.di.MovieListComponent;
import com.ltroya.movieindex.movielist.di.MovieListModule;
import com.ltroya.movieindex.movielist.ui.MovieListView;
import com.ltroya.movieindex.movielist.ui.adapters.OnItemClickListener;
import com.ltroya.movieindex.moviesaved.di.DaggerMovieSavedComponent;
import com.ltroya.movieindex.moviesaved.di.MovieSavedComponent;
import com.ltroya.movieindex.moviesaved.di.MovieSavedModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MovieIndexApp extends Application {
    public static final String IMAGE_API_SIZE = "w342";
    public static final String API_KEY = BuildConfig.THEMOVIEDB_API_KEY;

    private final int DB_SCHEMA_VERSION = 1;
    private final String DB_SCHEMA_NAME = "movie.realm";

    @Override
    public void onCreate() {
        super.onCreate();
        initDB();
    }

    private void initDB() {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .name(DB_SCHEMA_NAME)
                .schemaVersion(DB_SCHEMA_VERSION)
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }

    public MovieListComponent getMovieListComponent(MovieListView view, OnItemClickListener listener, Fragment fragment) {
        return DaggerMovieListComponent
                .builder()
                .libsModule(new LibsModule())
                .dBModule(new DBModule())
                .movieListModule(new MovieListModule(view, listener, fragment))
                .build();
    }

    public MovieDetailComponent getMovieDetailComponent(MovieDetailView view, Activity activity) {
        return DaggerMovieDetailComponent
                .builder()
                .dBModule(new DBModule())
                .libsModule(new LibsModule())
                .movieDetailModule(new MovieDetailModule(view, activity))
                .build();
    }

    public MovieSavedComponent getMovieSavedComponent(MovieListView view, Fragment fragment, OnItemClickListener listener) {
        return DaggerMovieSavedComponent
                .builder()
                .dBModule(new DBModule())
                .libsModule(new LibsModule())
                .movieSavedModule(new MovieSavedModule(view, fragment, listener))
                .build();
    }
}
