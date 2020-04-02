package com.doiliomatsinhe.popularmovies.network;

import com.doiliomatsinhe.popularmovies.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    /**
     * Asynchronous call to themoviedb API
     *
     * @param category used for sorting
     * @param apiKey   used for querying
     * @param language is part of the url
     * @param page     is part of the url
     * @return a list of movies
     */
    @GET("/3/movie/{category}")
    Call<MovieResponse> getMovies(
            @Path("category") String category,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page);
}
