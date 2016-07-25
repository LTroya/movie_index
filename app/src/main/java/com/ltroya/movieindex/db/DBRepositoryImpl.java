package com.ltroya.movieindex.db;

import android.util.Log;

import com.ltroya.movieindex.db.events.DBEvent;
import com.ltroya.movieindex.entities.Movie;
import com.ltroya.movieindex.libs.base.EventBus;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class DBRepositoryImpl implements DBRepository {
    private final String TAG = getClass().getSimpleName();

    private Realm realm;
    private EventBus eventBus;

    public DBRepositoryImpl(Realm realm, EventBus eventBus) {
        this.realm = realm;
        this.eventBus = eventBus;
    }

    @Override
    public void handleMovie(Movie m) {
        final Movie movie = getMovie(m.getId());
        if (movie != null)
            removeMovie(movie);
        else
            saveMovie(m);
    }

    @Override
    public void getMovies() {
        RealmResults<Movie> movies = realm.where(Movie.class).findAll();
        post(DBEvent.GET_EVENT, movies);
    }

    @Override
    public boolean exists(Movie movie) {
        return getMovie(movie.getId()) != null;
    }

    @Override
    public List<Movie> exists(List<Movie> movieList) {
        for (Movie movie : movieList) {
            boolean exists = exists(movie);
            movie.setKeep(exists);
        }
        return movieList;
    }

    public void saveMovie(final Movie movie) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(movie);
            }
        });

        movie.setKeep(true);
        post(DBEvent.SAVE_EVENT, movie);
    }

    private Movie getMovie(String movieId) {
        return realm.where(Movie.class)
                .equalTo("id", movieId)
                .findFirst();
    }

    public void removeMovie(final Movie movie) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                movie.deleteFromRealm();
            }
        });

        // Post event with the movie to be able to remove from adapter
        post(DBEvent.DELETE_EVENT, movie);
    }

    private void post(int type, String error, Movie movie, RealmResults<Movie> movies) {
        DBEvent event = new DBEvent();
        event.setType(type);
        event.setError(error);
        event.setMovie(movie);
        event.setMovieList(movies);
        eventBus.post(event);
    }

    private void post(int type, RealmResults<Movie> movies) {
        post(type, null, null, movies);
    }

    private void post(int type, String error) {
        post(type, error, null, null);
    }

    private void post(int type, Movie movie) {
        post(type, null, movie, null);
    }
}
