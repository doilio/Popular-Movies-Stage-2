package com.doiliomatsinhe.popularmovies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doiliomatsinhe.popularmovies.databinding.TrailerItemBinding;
import com.doiliomatsinhe.popularmovies.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private List<Trailer> trailerList = new ArrayList<>();
    final private TrailerItemClickListener onClickListener;
    private static final String BASE_URL = "https://img.youtube.com/vi/";
    private static final String QUALITY = "/hqdefault.jpg";

    public TrailerAdapter(TrailerItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TrailerItemBinding binding;

        public ViewHolder(@NonNull TrailerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        void bind(Trailer trailer) {
            String imageCover = BASE_URL + trailer.getKey() + QUALITY;
            Picasso.get().load(imageCover).fit().centerCrop().into(binding.trailerCover);
            binding.trailerName.setText(trailer.getName());


            binding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            int clickedItem = getAdapterPosition();
            onClickListener.onTrailerItemClick(clickedItem);
        }
    }

    public void setTrailerList(List<Trailer> trailerList) {
        this.trailerList = trailerList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        TrailerItemBinding binding = TrailerItemBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trailer currentTrailer = trailerList.get(position);
        holder.bind(currentTrailer);
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public interface TrailerItemClickListener {
        void onTrailerItemClick(int position);
    }


}
