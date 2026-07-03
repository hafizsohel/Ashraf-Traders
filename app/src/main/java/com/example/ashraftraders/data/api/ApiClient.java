package com.example.ashraftraders.data.api;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;

    public static Retrofit getClient() {

        if (retrofit == null) {

            HttpLoggingInterceptor logging =
                    new HttpLoggingInterceptor();

            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()

                    .addInterceptor(logging)

                    .addInterceptor((Interceptor) chain -> {

                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("apikey", ApiConstants.API_KEY)
                                .addHeader("Authorization", "Bearer " + ApiConstants.API_KEY)
                                .addHeader("Accept", "application/json")
                                .build();

                        return chain.proceed(request);

                    })

                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiConstants.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

}