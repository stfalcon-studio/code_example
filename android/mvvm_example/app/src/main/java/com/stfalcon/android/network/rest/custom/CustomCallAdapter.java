package com.stfalcon.android.network.rest.custom;

import android.content.Context;

import java.io.IOException;
import java.lang.reflect.Constructor;

import okhttp3.Request;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by troy379 on 03.05.16.
 */
class CustomCallAdapter<T> implements Call<T> {

    private final retrofit2.Call<T> delegate;
    private Class callbackClass;

    CustomCallAdapter(retrofit2.Call<T> call, Class<? extends CustomCallback> callbackClass) {
        this.delegate = call;
        this.callbackClass = callbackClass;
    }

    @Override
    public Response<T> execute() throws IOException {
        return delegate.execute();
    }

    @Override
    public void enqueue(Callback<T> callback) {
        delegate.enqueue(callback);
    }

    @Override
    public void enqueue(Context context, CustomCallback.SuccessListener<T> successListener) {
        enqueue(context, successListener, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void enqueue(Context context, CustomCallback.SuccessListener<T> successListener,
                        CustomCallback.FailureListener failureListener) {
        try {
            Constructor constructor = callbackClass.getDeclaredConstructor(
                    Context.class,
                    retrofit2.Call.class,
                    CustomCallback.SuccessListener.class,
                    CustomCallback.FailureListener.class);

            constructor.setAccessible(true);
            CustomCallback callback = (CustomCallback)constructor
                    .newInstance(context, delegate, successListener, failureListener);

            enqueue(callback);
        } catch (Exception ignore) { }
    }

    @Override
    public boolean isExecuted() {
        return delegate.isExecuted();
    }

    @Override
    public void cancel() {
        delegate.cancel();
    }

    @Override
    public boolean isCanceled() {
        return delegate.isCanceled();
    }

    @SuppressWarnings("all")
    @Override
    public Call<T> clone() {
        return new CustomCallAdapter<>(delegate.clone(), callbackClass);
    }

    @Override
    public Request request() {
        return delegate.request();
    }
}