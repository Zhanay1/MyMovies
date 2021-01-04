package com.example.mymovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovies.R;
import com.example.mymovies.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private OnPosterClickListener onPosterClickListener; // object of interface
    private onReachEndListener onReachEndListener;

    public MovieAdapter(){
        movies = new ArrayList<>(); // empty constuctor for arrayList
    }

    public interface OnPosterClickListener{
        void onPosterClick(int position); //to click the poster image
    }

    public void setOnPosterClickListener(OnPosterClickListener onPosterClickListener) { //setter
        this.onPosterClickListener = onPosterClickListener;
    }

    public interface onReachEndListener{
        void onReachEnd(); // method call when we come end of page
    }

    public void setOnReachEndListener(MovieAdapter.onReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //to create viewholder from movie_item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if ( movies.size() >= 20 && position > movies.size() - 2 && onReachEndListener != null){///when we come end of page and onReachEndListener not null
            onReachEndListener.onReachEnd();
        }
        Movie movie = movies.get(position);
        ImageView imageView = holder.imageViewSmallPoster; //get imageview
        Picasso.get().load(movie.getPosterPath()).into(imageView); // download  image
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder { // class for imageView
        private ImageView imageViewSmallPoster;
        public MovieViewHolder(@NonNull View itemView) { //constructor
            super(itemView);
            imageViewSmallPoster = itemView.findViewById(R.id.imageViewSmallPoster); // get a value
            itemView.setOnClickListener(new View.OnClickListener() { // when the click imege
                @Override
                public void onClick(View v) {
                    if(onPosterClickListener != null){
                        onPosterClickListener.onPosterClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public void clear(){
        this.movies.clear();
        notifyDataSetChanged();
    }
    //for add new ArrayList
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
    //we can add values this ArrayList
    public void addMovies(List<Movie> movies){
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public List<Movie> getMovies() {
        return movies;
    }
}
