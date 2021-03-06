package com.doiliomatsinhe.popularmovies.data;


import com.doiliomatsinhe.popularmovies.model.MovieResponse;
import com.doiliomatsinhe.popularmovies.model.ReviewResponse;
import com.doiliomatsinhe.popularmovies.model.TrailerResponse;
import com.doiliomatsinhe.popularmovies.network.APIService;
import com.doiliomatsinhe.popularmovies.network.ServiceBuilder;

import retrofit2.Call;

public class MovieRepository {

    private String key;

    public MovieRepository(String apiKey) {
        key = apiKey;
    }

    private static final String LANGUAGE = "en-US";
    private static final int PAGE = 1;

    private static APIService service = ServiceBuilder.BuildService(APIService.class);


    public Call<MovieResponse> getMovies(String categoria) {
        return service.getMovies(categoria, key, LANGUAGE, PAGE);
    }

    public Call<TrailerResponse> getTrailers(int movieId) {
        return service.getTrailers(movieId, key, LANGUAGE);
    }

    public Call<ReviewResponse> getReviews(int movieId) {
        return service.getReviews(movieId, key, LANGUAGE, PAGE);
    }

}
