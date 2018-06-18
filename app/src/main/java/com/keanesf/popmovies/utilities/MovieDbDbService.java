package com.keanesf.popmovies.utilities;

import com.keanesf.popmovies.models.Movie;
import com.keanesf.popmovies.models.TmdbResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieDbDbService {

    @GET("3/movie/popular")
    Call<TmdbResponse<Movie>> listPopMovies(@Query("api_key") String apiKey);

    @GET("3/movie/top_rated")
    Call<TmdbResponse<Movie>> listTopMovies(@Query("api_key") String apiKey);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
