package com.ltroya.movieindex.movielist.ui;


import android.app.Activity;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ltroya.movieindex.DrawerActivity;
import com.ltroya.movieindex.MovieIndexApp;
import com.ltroya.movieindex.R;
import com.ltroya.movieindex.entities.Movie;
import com.ltroya.movieindex.moviedetail.ui.MovieDetailActivity;
import com.ltroya.movieindex.movielist.MovieListPresenter;
import com.ltroya.movieindex.movielist.di.MovieListComponent;
import com.ltroya.movieindex.movielist.ui.adapters.MovieAdapter;
import com.ltroya.movieindex.movielist.ui.adapters.OnItemClickListener;
import com.ltroya.movieindex.util.ItemOffsetDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieListFragment extends Fragment implements MovieListView, OnItemClickListener {
    private final String TAG = getClass().getSimpleName();
    private final int RC_DETAIL_MOVIE = 10;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.main_container)
    RelativeLayout mainContainer;

    private MovieListPresenter presenter;
    private MovieAdapter adapter;
    private MovieListComponent component;


    private boolean loading = false;

    int pastVisiblesItems,
            visibleItemCount,
            totalItemCount;

    int moviePage = 0;

    GridLayoutManager gridLayoutManager;
    private List<Movie> movies;


    public MovieListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        ButterKnife.bind(this, view);
        LogArguments();
        setupInjection();
        setupRecyclerView();
        presenter.getMovies(++moviePage);
        return view;
    }

    private void LogArguments() {
        if (getArguments() != null) {
            Log.d(TAG, "OnCreateView: " + getArguments().get(DrawerActivity.EXTRA_MOVIE_ID));
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == RC_DETAIL_MOVIE) {
            // Update the keep movie's icon on the list if it is necessary
            Bundle extras = data.getExtras();
            String movieId = extras.getString(DrawerActivity.EXTRA_MOVIE_ID);
            boolean keepMovie = extras.getBoolean(DrawerActivity.EXTRA_MOVIE_KEEP);
            adapter.updateMovie(movieId, keepMovie);
        }
    }

    private void setupRecyclerView() {
        ItemOffsetDecoration itemOffsetDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemOffsetDecoration);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //check for scroll down
                if (dy > 0)
                {
                    visibleItemCount = gridLayoutManager.getChildCount();
                    totalItemCount = gridLayoutManager.getItemCount();
                    pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            presenter.getMovies(++moviePage);
                        }
                    }
                }
            }
        });
    }

    private void setupInjection() {
        MovieIndexApp app = (MovieIndexApp) getActivity().getApplication();
        component = app.getMovieListComponent(this, this, this);
        presenter = getPresenter();
        adapter = getAdapter();
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
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void noInfo() {
    }

    @Override
    public void onError(String error) {
        Snackbar.make(mainContainer, error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setMovies(List<Movie> movies) {
        adapter.setItems(movies);
        loading = true;
    }

    @Override
    public void removeMovie(Movie movie) {}

    @Override
    public void saveMovie(Movie movie) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(Movie movie) {
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, movie.getId());
        intent.putExtra(DrawerActivity.EXTRA_FRAGMENT_TO_LAUNCH, R.id.nav_movies);
        // StartActivityForResult is needed here in order to save the current
        // state in the recyclerView
        startActivityForResult(intent, RC_DETAIL_MOVIE);
    }

    @Override
    public void onSaveButton(Movie movie) {
        if (movie.isKeep()) {
            updateAdapterBeforeDelete(movie);
        }
        presenter.keepMovie(movie);
    }

    /**
     * Updates adapter before delete movie from realm, otherwise it will
     * launch an exception before the object no longer exists.
     * <p>
     * It like a hack, but realm handle object that way
     *
     * @param movie that will be deleted
     */
    private void updateAdapterBeforeDelete(Movie movie) {
        movie.setKeep(false);
        adapter.notifyDataSetChanged();
    }

    public MovieListPresenter getPresenter() {
        return component.getPresenter();
    }

    public MovieAdapter getAdapter() {
        return component.getAdapter();
    }
}
