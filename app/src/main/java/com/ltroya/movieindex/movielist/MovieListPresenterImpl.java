package com.ltroya.movieindex.movielist;

import android.util.Log;

import com.ltroya.movieindex.db.DBInteractor;
import com.ltroya.movieindex.db.events.DBEvent;
import com.ltroya.movieindex.entities.Movie;
import com.ltroya.movieindex.libs.base.EventBus;
import com.ltroya.movieindex.movielist.events.MovieListEvent;
import com.ltroya.movieindex.movielist.ui.MovieListView;

import org.greenrobot.eventbus.Subscribe;

import io.realm.RealmResults;

public class MovieListPresenterImpl implements MovieListPresenter {
    private final String TAG = getClass().getSimpleName();
    private MovieListView view;
    private EventBus eventBus;
    private MovieListInteractor interactor;
    private DBInteractor dbInteractor;

    public MovieListPresenterImpl(MovieListView view, EventBus eventBus, MovieListInteractor interactor, DBInteractor dbInteractor) {
        this.view = view;
        this.eventBus = eventBus;
        this.interactor = interactor;
        this.dbInteractor = dbInteractor;
    }

    @Override
    public void onResume() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void onPause() {
        eventBus.unregister(this);
    }

    @Override
    public void getMovies(int page) {
        if (this.view != null) {
            view.showProgress();
            view.hideUIElements();
        }
        interactor.execute(page);
    }

    @Override
    public void keepMovie(Movie movie) {
        dbInteractor.execute(movie);
    }

    @Override
    @Subscribe
    public void onEventMainThread(DBEvent event) {
        String errorMsg = event.getError();
        if (this.view != null) {
            view.hideProgress();
            view.showUIElements();
            if (errorMsg != null) {
                view.onError(errorMsg);
            } else {
                switch (event.getType()) {
                    case DBEvent.SAVE_EVENT:
                        view.saveMovie(event.getMovie());
                        break;
                    case DBEvent.DELETE_EVENT:
                        view.removeMovie(event.getMovie());
                        break;
                    case DBEvent.GET_EVENT:
                        RealmResults<Movie> movies = event.getMovieList();
                        if (movies.size() == 0) {
                            view.noInfo();
                        } else {
                            view.setMovies(movies);
                        }
                        break;
                }
            }
        }
    }


    @Override
    @Subscribe
    public void onEventMainThread(MovieListEvent event) {
        String errorMsg = event.getError();
        if (this.view != null) {
            view.hideProgress();
            view.showUIElements();
            if (errorMsg != null) {
                view.onError(errorMsg);
            } else {
                view.setMovies(event.getMovieList());
            }
        }
    }

    @Override
    public MovieListView getView() {
        return this.view;
    }
}
