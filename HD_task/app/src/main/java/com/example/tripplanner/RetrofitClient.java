package com.example.tripplanner;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:5000/";
    private static Retrofit retrofit;

    static {
        // Logging interceptor for debugging
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Build OkHttpClient with extended timeouts
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)   // increase connect timeout
                .readTimeout(30, TimeUnit.SECONDS)      // increase read timeout
                .writeTimeout(30, TimeUnit.SECONDS)     // increase write timeout
                .addInterceptor(logging)
                .build();

        // Build Retrofit instance
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiService getApiService() {
        return retrofit.create(ApiService.class);
    }
}
