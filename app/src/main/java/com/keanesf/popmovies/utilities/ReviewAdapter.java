package com.keanesf.popmovies.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keanesf.popmovies.R;
import com.keanesf.popmovies.models.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private List<Review> mReviews;

    public ReviewAdapter(){
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_review, parent, false);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder viewHolder, int position) {
        String reviewString = mReviews.get(position).getContent();

        TextView textView = viewHolder.reviewTextView;
        textView.setText(reviewString);
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        if (null == mReviews) return 0;
        return mReviews.size();
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder{

        public TextView reviewTextView;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);

            reviewTextView = (TextView) itemView.findViewById(R.id.review_content);
        }
    }

    public void setReviews(List<Review> reviews) {
        this.mReviews = reviews;
        notifyDataSetChanged();
    }

}
