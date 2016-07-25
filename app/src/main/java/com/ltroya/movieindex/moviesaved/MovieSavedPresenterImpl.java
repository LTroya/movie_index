package com.ltroya.movieindex.moviesaved;

import android.util.Log;

import com.ltroya.movieindex.db.DBInteractor;
import com.ltroya.movieindex.db.events.DBEvent;
import com.ltroya.movieindex.entities.Movie;
import com.ltroya.movieindex.libs.base.EventBus;
import com.ltroya.movieindex.movielist.MovieListPresenter;
import com.ltroya.movieindex.movielist.events.MovieListEvent;
import com.ltroya.movieindex.movielist.ui.MovieListView;

import org.greenrobot.eventbus.Subscribe;

import io.realm.RealmResults;

public class MovieSavedPresenterImpl implements MovieListPresenter {
    private MovieListView view;
    private DBInteractor dbInteractor;
    private EventBus eventBus;

    public MovieSavedPresenterImpl(MovieListView view, DBInteractor dbInteractor, EventBus eventBus) {
        this.view = view;
        this.dbInteractor = dbInteractor;
        this.eventBus = eventBus;
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
        dbInteractor.getMovies();
    }

    @Override
    public void keepMovie(Movie movie) {
        dbInteractor.execute(movie);
    }

    @Override
    public void onEventMainThread(MovieListEvent event) {
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
                        // TODO: escuchar este evento para cuando este agregado
                        // la funcionabilidad de agregar a ver mas tarde
                        // una pelicula
                        break;
                    case DBEvent.DELETE_EVENT:
                        Log.d("Will", "The movie was deleted");
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
    public MovieListView getView() {
        return view;
    }
}
