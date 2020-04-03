package com.doiliomatsinhe.popularmovies.ui.detail;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.doiliomatsinhe.popularmovies.data.MovieDatabase;
import com.doiliomatsinhe.popularmovies.data.MovieRepository;

public class DetailViewModelFactory implements ViewModelProvider.Factory {

    private MovieRepository repository;
    private int movieId;
    private MovieDatabase database;

    public DetailViewModelFactory(MovieRepository repository, int movieId, MovieDatabase database) {
        this.repository = repository;
        this.movieId = movieId;
        this.database = database;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (DetailViewModel.class.isAssignableFrom(modelClass)) {
            return (T) new DetailViewModel(repository, movieId,database);
        }
        throw new IllegalArgumentException("Unknown ViewModel Class!");
    }
}