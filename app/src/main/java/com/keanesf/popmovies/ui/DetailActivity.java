package com.keanesf.popmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.keanesf.popmovies.R;
import com.keanesf.popmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

public class DetailActivity extends AppCompatActivity {

    private ScrollView detailLayout;
    private ImageView imageView;
    private TextView titleView;
    private TextView overviewView;
    private TextView dateView;
    private TextView voteAverageView;

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

        final Button button = findViewById(R.id.button_id);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Context context = getApplicationContext();
                CharSequence text = "Hello toast!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            Movie movie = intentThatStartedThisActivity.getParcelableExtra("movie");
            if (movie != null) {

                detailLayout.setVisibility(View.VISIBLE);

                String imageUrl = "http://image.tmdb.org/t/p/w185" + movie.getPosterPath();

                Picasso.with(imageView.getContext()).load(imageUrl).into(imageView);

                titleView.setText(movie.getTitle());
                overviewView.setText(movie.getOverview());

                String movie_date = movie.getReleaseDate();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    String date = DateUtils.formatDateTime(this, formatter.parse(movie_date).getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
                    dateView.setText(date);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

                voteAverageView.setText(movie.getVoteAverage() + "/10");
            }
            else {
                detailLayout.setVisibility(View.INVISIBLE);
            }
        }
    }
}
