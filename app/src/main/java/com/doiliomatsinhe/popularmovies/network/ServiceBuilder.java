package com.doiliomatsinhe.popularmovies.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceBuilder {

    // Create a Logger
    private static HttpLoggingInterceptor logger = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    // Create Http Client
    private static OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder().addInterceptor(logger);

    private static final String BASE_URL = "https://api.themoviedb.org/";

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient.build());

    private static Retrofit retrofit = builder.build();

    public static <Doilio> Doilio BuildService(Class<Doilio> serviceType) {
        return retrofit.create(serviceType);
    }
}
