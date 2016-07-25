package com.ltroya.movieindex.movielist.di;

import com.ltroya.movieindex.db.di.DBModule;
import com.ltroya.movieindex.libs.di.LibsModule;
import com.ltroya.movieindex.movielist.MovieListPresenter;
import com.ltroya.movieindex.movielist.ui.adapters.MovieAdapter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MovieListModule.class, LibsModule.class, DBModule.class})
public interface MovieListComponent {
    MovieListPresenter getPresenter();
    MovieAdapter getAdapter();
}
