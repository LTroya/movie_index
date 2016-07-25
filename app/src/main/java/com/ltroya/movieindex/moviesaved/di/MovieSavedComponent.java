package com.ltroya.movieindex.moviesaved.di;

import com.ltroya.movieindex.db.di.DBModule;
import com.ltroya.movieindex.libs.di.LibsModule;
import com.ltroya.movieindex.movielist.MovieListPresenter;
import com.ltroya.movieindex.movielist.di.MovieListModule;
import com.ltroya.movieindex.movielist.ui.adapters.MovieAdapter;
import com.ltroya.movieindex.moviesaved.ui.adapter.RealmMovieAdapter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MovieSavedModule.class, LibsModule.class, DBModule.class})
public interface MovieSavedComponent {
    MovieListPresenter getPresenter();
    RealmMovieAdapter getAdapter();
}
