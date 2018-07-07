package com.keanesf.popmovies.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keanesf.popmovies.R;
import com.keanesf.popmovies.models.Trailer;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private List<Trailer> trailers;

    private final TrailerAdapterOnClickHandler trailerAdapterOnClickHandler;

    public TrailerAdapter(TrailerAdapterOnClickHandler clickHandler){trailerAdapterOnClickHandler = clickHandler;}

    @Override
    public int getItemCount() {
        if (null == trailers) return 0;
        return trailers.size();
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        // fixme this is wrong
        int layoutIdForListItem = R.layout.grid_item_movie;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder movieAdapterViewHolder, int position) {
        Trailer movie = trailers.get(position);

        //String imageUrl = "http://image.tmdb.org/t/p/w185" + movie.getPosterPath();

        //Picasso.with(movieAdapterViewHolder.movieImageView.getContext()).load(imageUrl).into(movieAdapterViewHolder.movieImageView);
    }


    public interface TrailerAdapterOnClickHandler {
        void onClick(Trailer trailer);
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TrailerAdapterViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Trailer trailer = trailers.get(adapterPosition);
            trailerAdapterOnClickHandler.onClick(trailer);
        }
    }
}
