package com.keanesf.popmovies.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.keanesf.popmovies.R;
import com.keanesf.popmovies.models.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private List<String> trailers;

    private final TrailerAdapterOnClickHandler trailerAdapterOnClickHandler;

    public TrailerAdapter(TrailerAdapterOnClickHandler clickHandler){trailerAdapterOnClickHandler = clickHandler;}

    private final String LOG_TAG = this.getClass().getSimpleName();

    @Override
    public int getItemCount() {
        if (null == trailers) return 0;
        return trailers.size();
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_trailer;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder trailerAdapterViewHolder, int position) {
        String trailer = trailers.get(position);

        final Context context = trailerAdapterViewHolder.trailerImageView.getContext();

        String thumbnailUrl = "http://img.youtube.com/vi/" + trailer  + "/0.jpg";


        Picasso.with(context)
                .load(thumbnailUrl)
                .config(Bitmap.Config.RGB_565)
                .into(trailerAdapterViewHolder.trailerImageView);
    }


    public interface TrailerAdapterOnClickHandler {
        void onClick(String trailer);
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView trailerImageView;

        public TrailerAdapterViewHolder(View view) {
            super(view);
            trailerImageView = (ImageView) view.findViewById(R.id.trailer_thumbnail);
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
            String trailer = trailers.get(adapterPosition);
            trailerAdapterOnClickHandler.onClick(trailer);
        }
    }

    public void setTrailers(Trailer trailer) {
        List<String> youTubeTrailers = new ArrayList<>();
        for (Trailer.Youtube youtube: trailer.getYoutubeVideos()) {
            youTubeTrailers.add(youtube.getSource());
        }
        this.trailers = youTubeTrailers;
        notifyDataSetChanged();
    }
}
