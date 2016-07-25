package com.ltroya.movieindex.movielist.di;

import android.support.v4.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.ltroya.movieindex.api.MovieClient;
import com.ltroya.movieindex.api.MovieService;
import com.ltroya.movieindex.db.DBInteractor;
import com.ltroya.movieindex.db.DBRepository;
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MovieListModule {
    private MovieListView view;
    private OnItemClickListener listener;
    private Fragment fragment;

    public MovieListModule(MovieListView view, OnItemClickListener listener, Fragment fragment) {
        this.view = view;
        this.listener = listener;
        this.fragment = fragment;
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

    @Provides @Singleton
    MovieListView providesMovieListView() {
        return this.view;
    }

    @Provides @Singleton
    MovieListPresenter providesMovieListPresenter(MovieListView view, EventBus eventBus, MovieListInteractor interactor, DBInteractor dbInteractor) {
        return new MovieListPresenterImpl(view, eventBus, interactor, dbInteractor);
    }

    @Provides @Singleton
    MovieListInteractor providesMovieListInteractor(MovieListRepository repository) {
        return new MovieListInteractorImpl(repository);
    }

    @Provides @Singleton
    MovieListRepository providesMovieListRepository(EventBus eventBus, MovieService service, DBRepository repository) {
        return new MovieListRepositoryImpl(eventBus, service, repository);
    }

    @Provides @Singleton
    MovieService providesMovieService() {
        return new MovieClient().getMovieService();
    }

    @Provides @Singleton
    MovieAdapter providesMovieAdapter(List<Movie> dataset, OnItemClickListener listener, ImageLoader imageLoader) {
        return new MovieAdapter(dataset, listener, imageLoader);
    }

    @Provides @Singleton
    OnItemClickListener providesMovieOnClickListener () {
        return this.listener;
    }

    @Provides @Singleton
    List<Movie> providesMovieEmptyList() {
        return new ArrayList<Movie>();
    }
}
