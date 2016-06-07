package com.stfalcon.android.network.rest.custom;

import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/**
 * Created by troy379 on 03.05.16.
 */
public class CustomCallAdapterFactory extends CallAdapter.Factory {

    private Class callbackClass;

    public CustomCallAdapterFactory(Class<? extends CustomCallback> callbackClass) {
        this.callbackClass = callbackClass;
    }

    @Override
    public CallAdapter<Call<?>> get(Type returnType, Annotation[] annotations,
                                    Retrofit retrofit) {
        TypeToken<?> token = TypeToken.get(returnType);

        if (token.getRawType() != Call.class) return null;
        if (!(returnType instanceof ParameterizedType))
            throw new IllegalStateException(
                    "Call must have generic type (e.g., Call<ResponseBody>)");

        final Type responseType = ((ParameterizedType) returnType).getActualTypeArguments()[0];
        return new CallAdapter<Call<?>>() {

            @Override
            public Type responseType() {
                return responseType;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <R> Call<R> adapt(retrofit2.Call<R> call) {
                return new CustomCallAdapter<>(call, callbackClass);
            }
        };
    }
}
