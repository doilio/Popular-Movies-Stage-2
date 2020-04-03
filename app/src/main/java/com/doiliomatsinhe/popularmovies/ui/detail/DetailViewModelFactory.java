package com.doiliomatsinhe.popularmovies.ui.detail;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.doiliomatsinhe.popularmovies.data.MovieRepository;

public class DetailViewModelFactory implements ViewModelProvider.Factory {

    private MovieRepository repository;
    private int movieId;

    public DetailViewModelFactory(MovieRepository repository, int movieId) {
        this.repository = repository;
        this.movieId = movieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (DetailViewModel.class.isAssignableFrom(modelClass)) {
            return (T) new DetailViewModel(repository, movieId);
        }
        throw new IllegalArgumentException("Unknown ViewModel Class!");
    }
}