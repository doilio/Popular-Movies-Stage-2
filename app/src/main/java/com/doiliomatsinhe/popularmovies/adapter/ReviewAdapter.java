package com.doiliomatsinhe.popularmovies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doiliomatsinhe.popularmovies.databinding.ReviewItemBinding;
import com.doiliomatsinhe.popularmovies.model.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<Review> reviewList = new ArrayList<>();
    final private ReviewItemClickListener onClickListener;

    public ReviewAdapter(ReviewItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ReviewItemBinding binding;

        ViewHolder(@NonNull ReviewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        void bind(Review review) {
            binding.reviewAuthor.setText(review.getAuthor());
            binding.reviewContent.setText(review.getContent());

            binding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            int clickedItem = getAdapterPosition();
            onClickListener.onReviewItemClick(clickedItem);

        }
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ReviewItemBinding binding = ReviewItemBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        Review currentReview = reviewList.get(position);
        holder.bind(currentReview);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public interface ReviewItemClickListener {
        void onReviewItemClick(int position);
    }

}
