package com.keanesf.popmovies;

import android.app.Fragment;
import android.net.ParseException;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

public class DetailActivityFragment extends android.support.v4.app.Fragment {

    public static final String TAG = DetailActivityFragment.class.getSimpleName();

    static final String DETAIL_MOVIE = "DETAIL_MOVIE";

    private Movie movie;
    private ScrollView detailLayout;
    private ImageView imageView;
    private TextView titleView;
    private TextView overviewView;
    private TextView dateView;
    private TextView voteAverageView;

    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            movie = arguments.getParcelable(DetailActivityFragment.DETAIL_MOVIE);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        detailLayout = (ScrollView) rootView.findViewById(R.id.detail_layout);

        if (movie != null) {
            detailLayout.setVisibility(View.VISIBLE);
        } else {
            detailLayout.setVisibility(View.INVISIBLE);
        }





        return rootView;
    }

}
