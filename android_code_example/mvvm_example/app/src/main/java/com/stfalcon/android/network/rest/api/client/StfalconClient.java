package com.stfalcon.android.network.rest.api.client;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stfalcon.android.BuildConfig;
import com.stfalcon.android.data.preferences.Preferences;
import com.stfalcon.android.network.rest.custom.CustomCallAdapterFactory;
import com.stfalcon.android.network.rest.custom.ItemTypeAdapterFactory;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by troy379 on 03.05.16.
 */
public class StfalconClient implements Interceptor {

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_LANGUAGE = "Accept-Language";
    public static final String AUTHORIZATION_TYPE = "Bearer ";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private Retrofit retrofit;
    private Object service;
    private Context context;

    public StfalconClient(Context context) {
        this.context = context;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(this)
                .addInterceptor(logging)
                .build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                .setDateFormat(DATE_FORMAT)
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL + BuildConfig.API_VERSION)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(new CustomCallAdapterFactory(stfalconApiCallback.class))
                .build();
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder()
                .url(original.url());

        builder = builder.header(HEADER_AUTHORIZATION,
                AUTHORIZATION_TYPE + Preferences.getManager().getAuthToken());;

        Request request = builder.build();

        return chain.proceed(request);
    }


    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> serviceClass) {
        if (service == null || !serviceClass.isInstance(service))
            service = retrofit.create(serviceClass);
        return (T) service;
    }
}
