package com.doiliomatsinhe.popularmovies.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.doiliomatsinhe.popularmovies.data.MovieRepository;


public class MainViewModelFactory implements ViewModelProvider.Factory {

    private MovieRepository repository;
    private Application application;

    public MainViewModelFactory(MovieRepository repository, Application application) {
        this.repository = repository;
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (MainViewModel.class.isAssignableFrom(modelClass)) {
            return (T) new MainViewModel(repository,application);
        }
        throw new IllegalArgumentException("Unknown ViewModel Class!");
    }
}
