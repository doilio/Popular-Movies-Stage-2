package com.doiliomatsinhe.popularmovies.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.doiliomatsinhe.popularmovies.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM favorites ORDER BY title")
    LiveData<List<Movie>> getAllFavorites();

    @Insert
    void insertFavorite(Movie movie);

    @Delete
    void removeFavorite(Movie movie);

    @Query("SELECT * FROM favorites WHERE id = :id")
    LiveData<Movie> getFavoriteById(int id);

}
