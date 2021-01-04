package com.example.mymovies.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MoviesDao {
    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> getAllMovies(); //request to database

    @Query("SELECT * FROM favorite_movies")
    LiveData<List<FavouriteMovie>> getAllFavoriteMovies(); //request to database

    @Query("SELECT * FROM movies WHERE id == :movieId") // request each movie with id
    Movie getMovieById(int movieId);

    @Query("SELECT * FROM favorite_movies WHERE id == :movieId") // request each movie with id
    FavouriteMovie getFavouriteMovieById(int movieId);

    @Query("DELETE FROM movies") //method to delete all movies in db
    void deleteAllMovies();

    @Insert
    void insertMovie(Movie movie); //insert to db

    @Delete
    void deleteMovie(Movie movie); //delete in db

    @Insert
    void insertFavouriteMovie(FavouriteMovie movie); //insert to db

    @Delete
    void deleteFavouriteMovie(FavouriteMovie movie); //delete in db
}
