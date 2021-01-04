package com.example.mymovies.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InterfaceAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class NetworkUtils {

    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie"; // request to network
    private static final String BASE_URL_VIDEO = "https://api.themoviedb.org/3/movie/%s/videos"; // request to net for videos
    private static final String BASE_URL_REVIEWS = "https://api.themoviedb.org/3/movie/%s/reviews"; // for reviews

    //parameters from url
    private static final String PARAMS_API_KEY = "api_key";
    private static final String PARAMS_LANGUAGE = "language";
    private static final String PARAMS_SORT_BY = "sort_by";
    private static final String PARAMS_PAGE = "page";
    private static final String PARAMS_MIN_VOTE_COUNT = "vote_count.gte";

    //values from parameters
    private static final String API_KEY = "de2b045baa608f0fcb7dba0ff4d52c0f";
    private static final String SORT_BY_POPULARITY = "popularity.desc";
    private static final String SORT_BY_TOP_RATED = "vote_average.desc";
    private static final String MIN_VOTE_COUNT_VALUE = "1000";

    public static final int POPULARITY = 0;
    public static final int TOP_RATED = 1;

    public static URL buildURLToReview(int id, String lang){
        Uri uri = Uri.parse(String.format(BASE_URL_REVIEWS, id)).buildUpon()
                .appendQueryParameter(PARAMS_LANGUAGE, lang)
                .appendQueryParameter(PARAMS_API_KEY, API_KEY).build();
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static URL buildURLToVideo(int id, String lang){
        Uri uri = Uri.parse(String.format(BASE_URL_VIDEO, id)).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, lang).build();
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //get url
    public static URL buildURL(int sortBy, int page, String lang) {   //get url
        URL result = null;
        String methodOfSort;
        if (sortBy == POPULARITY) {                   //check if sort by popularity
            methodOfSort = SORT_BY_POPULARITY;
        } else
            methodOfSort = SORT_BY_TOP_RATED;
        Uri uri = Uri.parse(BASE_URL).buildUpon() //get url to method uri
                // add parameters and values
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, lang)
                .appendQueryParameter(PARAMS_SORT_BY, methodOfSort)
                .appendQueryParameter(PARAMS_MIN_VOTE_COUNT, MIN_VOTE_COUNT_VALUE)
                .appendQueryParameter(PARAMS_PAGE, Integer.toString(page))
                .build();
        try {
            result = new URL(uri.toString()); // get URL result
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject getJSONForReviews (int id, String lang){  //get JSON data from network
        JSONObject result = null;
        URL url = buildURLToReview(id, lang); // get variables
        try {
            result = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject getJSONForVideos (int id, String lang){  //get JSON data from network
        JSONObject result = null;
        URL url = buildURLToVideo(id, lang); // get variables
        try {
            result = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject getJSONFromNetwork (int sortBy, int page, String lang){  //get JSON data from network
        JSONObject result = null;
        URL url = buildURL(sortBy, page, lang); // get variables
        try {
            result = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static class JSONLoader extends AsyncTaskLoader<JSONObject>{

        private Bundle bundle;

        private OnStartLoadingListener onStartLoadingListener;

        public interface OnStartLoadingListener{
            void OnStartLoading();
        }

        public void setOnStartLoadingListener(OnStartLoadingListener onStartLoadingListener) {
            this.onStartLoadingListener = onStartLoadingListener;
        }

        public JSONLoader(@NonNull Context context, Bundle bundle) {
            super(context);
            this.bundle = bundle;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if(onStartLoadingListener != null){
                onStartLoadingListener.OnStartLoading();
            }
            forceLoad();
        }

        @Nullable
        @Override
        public JSONObject loadInBackground() {
            if(bundle == null){
                return null;
            }
            String urlAsString = bundle.getString("url");
            URL url = null;
            try {
                url = new URL(urlAsString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            JSONObject result = null;
            if (url == null) {
                return result;
            }
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection(); //open connection
            InputStream inputStream = connection.getInputStream(); // inputstream
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream); // read inputStream
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine(); // read rows
            while (line != null){ // if line not null we add line to builder
                builder.append(line);
                line = reader.readLine();
            }
            result = new JSONObject(builder.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
            finally {
            if (connection != null){
                connection.disconnect(); // disconnect
            }
        }
            return result;
        }
    }

    private static class JSONLoadTask extends AsyncTask<URL, Void, JSONObject> { // load data with JSON

        @Override
        protected JSONObject doInBackground(URL... urls) { //method
            JSONObject result = null;
            if (urls == null || urls.length == 0) {
                return result;
            }
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) urls[0].openConnection(); //open connection
                InputStream inputStream = connection.getInputStream(); // inputstream
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream); // read inputStream
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder builder = new StringBuilder();
                String line = reader.readLine(); // read rows
                while (line != null){ // if line not null we add line to builder
                    builder.append(line);
                    line = reader.readLine();
                }
                result = new JSONObject(builder.toString());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            finally {
                if (connection != null){
                    connection.disconnect(); // disconnect
                }
            }
            return result;
        }
    }
}
