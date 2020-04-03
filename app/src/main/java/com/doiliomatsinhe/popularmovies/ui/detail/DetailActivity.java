package com.doiliomatsinhe.popularmovies.ui.detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.doiliomatsinhe.popularmovies.R;
import com.doiliomatsinhe.popularmovies.Utils;
import com.doiliomatsinhe.popularmovies.adapter.ReviewAdapter;
import com.doiliomatsinhe.popularmovies.adapter.TrailerAdapter;
import com.doiliomatsinhe.popularmovies.databinding.ActivityDetailBinding;
import com.doiliomatsinhe.popularmovies.model.Movie;
import com.doiliomatsinhe.popularmovies.model.MovieRepository;
import com.doiliomatsinhe.popularmovies.model.Review;
import com.doiliomatsinhe.popularmovies.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.doiliomatsinhe.popularmovies.ui.main.MainActivity.MOVIE;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerItemClickListener, ReviewAdapter.ReviewItemClickListener {

    private ActivityDetailBinding binding;
    private static final String TAG = "DetailActivity";
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;

    private List<Trailer> trailerList = new ArrayList<>();
    private List<Review> reviewList = new ArrayList<>();


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
        Log.d("DetailActivity", "populateUI: " + movie.getId());
        Picasso.get().load(movie.getBackdropPath()).into(binding.movieCover);
        Picasso.get().load(movie.getPosterPath()).into(binding.moviePoster);
        binding.movieTitle.setText(movie.getTitle());
        binding.movieRating.setText(String.valueOf(movie.getVoteAverage()));
        binding.userRatingCount.setText(String.valueOf(movie.getVoteCount()));
        binding.releaseDateText.setText(movie.getReleaseDate());
        binding.overviewText.setText(movie.getOverview());

        // Prepare DetailViewModel
        prepareDetailViewModel(movie.getId());
    }


    private void prepareDetailViewModel(Integer id) {
        MovieRepository repository = new MovieRepository(getString(R.string.api_key));
        DetailViewModelFactory factory = new DetailViewModelFactory(repository, id);
        DetailViewModel viewModel = new ViewModelProvider(this, factory).get(DetailViewModel.class);

        initAdapters();

        viewModel.trailersList.observe(this, new Observer<List<Trailer>>() {
            @Override
            public void onChanged(List<Trailer> trailers) {
                trailerAdapter.setTrailerList(trailers);
                trailerList = trailers;

            }
        });
        viewModel.reviewsList.observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                reviewAdapter.setReviewList(reviews);
                reviewList = reviews;

            }
        });

    }

    private void initAdapters() {
        // Initializing The Trailer Adapter
        trailerAdapter = new TrailerAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.trailerRecycler.setLayoutManager(layoutManager);
        binding.trailerRecycler.setHasFixedSize(true);
        binding.trailerRecycler.setAdapter(trailerAdapter);

        // Initializing The Reviews Adapter
        reviewAdapter = new ReviewAdapter(this);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        binding.reviewRecycler.setLayoutManager(layoutManager1);
        binding.reviewRecycler.setHasFixedSize(true);
        binding.reviewRecycler.setAdapter(reviewAdapter);

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
                shareMovie();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void shareMovie() {
        Intent i = new Intent(Intent.ACTION_SEND);
        String firstTrailer = "https://www.youtube.com/watch?v=" + trailerList.get(0).getKey();
        i.putExtra(Intent.EXTRA_TEXT, getString(R.string.movie_sharing) + " " + firstTrailer);
        i.setType("text/plain");
        if (i.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(i, getString(R.string.share_using)));
        }
    }

    @Override
    public void onTrailerItemClick(int position) {
        Trailer trailer = trailerList.get(position);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(String.format("https://www.youtube.com/watch?v=%s", trailer.getKey())));
        if (Utils.isAppInstalled(this, "com.google.android.youtube")) {
            i.setPackage("com.google.android.youtube");
        }
        startActivity(i);
    }

    @Override
    public void onReviewItemClick(int position) {
        Review review = reviewList.get(position);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(review.getUrl()));
        startActivity(i);
    }
}
