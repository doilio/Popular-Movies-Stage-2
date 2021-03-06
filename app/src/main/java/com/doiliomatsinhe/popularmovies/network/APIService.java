package com.doiliomatsinhe.popularmovies.network;

import com.doiliomatsinhe.popularmovies.model.MovieResponse;
import com.doiliomatsinhe.popularmovies.model.ReviewResponse;
import com.doiliomatsinhe.popularmovies.model.TrailerResponse;

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

    /**
     * Asynchronous call to themoviedb API
     *
     * @param movieId  Identifies the movie
     * @param apiKey   used for querying
     * @param language is part of the url
     * @return a list of trailers
     */
    @GET("/3/movie/{movie_id}/videos")
    Call<TrailerResponse> getTrailers(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    /**
     * Asynchronous call to themoviedb API
     *
     * @param movieId Identifies the movie
     * @param apiKey   used for querying
     * @param language is part of the url
     * @param page     is part of the url
     * @return a list of reviews
     */
    @GET("/3/movie/{movie_id}}/reviews")
    Call<ReviewResponse> getReviews(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );
}
