package com.ltroya.movieindex.moviedetail.di;

import android.app.Activity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.ltroya.movieindex.api.MovieClient;
import com.ltroya.movieindex.api.MovieService;
import com.ltroya.movieindex.db.DBInteractor;
import com.ltroya.movieindex.db.DBRepository;
import com.ltroya.movieindex.libs.GlideImageLoader;
import com.ltroya.movieindex.libs.base.EventBus;
import com.ltroya.movieindex.libs.base.ImageLoader;
import com.ltroya.movieindex.moviedetail.GetMovieInteractor;
import com.ltroya.movieindex.moviedetail.GetMovieInteractorImpl;
import com.ltroya.movieindex.moviedetail.MovieDetailPresenter;
import com.ltroya.movieindex.moviedetail.MovieDetailPresenterImpl;
import com.ltroya.movieindex.moviedetail.MovieDetailRepository;
import com.ltroya.movieindex.moviedetail.MovieDetailRepositoryImpl;
import com.ltroya.movieindex.moviedetail.ui.MovieDetailView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class MovieDetailModule {
    MovieDetailView view;
    Activity activity;

    public MovieDetailModule(MovieDetailView view, Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    @Provides
    @Singleton
    ImageLoader providesImageLoader(RequestManager requestManager) {
        return new GlideImageLoader(requestManager);
    }

    @Provides
    @Singleton
    RequestManager providesFragmentRequestManager(Activity activity) {
        return Glide.with(activity);
    }

    @Provides
    @Singleton
    Activity providesActivity() {
        return this.activity;
    }

    @Provides
    @Singleton
    MovieDetailView providesMovieDetailView() {
        return this.view;
    }

    @Provides
    @Singleton
    MovieDetailPresenter providesMovieDetailPresenter(MovieDetailView view, GetMovieInteractor getMovieInteractor, EventBus eventBus, DBInteractor dbInteractor) {
        return new MovieDetailPresenterImpl(view, getMovieInteractor, eventBus, dbInteractor);
    }

    @Provides
    @Singleton
    GetMovieInteractor providesGetMovieInteractor(MovieDetailRepository repository) {
        return new GetMovieInteractorImpl(repository);
    }

    @Provides
    @Singleton
    MovieDetailRepository providesMovieDetailRepository(MovieService service, EventBus eventBus, DBRepository repository) {
        return new MovieDetailRepositoryImpl(service, eventBus, repository);
    }

    @Provides
    @Singleton
    MovieService providesMovieService() {
        return new MovieClient().getMovieService();
    }
}
