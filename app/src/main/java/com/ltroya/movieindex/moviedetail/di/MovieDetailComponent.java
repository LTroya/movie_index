package com.ltroya.movieindex.moviedetail.di;

import com.ltroya.movieindex.db.di.DBModule;
import com.ltroya.movieindex.libs.base.ImageLoader;
import com.ltroya.movieindex.libs.di.LibsModule;
import com.ltroya.movieindex.moviedetail.MovieDetailPresenter;
import com.ltroya.movieindex.movielist.MovieListPresenter;
import com.ltroya.movieindex.movielist.ui.adapters.MovieAdapter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MovieDetailModule.class, LibsModule.class, DBModule.class})
public interface MovieDetailComponent {
    MovieDetailPresenter getPresenter();
    ImageLoader getImageLoader();
}
