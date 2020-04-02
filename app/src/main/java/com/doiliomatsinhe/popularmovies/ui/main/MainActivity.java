package com.doiliomatsinhe.popularmovies.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.doiliomatsinhe.popularmovies.R;
import com.doiliomatsinhe.popularmovies.adapter.MovieAdapter;
import com.doiliomatsinhe.popularmovies.databinding.ActivityMainBinding;
import com.doiliomatsinhe.popularmovies.model.Movie;
import com.doiliomatsinhe.popularmovies.model.MovieRepository;
import com.doiliomatsinhe.popularmovies.ui.detail.DetailActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, MovieAdapter.MovieItemClickListener {

    private MainViewModel viewModel;
    private MovieAdapter adapter;
    private ActivityMainBinding binding;
    private List<Movie> movieList = new ArrayList<>();
    public static final String MOVIE = "Movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initComponentes();

        initAdapter();

        decideWhatToShow();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String filter;
        switch (item.getItemId()) {
            case R.id.ic_popular:
                filter = getString(R.string.popular_filter);
                recoverMovies(filter);
                break;
            case R.id.ic_rating:
                filter = getString(R.string.top_rated_filter);
                recoverMovies(filter);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Gets the Movie from the API Using Separation of Concerns.
     * @param category is a filter used to know what list it will load.
     */
    private void recoverMovies(String category) {
        binding.swipeRefresh.setRefreshing(true);
        viewModel.setFilter(category);
        viewModel.getMovies(category).observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                adapter.setMovieList(movies);
                binding.swipeRefresh.setRefreshing(false);
                movieList = movies;

            }
        });
    }

    /**
     * Initialization of the Adapter, removed from onCreate for better Readability.
     */
    private void initAdapter() {
        adapter = new MovieAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.recyclerMovies.setLayoutManager(layoutManager);
        binding.recyclerMovies.setHasFixedSize(true);
        binding.recyclerMovies.setAdapter(adapter);
    }

    /**
     * Initialization of ViewModel.
     */
    private void initComponentes() {

        binding.swipeRefresh.setOnRefreshListener(this);

        // Initializing the repository, factory and the ViewModel
        MovieRepository repository = new MovieRepository(getString(R.string.api_key));
        MainViewModelFactory factory = new MainViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);
    }

    @Override
    public void onRefresh() {
        decideWhatToShow();
    }

    /**
     * Method to decide what list to show after rotation has occurred.
     */
    private void decideWhatToShow() {
        if (viewModel.getFilter() == null) {
            viewModel.setFilter(getString(R.string.popular_filter));
            recoverMovies(viewModel.getFilter());
        } else {
            recoverMovies(viewModel.getFilter());
        }
        if (viewModel.getFilter().equalsIgnoreCase(getString(R.string.popular_filter))) {
            recoverMovies(getString(R.string.popular_filter));
        } else {
            recoverMovies(getString(R.string.top_rated_filter));
        }
    }

    @Override
    public void onMovieItemClick(int position) {
        Movie selectedMovie = movieList.get(position);
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra(MOVIE, selectedMovie);
        startActivity(i);
    }
}
