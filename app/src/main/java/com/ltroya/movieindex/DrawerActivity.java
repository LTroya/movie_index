package com.ltroya.movieindex;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.ltroya.movieindex.movielist.ui.MovieListFragment;
import com.ltroya.movieindex.moviesaved.ui.MovieSavedFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_MOVIE_ID = "id";
    public static final String EXTRA_MOVIE_KEEP = "keep";
    public static final String EXTRA_FRAGMENT_TO_LAUNCH = "fragment";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.main_container)
    RelativeLayout mainContainer;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;

    private Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        setupFragment(savedInstanceState);
    }

    private void setupFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null && extras.get(DrawerActivity.EXTRA_FRAGMENT_TO_LAUNCH) != null) {
                int menuId = extras.getInt(DrawerActivity.EXTRA_FRAGMENT_TO_LAUNCH);
                selectedItem(menuId);
                navigationView.getMenu().getItem(getMenuIdPosition(menuId)).setChecked(true);
            } else {
                selectedItem(R.id.nav_movies);
                navigationView.getMenu().getItem(0).setChecked(true);
            }
        }
    }

    private int getMenuIdPosition(int menuId) {
        switch (menuId) {
            case R.id.nav_watch_later: return 1;
            default: return 0;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        selectedItem(id);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void selectedItem(int id) {
        if (id == R.id.nav_movies) {
            Bundle args = getIntent().getExtras();
            fragment = new MovieListFragment();
            fragment.setArguments(args);
        } else if (id == R.id.nav_watch_later) {
            fragment = new MovieSavedFragment();
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_container, fragment, "visible_fragment");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    // This method needs to be override in order to listen this event in the
    // fragments
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
