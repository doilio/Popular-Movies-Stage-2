package com.doiliomatsinhe.popularmovies.ui.detail;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doiliomatsinhe.popularmovies.data.MovieDatabase;
import com.doiliomatsinhe.popularmovies.data.MovieRepository;
import com.doiliomatsinhe.popularmovies.model.Movie;
import com.doiliomatsinhe.popularmovies.model.Review;
import com.doiliomatsinhe.popularmovies.model.ReviewResponse;
import com.doiliomatsinhe.popularmovies.model.Trailer;
import com.doiliomatsinhe.popularmovies.model.TrailerResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailViewModel extends ViewModel {

    private MovieRepository repository;

    private MutableLiveData<List<Trailer>> _trailersList = new MutableLiveData<>();
    public LiveData<List<Trailer>> trailersList = _trailersList;

    private MutableLiveData<List<Review>> _reviewsList = new MutableLiveData<>();
    public LiveData<List<Review>> reviewsList = _reviewsList;

    private LiveData<Movie> favorite;

    public DetailViewModel(MovieRepository repository, int movieId, MovieDatabase database) {
        this.repository = repository;
        getTrailers(movieId);
        getReviews(movieId);

        favorite = database.movieDao().getFavoriteById(movieId);
    }

    public LiveData<Movie> getFavorite() {
        return favorite;
    }

    /**
     * Gets a list of Reviews by making an Asynchronous call with Retrofit.
     *
     * @param movieId identifies a movie uniquely and returns data about it
     */
    private void getReviews(int movieId) {
        repository.getReviews(movieId).enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (response.body() != null) {
                    ReviewResponse reviewResponse = response.body();

                    List<Review> listOfReviews = new ArrayList<>(reviewResponse.getResults());
                    _reviewsList.setValue(listOfReviews);
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Log.d("DetailViewModel", "Review: " + t.getMessage());
            }
        });
    }

    /**
     * Gets a list of Trailers by making an Asynchronous call with Retrofit.
     *
     * @param movieId identifies a movie uniquely and returns data about it
     */
    private void getTrailers(int movieId) {
        repository.getTrailers(movieId).enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                if (response.body() != null) {
                    TrailerResponse trailerResponse = response.body();

                    List<Trailer> listOfTrailers = new ArrayList<>(trailerResponse.getResults());
                    _trailersList.setValue(listOfTrailers);
                } else {
                    // Didn't extract this because I would then have to pass the context of the activity through the Factory.
                    Log.d("DetailViewModel", "Error Getting Trailers");
                }
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                Log.d("DetailViewModel", "Trailer: " + t.getMessage());
            }
        });

    }

}
