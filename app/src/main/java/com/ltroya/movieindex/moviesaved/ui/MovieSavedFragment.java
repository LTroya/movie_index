package com.ltroya.movieindex.moviesaved.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ltroya.movieindex.DrawerActivity;
import com.ltroya.movieindex.MovieIndexApp;
import com.ltroya.movieindex.R;
import com.ltroya.movieindex.entities.Movie;
import com.ltroya.movieindex.moviedetail.ui.MovieDetailActivity;
import com.ltroya.movieindex.movielist.MovieListPresenter;
import com.ltroya.movieindex.movielist.ui.MovieListView;
import com.ltroya.movieindex.movielist.ui.adapters.OnItemClickListener;
import com.ltroya.movieindex.moviesaved.di.MovieSavedComponent;
import com.ltroya.movieindex.moviesaved.ui.adapter.RealmMovieAdapter;
import com.ltroya.movieindex.util.ItemOffsetDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MovieSavedFragment extends Fragment implements MovieListView, OnItemClickListener{
    private final String TAG = getClass().getSimpleName();

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.container_no_info)
    LinearLayout containerNoInfo;
    @Bind(R.id.main_container)
    RelativeLayout mainContainer;

    private MovieIndexApp app;
    private MovieListPresenter presenter;
    private MovieSavedComponent component;
    private RealmMovieAdapter adapter;
    private boolean wasStopped = false;

    public MovieSavedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_saved, container, false);
        ButterKnife.bind(this, view);
        setupInjection();
        setupRecyclerView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
        presenter.getMovies(1);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        presenter.onDestroy();
    }

    private void setupInjection() {
        app = (MovieIndexApp) getActivity().getApplication();
        component = app.getMovieSavedComponent(this, this, this);
        presenter = getPresenter();
        adapter = getAdapter();
    }

    private RealmMovieAdapter getAdapter() {
        return component.getAdapter();
    }

    private MovieListPresenter getPresenter() {
        return component.getPresenter();
    }

    private void setupRecyclerView() {
        ItemOffsetDecoration itemOffsetDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemOffsetDecoration);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showUIElements() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideUIElements() {
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void noInfo() {
        containerNoInfo.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(String error) {
        Snackbar.make(mainContainer, error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setMovies(List<Movie> movies) {
//        adapter.setItems(movies);
    }

    @Override
    public void removeMovie(Movie movie) {}

    @Override
    public void saveMovie(Movie movie) {}

    @Override
    public void onItemClick(Movie movie) {
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, movie.getId());
        intent.putExtra(DrawerActivity.EXTRA_FRAGMENT_TO_LAUNCH, R.id.nav_watch_later);
        startActivity(intent);
    }

    @Override
    public void onSaveButton(Movie movie) {
        presenter.keepMovie(movie);
    }
}
