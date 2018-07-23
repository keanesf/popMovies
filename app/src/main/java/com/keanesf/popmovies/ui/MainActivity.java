package com.keanesf.popmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.keanesf.popmovies.BuildConfig;
import com.keanesf.popmovies.data.database.FavoriteEntry;
import com.keanesf.popmovies.data.database.PopMoviesDatabase;
import com.keanesf.popmovies.utilities.MovieAdapter;
import com.keanesf.popmovies.R;
import com.keanesf.popmovies.models.Movie;
import com.keanesf.popmovies.models.TmdbResponse;
import com.keanesf.popmovies.utilities.MovieService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private ArrayList<Movie> movies = null;

    private static final String POPULARITY_DESC = "popularity.desc";
    private static final String RATING_DESC = "vote_average.desc";
    private static final String FAVORITE_DESC = "favorite.desc";
    private static final String SORT_SETTING_KEY = "sort_setting";
    private static final String MOVIES_KEY = "movies";
    private String sortBy = POPULARITY_DESC;
    private PopMoviesDatabase popMoviesDatabase;
    private static final String BUNDLE_RECYCLER_LAYOUT = "MainActivity.recycler.layout";
    private final String LOG_TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        popMoviesDatabase = PopMoviesDatabase.getInstance(this);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        recyclerView = (RecyclerView) findViewById(R.id.rvMovies);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        movieAdapter = new MovieAdapter(this);

        recyclerView.setAdapter(movieAdapter);

        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         *
         * Please note: This so called "ProgressBar" isn't a bar by default. It is more of a
         * circle. We didn't make the rules (or the names of Views), we just follow them.
         */
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        /* Once all of our views are setup, we can load the movie data. */
        if(savedInstanceState == null)
            loadMovieData(sortBy);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem action_sort_by_popularity = menu.findItem(R.id.action_sort_by_popular);
        MenuItem action_sort_by_rating = menu.findItem(R.id.action_sort_by_top_rated);

        if (sortBy.contentEquals(POPULARITY_DESC)) {
            if (!action_sort_by_popularity.isChecked()) {
                action_sort_by_popularity.setChecked(true);
            }
        } else if (sortBy.contentEquals(RATING_DESC)) {
            if (!action_sort_by_rating.isChecked()) {
                action_sort_by_rating.setChecked(true);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sort_by_popular:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                sortBy = POPULARITY_DESC;
                loadMovieData(sortBy);
                return true;
            case R.id.action_sort_by_top_rated:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                sortBy = RATING_DESC;
                loadMovieData(sortBy);
                return true;
            case R.id.action_favorites:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                sortBy = FAVORITE_DESC;
                loadMovieData(sortBy);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(SORT_SETTING_KEY, sortBy);
        outState.putParcelableArrayList(MOVIES_KEY, movies);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            sortBy = savedInstanceState.getString(SORT_SETTING_KEY);
            movies = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
            loadMovieData(sortBy);
        }
    }

    private void loadMovieData(String sortBy){
        if(isOnline()){
            showMovieDataView();
            new FetchMoviesTask().execute(sortBy);
        }
        else {
            mErrorMessageDisplay.setVisibility(View.VISIBLE);
        }

    }

    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the movie data is visible */
        recyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the movie
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        recyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            MovieService movieDbService = MovieService.retrofit.create(MovieService.class);

            try {
                if (POPULARITY_DESC.equals(params[0])) {
                    Call<TmdbResponse<Movie>> apiCall = movieDbService.listPopMovies(BuildConfig.API_KEY);
                    return apiCall.execute().body().getResults();

                } else if (RATING_DESC.equals(params[0])) {
                    Call<TmdbResponse<Movie>> apiCall = movieDbService.listTopMovies(BuildConfig.API_KEY);
                    return apiCall.execute().body().getResults();
                } else if (FAVORITE_DESC.equals(params[0])){
                    List<FavoriteEntry> favoriteEntries = popMoviesDatabase.favoriteDao().getAll();
                    List<Movie> movies = new ArrayList<>();
                    // convert favorites to movies
                    for(FavoriteEntry favoriteEntry: favoriteEntries){
                        Movie movie = new Movie(favoriteEntry.getId(), favoriteEntry.getVoteAverage(),
                                favoriteEntry.getTitle(), favoriteEntry.getPosterPath(),
                                favoriteEntry.getOverview(), favoriteEntry.getReleaseDate());
                        movies.add(movie);
                    }
                    return movies;
                }
                else {
                    return null;
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            }

        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movies != null) {
                if (movieAdapter != null) {
                    showMovieDataView();
                    movieAdapter.setMovies(movies);
                }
                else {
                    showErrorMessage();
                }
            }
        }
    }

    @Override
    public void onClick(Movie movie) {
        Intent movieDetailIntent = new Intent(MainActivity.this, DetailActivity.class);
        movieDetailIntent.putExtra("movie", movie);
        startActivity(movieDetailIntent);
    }

}
