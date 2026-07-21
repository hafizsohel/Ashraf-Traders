package com.example.ashraftraders.data.api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://odaomcwsdaqsqdlxfpda.supabase.co/rest/v1/";
    private static final String SUPABASE_ANON_KEY = "sb_publishable_8GLmErLipG89oDKeIcEpcw_wnTuSvv_"; // আপনার আসল অ্যানন কি এখানে বসবে

    public static Retrofit getClient() {
        if (retrofit == null) {

            // সব রিকোয়েস্টের সাথে অটোমেটিক হেডার জুড়ে দেওয়ার ইন্টারসেপ্টর
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("Content-Type", "application/json")
                                .header("apikey", SUPABASE_ANON_KEY)
                                .header("Authorization", "Bearer " + SUPABASE_ANON_KEY);

                        return chain.proceed(requestBuilder.build());
                    })
                    // আপনার অলরেডি থাকা লগিং ইন্টারসেপ্টর (যদি থাকে)
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient) // ক্লায়েন্টটি সেট করা হলো
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}