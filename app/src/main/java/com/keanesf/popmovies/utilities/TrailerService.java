package com.keanesf.popmovies.utilities;

import com.keanesf.popmovies.models.Trailer;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TrailerService {

    @GET("3/movie/{movieId}/trailers")
    Call<Trailer> getTrailers(@Path("movieId") String movieId, @Query("api_key") String apiKey);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
