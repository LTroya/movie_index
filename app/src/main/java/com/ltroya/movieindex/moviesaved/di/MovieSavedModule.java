package com.ltroya.movieindex.moviesaved.di;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.ltroya.movieindex.api.MovieClient;
import com.ltroya.movieindex.api.MovieService;
import com.ltroya.movieindex.db.DBInteractor;
import com.ltroya.movieindex.db.DBInteractorImpl;
import com.ltroya.movieindex.db.DBRepository;
import com.ltroya.movieindex.db.DBRepositoryImpl;
import com.ltroya.movieindex.entities.Movie;
import com.ltroya.movieindex.libs.GlideImageLoader;
import com.ltroya.movieindex.libs.base.EventBus;
import com.ltroya.movieindex.libs.base.ImageLoader;
import com.ltroya.movieindex.movielist.MovieListInteractor;
import com.ltroya.movieindex.movielist.MovieListInteractorImpl;
import com.ltroya.movieindex.movielist.MovieListPresenter;
import com.ltroya.movieindex.movielist.MovieListPresenterImpl;
import com.ltroya.movieindex.movielist.MovieListRepository;
import com.ltroya.movieindex.movielist.MovieListRepositoryImpl;
import com.ltroya.movieindex.movielist.ui.MovieListView;
import com.ltroya.movieindex.movielist.ui.adapters.MovieAdapter;
import com.ltroya.movieindex.movielist.ui.adapters.OnItemClickListener;
import com.ltroya.movieindex.moviesaved.MovieSavedPresenterImpl;
import com.ltroya.movieindex.moviesaved.ui.adapter.RealmMovieAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;

@Module
public class MovieSavedModule {
    private MovieListView view;
    private Fragment fragment;
    private OnItemClickListener listener;

    public MovieSavedModule(MovieListView view, Fragment fragment, OnItemClickListener listener) {
        this.view = view;
        this.fragment = fragment;
        this.listener = listener;
    }

    @Provides
    @Singleton
    ImageLoader providesImageLoader(RequestManager requestManager) {
        return new GlideImageLoader(requestManager);
    }

    @Provides
    @Singleton
    RequestManager providesFragmentRequestManager(Fragment fragment) {
        return Glide.with(fragment);
    }

    @Provides
    @Singleton
    Fragment providesFragment() {
        return this.fragment;
    }

    @Provides
    @Singleton
    MovieListView providesMovieListView() {
        return this.view;
    }

    @Provides
    @Singleton
    MovieListPresenter providesMovieSavedPresenter(MovieListView view, DBInteractor interactor, EventBus eventBus) {
        return new MovieSavedPresenterImpl(view, interactor, eventBus);
    }

    @Provides
    @Singleton
    RealmMovieAdapter providesRealmMovieAdapter(Context context, @Nullable OrderedRealmCollection<Movie> data, OnItemClickListener listener, ImageLoader imageLoader) {
        return new RealmMovieAdapter(context, data, listener, imageLoader);
    }

    @Provides
    @Singleton
    OnItemClickListener providesMovieOnClickListener() {
        return this.listener;
    }

    @Provides
    @Singleton
    OrderedRealmCollection<Movie> providesRealmMovieList() {
        return Realm.getDefaultInstance().where(Movie.class).findAll();
    }

    @Provides
    @Singleton
    Context providesContext() {
        return fragment.getActivity();
    }
}
