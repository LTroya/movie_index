package com.ltroya.movieindex.movielist;

import com.ltroya.movieindex.BaseTest;
import com.ltroya.movieindex.BuildConfig;
import com.ltroya.movieindex.api.MovieService;
import com.ltroya.movieindex.api.PopularMovieResponse;
import com.ltroya.movieindex.db.DBRepository;
import com.ltroya.movieindex.entities.Movie;
import com.ltroya.movieindex.libs.base.EventBus;
import com.ltroya.movieindex.movielist.events.MovieListEvent;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MovieListRepositoryImplTest extends BaseTest {
    @Mock private EventBus eventBus;
    @Mock private MovieService service;
    @Mock private DBRepository dbRepository;
    @Mock Movie movie;

    private MovieListRepository repository;
    private ArgumentCaptor<MovieListEvent> movieListEventArgumentCaptor;

    private final int API_PAGE = 1;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        repository = new MovieListRepositoryImpl(eventBus, service, dbRepository);
        movieListEventArgumentCaptor = ArgumentCaptor.forClass(MovieListEvent.class);
    }

    @Test
    public void testGetMoviesCalled_ApiServiceSuccessCall_EventPosted() throws Exception {
        when(service.getMovies(BuildConfig.THEMOVIEDB_API_KEY,
                API_PAGE)).thenReturn(buildCall(true, null));
        repository.getMovies(API_PAGE);

        verify(service).getMovies(BuildConfig.THEMOVIEDB_API_KEY, 1);
        verify(eventBus).post(movieListEventArgumentCaptor.capture());

        MovieListEvent event = movieListEventArgumentCaptor.getValue();
        assertNotNull(event.getMovieList());
        assertNull(event.getError());
    }


    @Test
    public void testGetMoviesCalled_ApiServiceFailureCall_EventPosted() throws Exception {
        String errorMsg = "error";
        when(service.getMovies(BuildConfig.THEMOVIEDB_API_KEY,
                API_PAGE)).thenReturn(buildCall(false, errorMsg));
        repository.getMovies(API_PAGE);

        verify(service).getMovies(BuildConfig.THEMOVIEDB_API_KEY, 1);
        verify(eventBus).post(movieListEventArgumentCaptor.capture());

        MovieListEvent event = movieListEventArgumentCaptor.getValue();
        assertNull(event.getMovieList());
        System.out.print(event.getError());
        assertNotNull(event.getError());
        assertEquals(errorMsg, event.getError());
    }

    private Call<PopularMovieResponse> buildCall(final boolean success, final String errorMsg) {
        Call<PopularMovieResponse> response = new Call<PopularMovieResponse>() {
            @Override
            public Response<PopularMovieResponse> execute() throws IOException {
                Response<PopularMovieResponse> result = null;
                if (success) {
                    PopularMovieResponse response = new PopularMovieResponse();
                    response.setPage(1);
                    response.setResults(Collections.singletonList(movie));
                    response.setTotalResults(1);
                    response.setTotalPages(1);

                    result = Response.success(response);
                } else {
                    result = Response.error(null, null);
                }
                return result;
            }

            @Override
            public void enqueue(Callback<PopularMovieResponse> callback) {
                if (success) {
                    try {
                        callback.onResponse(this, execute());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    callback.onFailure(this, new Throwable(errorMsg));
                }
            }

            @Override
            public boolean isExecuted() {
                return true;
            }

            @Override
            public void cancel() {

            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<PopularMovieResponse> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }
        };
        return response;
    }
}
