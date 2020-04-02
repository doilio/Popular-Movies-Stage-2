package com.doiliomatsinhe.popularmovies.ui.detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.doiliomatsinhe.popularmovies.R;
import com.doiliomatsinhe.popularmovies.databinding.ActivityDetailBinding;
import com.doiliomatsinhe.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import static com.doiliomatsinhe.popularmovies.ui.main.MainActivity.MOVIE;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Intent i = getIntent();
        if (i != null) {
            Movie movie = (Movie) i.getSerializableExtra(MOVIE);
            if (movie != null) {
                populateUI(movie);
            }
        }
    }

    private void populateUI(Movie movie) {
        Picasso.get().load(movie.getBackdropPath()).into(binding.movieCover);
        Picasso.get().load(movie.getPosterPath()).into(binding.moviePoster);
        binding.movieTitle.setText(movie.getTitle());
        binding.movieRating.setText(String.valueOf(movie.getVoteAverage()));
        binding.userRatingCount.setText(String.valueOf(movie.getVoteCount()));
        binding.releaseDateText.setText(movie.getReleaseDate());
        binding.overviewText.setText(movie.getOverview());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_favorite:
                Toast.makeText(this, R.string.favorite_clicked, Toast.LENGTH_SHORT).show();
                break;
            case R.id.ic_share:
                Toast.makeText(this, R.string.share_clicked, Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
