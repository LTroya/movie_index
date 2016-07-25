package com.ltroya.movieindex.moviedetail.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ltroya.movieindex.DrawerActivity;
import com.ltroya.movieindex.MovieIndexApp;
import com.ltroya.movieindex.R;
import com.ltroya.movieindex.entities.Genre;
import com.ltroya.movieindex.entities.Movie;
import com.ltroya.movieindex.libs.base.ImageLoader;
import com.ltroya.movieindex.moviedetail.MovieDetailPresenter;
import com.ltroya.movieindex.moviedetail.di.MovieDetailComponent;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailView {
    public final String TAG = getClass().getSimpleName();
    public static final String EXTRA_MOVIE_ID = "id";

    private MovieDetailPresenter presenter;
    private ImageLoader imageLoader;
    private MovieDetailComponent component;
    private Movie movie;

    @Bind(R.id.img_movie_poster)
    ImageView imgMoviePoster;

    @Bind(R.id.txt_movie_title)
    TextView txtMovieTitle;

    @Bind(R.id.btn_save_movie)
    ImageButton btnSaveMovie;

    @Bind(R.id.txt_movie_overview)
    TextView txtMovieOverview;

    @Bind(R.id.txt_popularity)
    TextView txtPopularity;

    @Bind(R.id.txt_vote_average)
    TextView txtVoteAverage;

    @Bind(R.id.txt_genre)
    TextView txtGenre;

    @Bind(R.id.txt_release_date)
    TextView txtReleaseDate;

    @Bind(R.id.container_movie_info)
    LinearLayout containerMovieInfo;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Bind(R.id.main_container)
    RelativeLayout mainContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        String movieId = (String) getIntent().getExtras().get(EXTRA_MOVIE_ID);
        setupInjection();
        presenter.onCreate();
        presenter.getMovie(movieId);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                Intent upIntent = NavUtils.getParentActivityIntent(this);
//                upIntent.putExtra(DrawerActivity.EXTRA_MOVIE_ID, movie.getId());
//                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
//                    // This activity is NOT part of this app's task, so create a new task
//                    // when navigating up, with a synthesized back stack.
//                    TaskStackBuilder.create(this)
//                            // Add all of this activity's parents to the back stack
//                            .addNextIntentWithParentStack(upIntent)
//                            // Navigate up to the closest parent
//                            .startActivities();
//                } else {
//                    // This activity is part of this app's task, so simply
//                    // navigate up to the logical parent activity.
//                    NavUtils.navigateUpTo(this, upIntent);
//                }
                backToMovieList();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupInjection() {
        MovieIndexApp app = (MovieIndexApp) getApplication();
        component = app.getMovieDetailComponent(this, this);
        imageLoader = getImageLoader();
        presenter = getPresenter();
    }

    @OnClick(R.id.btn_save_movie)
    public void keepMovie() {
        boolean keepMovie = movie.isKeep();
        movie.setKeep(!keepMovie);
        presenter.keepMovie(movie);
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
    public void showContentElements() {
        containerMovieInfo.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideContentElements() {
        containerMovieInfo.setVisibility(View.GONE);
    }

    @Override
    public void setMovie(Movie movie) {
        imageLoader.load(imgMoviePoster, movie.getPosterPath(), 1000, 1000);

        txtMovieTitle.setText(movie.getTitle());
        txtMovieOverview.setText(movie.getOverview());
        txtPopularity.setText(String.valueOf(movie.getPopularity()));
        txtVoteAverage.setText(String.valueOf(movie.getVoteAverage()));
        txtReleaseDate.setText(movie.getReleaseDate());

        String genreStr = TextUtils.join(", ", movie.getGenreList());
        txtGenre.setText(genreStr);

        btnSaveMovie.setImageResource(movie.isKeep() ? R.drawable.ic_bookmark_saved : R.drawable.ic_bookmark_not_saved);

        this.movie = movie;
    }

    @Override
    public void onError(String error) {
        Snackbar.make(mainContainer, error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void saveMovie() {
        btnSaveMovie.setImageResource(R.drawable.ic_bookmark_saved);
    }

    @Override
    public void removeMovie() {
        btnSaveMovie.setImageResource(R.drawable.ic_bookmark_not_saved);
    }

    @Override
    public void showMessage(int resMessage) {
        Snackbar.make(mainContainer, resMessage, Snackbar.LENGTH_LONG).show();
    }

    public MovieDetailPresenter getPresenter() {
        return component.getPresenter();
    }

    public ImageLoader getImageLoader() {
        return component.getImageLoader();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backToMovieList();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * The intent is for reset the values on the MovieListFragment's adapter, this way,
     * the fragment will trigger its onCreateView and check if a movie received from the
     * API is on the local DB
     */
    private void backToMovieList() {
        Intent intent = new Intent(this, DrawerActivity.class);
        intent.putExtra(DrawerActivity.EXTRA_MOVIE_ID, movie.getId());
        intent.putExtra(DrawerActivity.EXTRA_MOVIE_KEEP, movie.isKeep());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
