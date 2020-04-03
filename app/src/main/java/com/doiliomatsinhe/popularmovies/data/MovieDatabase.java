package com.doiliomatsinhe.popularmovies.data;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.doiliomatsinhe.popularmovies.model.Movie;

@Database(entities = {Movie.class,}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    public abstract MovieDao movieDao();

    private static final String TAG = MovieDatabase.class.getSimpleName();
    private static MovieDatabase INSTANCE;
    private static final String DATABASE_NAME = "favorites_list";

    public static MovieDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (MovieDatabase.class) {
                Log.d(TAG, "Creating a new Database Instance");
                INSTANCE = Room.databaseBuilder(context,
                        MovieDatabase.class,
                        DATABASE_NAME).build();
            }
        }
        Log.d(TAG, "Getting the Database Instance");
        return INSTANCE;
    }
}
