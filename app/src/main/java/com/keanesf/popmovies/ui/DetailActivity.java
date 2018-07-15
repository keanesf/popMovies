package com.keanesf.popmovies.ui;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.keanesf.popmovies.BuildConfig;
import com.keanesf.popmovies.R;
import com.keanesf.popmovies.data.database.FavoriteEntry;
import com.keanesf.popmovies.data.database.PopMoviesDatabase;
import com.keanesf.popmovies.models.Movie;
import com.keanesf.popmovies.models.Review;
import com.keanesf.popmovies.models.TmdbResponse;
import com.keanesf.popmovies.models.Trailer;
import com.keanesf.popmovies.utilities.ReviewAdapter;
import com.keanesf.popmovies.utilities.ReviewService;
import com.keanesf.popmovies.utilities.TrailerAdapter;
import com.keanesf.popmovies.utilities.TrailerService;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {

    private ScrollView detailLayout;
    private ImageView imageView;
    private TextView titleView;
    private TextView overviewView;
    private TextView dateView;
    private TextView voteAverageView;
    private PopMoviesDatabase popMoviesDatabase;
    private RecyclerView reviewRecyclerView;
    private RecyclerView trailerRecyclerView;
    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;

    public static final String YOUTUBE_BASE_URL = "http://www.youtube.com/watch?v=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = (ImageView) findViewById(R.id.detail_image);
        titleView = (TextView) findViewById(R.id.detail_title);
        overviewView = (TextView) findViewById(R.id.detail_overview);
        dateView = (TextView) findViewById(R.id.detail_date);
        voteAverageView = (TextView) findViewById(R.id.detail_vote_average);
        detailLayout = (ScrollView) findViewById(R.id.detail_layout);

        popMoviesDatabase = PopMoviesDatabase.getInstance(this);


        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            final Movie movie = intentThatStartedThisActivity.getParcelableExtra("movie");
            if (movie != null) {

                detailLayout.setVisibility(View.VISIBLE);

                String imageUrl = "http://image.tmdb.org/t/p/w185" + movie.getPosterPath();

                Picasso.with(imageView.getContext()).load(imageUrl).into(imageView);

                titleView.setText(movie.getTitle());
                overviewView.setText(movie.getOverview());

                if(movie.getReleaseDate() != null){
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        String date = DateUtils.formatDateTime(this, formatter.parse(movie.getReleaseDate()).getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
                        dateView.setText(date);
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                }

                voteAverageView.setText(movie.getVoteAverage() + "/10");

                final Button button = findViewById(R.id.button_id);
                final Button unFavButton = findViewById(R.id.button_un_fav);
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        final FavoriteEntry favoriteEntry = new FavoriteEntry(
                                movie.getId(), movie.getPosterPath(), movie.getTitle(),
                                movie.getOverview(), movie.getReleaseDate(),
                                movie.getVoteAverage());

                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                popMoviesDatabase.favoriteDao().insert(favoriteEntry);
                                finish();
                            }
                        });


                        // Code here executes on main thread after user presses favorite button
                        button.setVisibility(View.GONE);
                        unFavButton.setVisibility(View.VISIBLE);
//                        Context context = getApplicationContext();
//                        CharSequence text = "Favorite saved!";
//                        int duration = Toast.LENGTH_SHORT;
//
//                        Toast toast = Toast.makeText(context, text, duration);
//                        toast.show();
                    }
                });

                // Review Recycler View

                reviewRecyclerView = (RecyclerView) findViewById(R.id.reviewRecyclerView);

                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                reviewRecyclerView.setLayoutManager(layoutManager);

                reviewAdapter = new ReviewAdapter();
                reviewRecyclerView.setAdapter(reviewAdapter);

                loadReviewData(movie.getId());

                // Trailer Recycler View

                trailerRecyclerView = (RecyclerView) findViewById(R.id.trailerRecyclerView);
                LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this);
                trailerRecyclerView.setLayoutManager(trailerLayoutManager);
                trailerAdapter = new TrailerAdapter(this);
                trailerRecyclerView.setAdapter(trailerAdapter);

                loadTrailerData(movie.getId());




            }
            else {
                detailLayout.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void loadReviewData(Long movieId){
        new FetchReviewsTask().execute(movieId.toString());
    }

    private void loadTrailerData(Long movieId){
        new FetchTrailersTask().execute(movieId.toString());
    }

    public class FetchReviewsTask extends AsyncTask<String, Void, List<Review>> {

        private final String LOG_TAG = FetchReviewsTask.class.getSimpleName();

        @Override
        protected List<Review> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            ReviewService reviewService = ReviewService.retrofit.create(ReviewService.class);


            try {
                Call<TmdbResponse<Review>> apiCall = reviewService.getReviews(params[0], BuildConfig.API_KEY);
                return apiCall.execute().body().getResults();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            }

        }

        @Override
        protected void onPostExecute(List<Review> reviews) {
            if (reviews != null) {
                if (reviewAdapter != null) {
                    reviewAdapter.setReviews(reviews);
                }
            }
        }
    }

    public class FetchTrailersTask extends AsyncTask<String, Void, Trailer>{

        private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();

        @Override
        protected Trailer doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            TrailerService trailerService = TrailerService.retrofit.create(TrailerService.class);

            try {
                Call<Trailer> apiCall = trailerService.getTrailers(params[0], BuildConfig.API_KEY);
                return apiCall.execute().body();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            }

        }

        @Override
        protected void onPostExecute(Trailer trailer) {
            if (trailerAdapter != null) {
                trailerAdapter.setTrailers(trailer);
            }
        }

    }

    @Override
    public void onClick(String trailer) {
        Intent videoIntent = new Intent(
                Intent.ACTION_VIEW, Uri.parse( YOUTUBE_BASE_URL + trailer));
        startActivity(videoIntent);
    }
}
