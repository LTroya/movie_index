package com.ltroya.movieindex.db.di;

import android.app.Activity;

import com.ltroya.movieindex.db.DBInteractor;
import com.ltroya.movieindex.db.DBInteractorImpl;
import com.ltroya.movieindex.db.DBRepository;
import com.ltroya.movieindex.db.DBRepositoryImpl;
import com.ltroya.movieindex.libs.base.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class DBModule {
    @Provides
    @Singleton
    Realm providesRealm() {
        return Realm.getDefaultInstance();
    }

    @Provides
    @Singleton
    DBInteractor providesDBInteractor(DBRepository repository) {
        return new DBInteractorImpl(repository);
    }

    @Provides
    @Singleton
    DBRepository providesDBRepository(Realm realm, EventBus eventBus) {
        return new DBRepositoryImpl(realm, eventBus);
    }
}
