package com.keanesf.popmovies;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private List<Movie> movies;

    public MovieAdapter(Activity context, List<Movie> myMovies) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        movies = myMovies;
    }

    @Override
    public Movie getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return movies.size();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        RecyclerView.ViewHolder viewHolder;

        if (view == null) {
            view = inflater.inflate(R.layout.grid_item_movie, parent, false);
            viewHolder = new RecyclerView.ViewHolder(view);
            view.setTag(viewHolder);
        }

        final Movie movie = getItem(position);

        String imageUrl = "http://image.tmdb.org/t/p/w185" + movie.getPosterPath();

        viewHolder = (RecyclerView.ViewHolder) view.getTag();

        Picasso.with(getContext()).load(imageUrl).into(viewHolder.);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_movie, parent, false);
        }

        return convertView;
    }

    public void setData(List<Movie> data) {
        clear();
        for (Movie movie : data) {
            add(movie);
        }
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mWeatherTextView;

        public MovieAdapterViewHolder(View view) {
            super(view);
            mWeatherTextView = (TextView) view.findViewById(R.layout.grid_item_movie);
            view.setOnClickListener(this);
        }

        // COMPLETED (6) Override onClick, passing the clicked day's data to mClickHandler via its onClick method
        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String weatherForDay = mWeatherData[adapterPosition];
            mClickHandler.onClick(weatherForDay);
        }
    }
}
