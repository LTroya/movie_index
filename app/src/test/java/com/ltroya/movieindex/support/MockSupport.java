package com.ltroya.movieindex.support;

import com.ltroya.movieindex.entities.Movie;

import org.powermock.api.mockito.PowerMockito;

import io.realm.Realm;

import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

public class MockSupport {
    public static Realm mockRealm() {
        mockStatic(Realm.class);

        Realm mockRealm = PowerMockito.mock(Realm.class);

        when(mockRealm.createObject(Movie.class)).thenReturn(new Movie());

        when(Realm.getDefaultInstance()).thenReturn(mockRealm);

        return mockRealm;
    }
}
