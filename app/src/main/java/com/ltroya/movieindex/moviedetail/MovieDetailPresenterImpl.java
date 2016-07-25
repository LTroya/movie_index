package com.ltroya.movieindex.moviedetail;

import com.ltroya.movieindex.R;
import com.ltroya.movieindex.db.DBInteractor;
import com.ltroya.movieindex.db.events.DBEvent;
import com.ltroya.movieindex.entities.Movie;
import com.ltroya.movieindex.libs.base.EventBus;
import com.ltroya.movieindex.moviedetail.events.MovieDetailEvent;
import com.ltroya.movieindex.moviedetail.ui.MovieDetailView;

import org.greenrobot.eventbus.Subscribe;

public class MovieDetailPresenterImpl implements MovieDetailPresenter {
    private final String TAG = getClass().getSimpleName();

    private MovieDetailView view;
    private EventBus eventBus;
    private GetMovieInteractor getMovieInteractor;
    private DBInteractor dbInteractor;

    public MovieDetailPresenterImpl(MovieDetailView view, GetMovieInteractor getMovieInteractor, EventBus eventBus, DBInteractor dbInteractor) {
        this.view = view;
        this.getMovieInteractor = getMovieInteractor;
        this.eventBus = eventBus;
        this.dbInteractor = dbInteractor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        view = null;
        eventBus.unregister(this);
    }

    @Override
    public void getMovie(String movieId) {
        if (view != null) {
            view.showProgress();
            view.hideContentElements();
        }
        getMovieInteractor.execute(movieId);
    }

    @Override
    @Subscribe
    public void onEventMainThread(MovieDetailEvent event) {
        String errorMsg = event.getError();
        if (view != null) {
            if (errorMsg != null) {
                view.onError(errorMsg);
            } else {
                switch (event.getType()) {
                    case MovieDetailEvent.GET_EVENT:
                        view.setMovie(event.getMovie());
                        view.hideProgress();
                        view.showContentElements();
                        break;
                }
            }
        }
    }

    @Override
    @Subscribe
    public void onEventMainThread(DBEvent event) {
        String errorMsg = event.getError();
        if (view != null) {
            if (errorMsg != null) {
                view.onError(errorMsg);
            } else {
                switch (event.getType()) {
                    case DBEvent.SAVE_EVENT:
                        view.showMessage(R.string.movie_detail_save_in_db);
                        view.saveMovie();
                        break;
                    case DBEvent.DELETE_EVENT:
                        view.showMessage(R.string.movie_detail_remove_from_db);
                        view.removeMovie();
                        break;
                }
            }
        }
    }

    @Override
    public void keepMovie(Movie movie) {
        dbInteractor.execute(movie);
    }

    @Override
    public MovieDetailView getView() {
        return view;
    }
}
