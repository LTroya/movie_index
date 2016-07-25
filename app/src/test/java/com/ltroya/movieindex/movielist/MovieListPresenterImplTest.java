package com.ltroya.movieindex.movielist;


import android.content.Context;

import com.ltroya.movieindex.BaseTest;
import com.ltroya.movieindex.BuildConfig;
import com.ltroya.movieindex.db.DBInteractor;
import com.ltroya.movieindex.db.events.DBEvent;
import com.ltroya.movieindex.entities.Movie;
import com.ltroya.movieindex.libs.base.EventBus;
import com.ltroya.movieindex.movielist.events.MovieListEvent;
import com.ltroya.movieindex.movielist.ui.MovieListView;
import com.ltroya.movieindex.support.MockSupport;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.internal.RealmCore;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

//@RunWith(PowerMockRunner.class)
//@Config(constants = BuildConfig.class, sdk = 23)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@PrepareForTest({Realm.class, RealmConfiguration.class, RealmQuery.class, RealmResults.class, RealmCore.class})
public class MovieListPresenterImplTest extends BaseTest {
    @Mock
    private MovieListView view;
    @Mock
    private EventBus eventBus;
    @Mock
    private MovieListInteractor interactor;
    @Mock
    private DBInteractor dbInteractor;
    @Mock
    private Movie movie;
    @Mock
    private MovieListEvent event;
    @Mock
    private DBEvent dbEvent;

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    Realm mockRealm;
    private RealmResults<Movie> movies;
    private MovieListPresenter presenter;

//    @Override
//    public void setUp() throws Exception {
//        super.setUp();
//        // Setup Realm to be mocked
//        mockStatic(Realm.class);
//        mockStatic(RealmConfiguration.class);
//        mockStatic(RealmCore.class);
//
//        // Create the mock
//        final Realm mockRealm = mock(Realm.class);
//        final RealmConfiguration mockRealmConfig = mock(RealmConfiguration.class);
//
//        // TODO: Better solution would be just mock the RealmConfiguration.Builder class. But it seems there is some
//        // problems for powermock to mock it (static inner class). We just mock the RealmCore.loadLibrary(Context) which
//        // will be called by RealmConfiguration.Builder's constructor.
//        doNothing().when(RealmCore.class);
//        RealmCore.loadLibrary(any(Context.class));
//
//        // TODO: Mock the RealmConfiguration's constructor. If the RealmConfiguration.Builder.build can be mocked, this
//        // is not necessary anymore.
//        whenNew(RealmConfiguration.class).withAnyArguments().thenReturn(mockRealmConfig);
//
//        // Anytime getInstance is called with any configuration, then return the mockRealm
//        when(Realm.getInstance(any(RealmConfiguration.class))).thenReturn(mockRealm);
//
//        // Anytime we ask Realm to create a Person, return a new instance.
//        when(mockRealm.createObject(Movie.class)).thenReturn(new Movie());
//
//        // Set up some naive stubs
//        Movie m1 = new Movie();
//        m1.setId("1");
//        m1.setTitle("John Young");
//
//        Movie m2 = new Movie();
//        m2.setId("2");
//        m2.setTitle("John Senior");
//
//        Movie m3 = new Movie();
//        m3.setId("3");
//        m3.setTitle("Jane");
//
//        Movie m4 = new Movie();
//        m4.setId("4");
//        m4.setTitle("Robert");
//
//        List<Movie> movieList = Arrays.asList(m1, m2, m3, m4);
//
//        // Create a mock RealmQuery
//        RealmQuery<Movie> movieQuery = mockRealmQuery();
//
//        // When the RealmQuery performs findFirst, return the first record in the list.
//        when(movieQuery.findFirst()).thenReturn(movieList.get(0));
//
//        // When the where clause is called on the Realm, return the mock query.
//        when(mockRealm.where(Movie.class)).thenReturn(movieQuery);
//
//        // Create a mock RealmResults
//        RealmResults<Movie> movies = mockRealmResults();
//
//        // When we ask Realm for all of the Person instances, return the mock RealmResults
//        when(mockRealm.where(Movie.class).findAll()).thenReturn(movies);
//
//        // When we ask the RealmQuery for all of the Person objects, return the mock RealmResults
//        when(movieQuery.findAll()).thenReturn(movies);
//
//        // RealmResults is final, must mock static and also place this in the PrepareForTest annotation array.
//        mockStatic(RealmResults.class);
//
//        // The for(...) loop in Java needs an iterator, so we're giving it one that has items,
//        // since the mock RealmResults does not provide an implementation. Therefore, anytime
//        // anyone asks for the RealmResults Iterator, give them a functioning iterator from the
//        // ArrayList of Persons we created above. This will allow the loop to execute.
//        when(movies.iterator()).thenReturn(movieList.iterator());
//
//        // Return the size of the mock list.
//        when(movies.size()).thenReturn(movieList.size());
//
//        this.mockRealm = mockRealm;
//        this.movies = movies;
//        presenter = new MovieListPresenterImpl(view, eventBus, interactor, dbInteractor);
//    }


    @Override
    public void setUp() throws Exception {
        super.setUp();

        mockRealm = MockSupport.mockRealm();
        presenter = new MovieListPresenterImpl(view, eventBus, interactor, dbInteractor);
    }

    @Test
    public void testRealm_GetDefaultInstance() throws Exception {
        assertThat(Realm.getDefaultInstance(), is(mockRealm));
    }

    @Test
    public void testOnResume_SubscribeToEventBus() throws Exception {
        presenter.onResume();
        verify(eventBus).register(presenter);
    }

    @Test
    public void testOnPause_UnsubscribeToEventBus() throws Exception {
        presenter.onPause();
        verify(eventBus).unregister(presenter);
    }

    @Test
    public void testOnDestroy_ViewShouldBeNull() throws Exception {
        presenter.onDestroy();
        assertNull(presenter.getView());
    }

    @Test
    public void testKeepMovie_ShouldCallDBInteractor() throws Exception {
        presenter.keepMovie(movie);
        verify(dbInteractor).execute(movie);
    }

    @Test
    public void testGetMovies_GetMoviesFromDB() throws Exception {
        presenter.getMovies(1);
        assertNotNull(view);
        verify(view).showProgress();
        verify(view).hideUIElements();
        verify(interactor).execute(1);
    }

    @Test
    public void testEventMainThread_MovieListEvent_SetMoviesOnView() throws Exception {
        when(event.getMovieList()).thenReturn(Arrays.asList(movie));
        presenter.onEventMainThread(event);

        assertNotNull(view);
        verify(view).hideProgress();
        verify(view).showUIElements();
        verify(view).setMovies(event.getMovieList());
    }

    @Test
    public void testEventMainThread_MovieListEvent_ShowErrorOnView() throws Exception {
        String errorMsg = "error";
        when(event.getError()).thenReturn(errorMsg);
        presenter.onEventMainThread(event);

        assertNotNull(view);
        verify(view).hideProgress();
        verify(view).showUIElements();
        verify(view).onError(errorMsg);
    }

    @Test
    public void testOnEventMainThread_DBEvent_GetSaveMessageOnView() throws Exception {
        when(dbEvent.getMovie()).thenReturn(movie);
        when(dbEvent.getType()).thenReturn(DBEvent.SAVE_EVENT);
        presenter.onEventMainThread(dbEvent);

        assertNotNull(view);
        verify(view).hideProgress();
        verify(view).showUIElements();
        verify(view).saveMovie(dbEvent.getMovie());
    }

    @Test
    public void testOnEventMainThread_DBEvent_GetDeleteMessageOnView() throws Exception {
        when(dbEvent.getMovie()).thenReturn(movie);
        when(dbEvent.getType()).thenReturn(DBEvent.DELETE_EVENT);
        presenter.onEventMainThread(dbEvent);

        assertNotNull(view);
        verify(view).hideProgress();
        verify(view).showUIElements();
        verify(view).removeMovie(dbEvent.getMovie());
    }

    @Test
    public void testOnEventMainThread_DBEvent_GetMovies() throws Exception {
        RealmResults<Movie> movieList = mockRealmResults();

        when(mockRealm.where(Movie.class).findAll()).thenReturn(movieList);

        when(dbEvent.getMovie()).thenReturn(movie);
        when(dbEvent.getMovieList()).thenReturn(movies);

        when(movies.size()).thenReturn(1);

        when(dbEvent.getType()).thenReturn(DBEvent.GET_EVENT);
        presenter.onEventMainThread(dbEvent);

        assertNotNull(view);
        verify(view).hideProgress();
        verify(view).showUIElements();
        verify(view).setMovies(movieList);
    }

    @Test
    public void testOnEventMainThread_DBEvent_GetNoMovies() throws Exception {
        when(dbEvent.getMovie()).thenReturn(movie);
        when(dbEvent.getType()).thenReturn(DBEvent.SAVE_EVENT);
        presenter.onEventMainThread(dbEvent);

        assertNotNull(view);
        verify(view).hideProgress();
        verify(view).showUIElements();
        verify(view).saveMovie(dbEvent.getMovie());
    }

    @Test
    public void testOnEventMainThread_GetErrorOnView() throws Exception {
        String errorMsg = "error";
        when(dbEvent.getError()).thenReturn(errorMsg);
        presenter.onEventMainThread(dbEvent);

        assertNotNull(view);
        verify(view).hideProgress();
        verify(view).showUIElements();
        verify(view).onError(dbEvent.getError());
    }

    @Test
    public void testGetView_ReturnsView() throws Exception {
        assertEquals(view, presenter.getView());
    }

    @SuppressWarnings("unchecked")
    private <T extends RealmObject> RealmResults<T> mockRealmResults() {
        return mock(RealmResults.class);
    }

    @SuppressWarnings("unchecked")
    private <T extends RealmObject> RealmQuery<T> mockRealmQuery() {
        return mock(RealmQuery.class);
    }
}
