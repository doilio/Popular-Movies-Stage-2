package com.doiliomatsinhe.popularmovies.adapter;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doiliomatsinhe.popularmovies.databinding.MovieItemBinding;
import com.doiliomatsinhe.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<Movie> movieList = new ArrayList<>();
    final private MovieItemClickListener onClickListener;

    public MovieAdapter(MovieItemClickListener listener) {
        onClickListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MovieItemBinding binding;

        ViewHolder(@NonNull MovieItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        void bind(Movie movie) {

            if (movie.getVideo() != null) {
                // Loading From Internet
                Picasso.get().load(movie.getPosterPath()).into(binding.movieImage);
            } else {
                // Loading From Internal Storage
                ContextWrapper cw = new ContextWrapper(binding.getRoot().getContext());
                File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                File posterFile = new File(directory, movie.getTitle() + "poster.jpeg");
                Picasso.get().load(posterFile).into(binding.movieImage);
            }

            binding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            int clickedItem = getAdapterPosition();
            onClickListener.onMovieItemClick(clickedItem);
        }
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MovieItemBinding binding = MovieItemBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie currentMovie = movieList.get(position);
        holder.bind(currentMovie);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public interface MovieItemClickListener {
        void onMovieItemClick(int position);
    }
}
