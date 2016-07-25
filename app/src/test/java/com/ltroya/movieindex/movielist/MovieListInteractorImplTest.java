package com.ltroya.movieindex.movielist;

import com.ltroya.movieindex.BaseTest;

import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;

public class MovieListInteractorImplTest extends BaseTest{
    @Mock MovieListRepository repository;
    private MovieListInteractor interactor;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        interactor = new MovieListInteractorImpl(repository);
    }

    @Test
    public void testExecute_callRepository() throws Exception {
        interactor.execute(1);
        verify(repository).getMovies(1);
    }
}
