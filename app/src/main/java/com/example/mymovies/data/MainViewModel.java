package com.example.mymovies.data;

import android.app.Application;
import android.app.ListActivity;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainViewModel extends AndroidViewModel {

    private static MovieDatabase database; //create object to db
    private LiveData<List<Movie>> movies; // create list of movies
    private LiveData<List<FavouriteMovie>> favouriteMovies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        database = MovieDatabase.getInstance(getApplication());
        movies = database.moviesDao().getAllMovies();
        favouriteMovies = database.moviesDao().getAllFavoriteMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public Movie getMovieById(int id){ //get back object movie
        try {
            return new GetMovieTask().execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public FavouriteMovie getFavouriteMovieById(int id){ //get back object movie
        try {
            return new GetFavouriteMovieTask().execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LiveData<List<FavouriteMovie>> getFavouriteMovies() {
        return favouriteMovies;
    }

    public void deleteAllMovies(){   ///method to delete all movies
        new DeleteMovieTask().execute();
    }
    public void insertMovie(Movie movie){
        new InsertMovieTask().execute(movie); //insert movie
    }

    public void deleteMovie(Movie movie){
        new DeleteTask().execute(movie); //delete movie
    }

    public void insertFavouriteMovie(FavouriteMovie movie){
        new InsertFavouriteMovieTask().execute(movie); //insert movie
    }

    public void deleteFavouriteMovie(FavouriteMovie movie){
        new DeleteFavouriteTask().execute(movie); //delete movie
    }

    private static class DeleteFavouriteTask extends AsyncTask<FavouriteMovie, Void, Void>{ //task for deleteMovie

        @Override
        protected Void doInBackground(FavouriteMovie... movies) {
            if(movies != null && movies.length > 0){
                database.moviesDao().deleteFavouriteMovie(movies[0]);
            }
            return null;
        }
    }

    private static class InsertFavouriteMovieTask extends AsyncTask<FavouriteMovie, Void, Void>{//task for insertMovie

        @Override
        protected Void doInBackground(FavouriteMovie... movies) {
            if(movies != null && movies.length > 0){
                database.moviesDao().insertFavouriteMovie(movies[0]);
            }
            return null;
        }
    }

    private static class DeleteTask extends AsyncTask<Movie, Void, Void>{ //task for deleteMovie

        @Override
        protected Void doInBackground(Movie... movies) {
            if(movies != null && movies.length > 0){
                database.moviesDao().deleteMovie(movies[0]);
            }
            return null;
        }
    }

    private static class InsertMovieTask extends AsyncTask<Movie, Void, Void>{//task for insertMovie

        @Override
        protected Void doInBackground(Movie... movies) {
            if(movies != null && movies.length > 0){
                database.moviesDao().insertMovie(movies[0]);
            }
            return null;
        }
    }

    private static class DeleteMovieTask extends AsyncTask<Void, Void, Void> {//task for deleteAllMovies

        @Override
        protected Void doInBackground(Void... integers) {
                database.moviesDao().deleteAllMovies();
            return null;
        }
    }

    private static class GetMovieTask extends AsyncTask<Integer, Void, Movie>{//task for getMovieById

        @Override
        protected Movie doInBackground(Integer... integers) {
            if(integers != null && integers.length > 0){
                return  database.moviesDao().getMovieById(integers[0]);
            }
            return null;
        }
    }
    private static class GetFavouriteMovieTask extends AsyncTask<Integer, Void, FavouriteMovie>{//task for getMovieById

        @Override
        protected FavouriteMovie doInBackground(Integer... integers) {
            if(integers != null && integers.length > 0){
                return  database.moviesDao().getFavouriteMovieById(integers[0]);
            }
            return null;
        }
    }
}
