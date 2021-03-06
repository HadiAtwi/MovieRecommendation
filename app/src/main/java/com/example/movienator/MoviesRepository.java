package com.example.movienator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

// MoviesRepository Singleton
public class MoviesRepository {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String LANGUAGE = "en-US";

    private static MoviesRepository repository;

    private TMDbApi api;

    private MoviesRepository(TMDbApi api) {
        this.api = api;
    }

    public static MoviesRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new MoviesRepository(retrofit.create(TMDbApi.class));
        }

        return repository;
    }

    public void getMovies(int page, final OnGetMoviesCallback callback) {
        Log.d("MoviesRepository", "Next Page = " + page);
        api.getPopularMovies(BuildConfig.API_KEY, LANGUAGE, page)
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                        if (response.isSuccessful()) {
                            MoviesResponse moviesResponse = response.body();
                            if (moviesResponse != null && moviesResponse.getMovies() != null) {
                                callback.onSuccess(moviesResponse.getPage(), moviesResponse.getMovies());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getGenres(final OnGetGenresCallback callback) {
        api.getGenres(BuildConfig.API_KEY, LANGUAGE)
                .enqueue(new Callback<GenresResponse>() {
                    @Override
                    public void onResponse(Call<GenresResponse> call, Response<GenresResponse> response) {
                        if (response.isSuccessful()) {
                            GenresResponse genresResponse = response.body();
                            if (genresResponse != null && genresResponse.getGenres() != null) {
                                callback.onSuccess(genresResponse.getGenres());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<GenresResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }
}


//
//public class MoviesRepository {
//
//    private static final String BASE_URL = "https://api.themoviedb.org/3/";
//    private static final String LANGUAGE = "en-US";
//
//    private static MoviesRepository repository;
//
//    private TMDbApi api;
//
//    private MoviesRepository(TMDbApi api) {
//        this.api = api;
//    }
//
//    public static MoviesRepository getInstance() {
//        if (repository == null) {
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//
//            repository = new MoviesRepository(retrofit.create(TMDbApi.class));
//        }
//
//        return repository;
//    }
//
//    public void getMovies(final OnGetMoviesCallback callback) {
//        api.getPopularMovies("39c69a10c366d0d25de6fec34b2cf1bc", LANGUAGE, 1)
//                .enqueue(new Callback<MoviesResponse>() {
//                    @Override
//                    public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
//                        if (response.isSuccessful()) {
//                            MoviesResponse moviesResponse = response.body();
//                            if (moviesResponse != null && moviesResponse.getMovies() != null) {
//                                callback.onSuccess(moviesResponse.getMovies());
//                            } else {
//                                callback.onError();
//                            }
//                        } else {
//                            callback.onError();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
//                        callback.onError();
//                    }
//                });
//    }
//
//}
