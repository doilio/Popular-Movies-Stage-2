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

import com.doiliomatsinhe.popularmovies.AppExecutors;
import com.doiliomatsinhe.popularmovies.R;
import com.doiliomatsinhe.popularmovies.Utils;
import com.doiliomatsinhe.popularmovies.adapter.ReviewAdapter;
import com.doiliomatsinhe.popularmovies.adapter.TrailerAdapter;
import com.doiliomatsinhe.popularmovies.data.MovieDatabase;
import com.doiliomatsinhe.popularmovies.databinding.ActivityDetailBinding;
import com.doiliomatsinhe.popularmovies.model.Movie;
import com.doiliomatsinhe.popularmovies.data.MovieRepository;
import com.doiliomatsinhe.popularmovies.model.Review;
import com.doiliomatsinhe.popularmovies.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.doiliomatsinhe.popularmovies.ui.main.MainActivity.MOVIE;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerItemClickListener, ReviewAdapter.ReviewItemClickListener {

    private ActivityDetailBinding binding;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private static final String TAG = DetailActivity.class.getSimpleName();

    private List<Trailer> trailerList = new ArrayList<>();
    private List<Review> reviewList = new ArrayList<>();

    private Movie movie;
    private int movieId;
    private DetailViewModel viewModel;

    // Local Database Members
    private MovieDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Intent i = getIntent();
        if (i != null) {
            movie = (Movie) i.getSerializableExtra(MOVIE);
            if (movie != null) {
                populateUI(movie);

                movieId = movie.getId();
                setupViewModel(movieId);
            }
        }

        initAdapters();

        initLists();


    }

    private void initLists() {
        // Gets the List of trailers as livedata
        viewModel.trailersList.observe(this, new Observer<List<Trailer>>() {
            @Override
            public void onChanged(List<Trailer> trailers) {
                trailerAdapter.setTrailerList(trailers);
                trailerList = trailers;

            }
        });

        // Gets the List of reviews as livedata
        viewModel.reviewsList.observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                reviewAdapter.setReviewList(reviews);
                reviewList = reviews;

            }
        });
    }

    private void setupViewModel(Integer id) {
        database = MovieDatabase.getInstance(this);
        // Initializes the ViewModel
        MovieRepository repository = new MovieRepository(getString(R.string.api_key));
        DetailViewModelFactory factory = new DetailViewModelFactory(repository, id, database);
        viewModel = new ViewModelProvider(this, factory).get(DetailViewModel.class);
    }

    /**
     * Populates the UI with Movie Details
     *
     * @param movie to use to populate fields
     */
    private void populateUI(Movie movie) {
        Log.d(TAG, "populateUI: " + movie.getId());
        Picasso.get().load(movie.getBackdropPath()).into(binding.movieCover);
        Picasso.get().load(movie.getPosterPath()).into(binding.moviePoster);
        binding.movieTitle.setText(movie.getTitle());
        binding.movieRating.setText(String.valueOf(movie.getVoteAverage()));
        binding.userRatingCount.setText(String.valueOf(movie.getVoteCount()));
        binding.releaseDateText.setText(movie.getReleaseDate());
        binding.overviewText.setText(movie.getOverview());


    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        final MenuItem favoriteItem = menu.findItem(R.id.ic_favorite);

        viewModel.getFavorite().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                if (movie != null) {
                    favoriteItem.setIcon(R.drawable.ic_favorite_black_24dp);
                    Log.d(TAG, "Exists in DB " + movie.getTitle());
                } else {
                    favoriteItem.setIcon(R.drawable.ic_favorite_border_black_24dp);
                    Log.d(TAG, "Doesn't exist in DB");

                }
            }
        });

        favoriteItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                viewModel.getFavorite().observe(DetailActivity.this, new Observer<Movie>() {
                    @Override
                    public void onChanged(Movie movie) {
                        viewModel.getFavorite().removeObserver(this);
                        if (movie != null) {
                            Log.d(TAG, "This Movie is being deleted from favorites" + movie.getTitle());
                            removeFromFavorites(movie);
                            favoriteItem.setIcon(R.drawable.ic_favorite_border_black_24dp);
                        } else {
                            addToFavorites();
                            favoriteItem.setIcon(R.drawable.ic_favorite_black_24dp);
                            Log.d(TAG, "This movie was Added to Favorites");
                        }
                    }
                });
                return false;
            }
        });

        return true;
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
        if (item.getItemId() == R.id.ic_share) {
            shareMovie();
        }
        return super.onOptionsItemSelected(item);
    }

    private void removeFromFavorites(final Movie movie) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Verificar se este id ja existe se sim apaga se nao salva
                database.movieDao().removeFavorite(movie);
            }
        });
    }

    private void addToFavorites() {
        if (movie != null) {
            int voteCount = movie.getVoteCount();
            String posterPath = movie.getPosterPath();
            int id = movie.getId();
            String backdropPath = movie.getBackdropPath();
            String title = movie.getTitle();
            double voteAverage = movie.getVoteAverage();
            String overview = movie.getOverview();
            String releaseDate = movie.getReleaseDate();
            final Movie movie = new Movie(voteCount, posterPath, id, backdropPath, title, voteAverage, overview, releaseDate);

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    // Verificar se este id ja existe se sim apaga se nao salva
                    database.movieDao().insertFavorite(movie);
                }
            });

        }


    }

    /**
     * Metthod to share first Trailer
     */
    private void shareMovie() {
        Intent i = new Intent(Intent.ACTION_SEND);
        String firstTrailer = "https://www.youtube.com/watch?v=" + trailerList.get(0).getKey();
        i.putExtra(Intent.EXTRA_TEXT, getString(R.string.movie_sharing) + " " + firstTrailer);
        i.setType("text/plain");
        if (i.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(i, getString(R.string.share_using)));
        }
    }

    /**
     * Checks if The Youtube App is Installed.
     * If it is it opens the link through the App
     * Else it opens through the browser
     *
     * @param position of current item
     */
    @Override
    public void onTrailerItemClick(int position) {
        Trailer trailer = trailerList.get(position);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(String.format("https://www.youtube.com/watch?v=%s", trailer.getKey())));
        if (Utils.isAppInstalled(this, getString(R.string.youtube_app_name))) {
            i.setPackage(getString(R.string.youtube_app_name));
        }
        startActivity(i);
    }


    /**
     * CLICKING STOPPED WORKING after replacing <ScrollView> with <NestedScrollView>
     * this was used to open the full Review on the browser.
     */
    @Override
    public void onReviewItemClick(int position) {

        Review review = reviewList.get(position);
        Log.d(TAG, review.getUrl());
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(review.getUrl()));
        startActivity(i);

    }
}
