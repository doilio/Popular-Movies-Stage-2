package com.doiliomatsinhe.popularmovies.ui.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doiliomatsinhe.popularmovies.model.Movie;
import com.doiliomatsinhe.popularmovies.model.MovieRepository;
import com.doiliomatsinhe.popularmovies.model.MovieResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {

    private MovieRepository repository;
    private MutableLiveData<List<Movie>> _movieList = new MutableLiveData<>();
    //public LiveData<List<Movie>> movieList = _movieList; will use this in the future.

    public String filter;

    public String getFilter() { return filter; }

    public void setFilter(String filter) { this.filter = filter; }

    public MainViewModel(MovieRepository repository) {
        this.repository = repository;
    }


    /**
     * Gets a list of Movies by making an Asynchronous call with Retrofit.
     * @param categoria filter to determine what list will show up
     * @return LiveData to update the list if API changes happen.
     */
    public LiveData<List<Movie>> getMovies(String categoria) {
        repository.getMovies(categoria).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.body() != null) {

                    MovieResponse movieResponse = response.body();
                    List<Movie> movieList = movieResponse.getResults();

                    List<Movie> listOfMovies = new ArrayList<>(movieResponse.getResults());

                    _movieList.setValue(listOfMovies);
                    Log.d("MainViewModel", "Movie: " + movieList.get(0).getPosterPath());
                } else {
                    // Didn't extract this because I would then have to pass the context of the activity through the Factory.
                    Log.d("MainViewModel", "Error Getting Movie Results");
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d("MainViewModel", "Movie: " + t.getMessage());
            }
        });

        return _movieList;

    }
}
