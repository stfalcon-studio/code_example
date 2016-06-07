package com.stfalcon.android.network.rest.custom;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

/**
 * Created by troy379 on 03.05.16.
 */
public class ItemTypeAdapterFactory implements TypeAdapterFactory {
    public <T> TypeAdapter<T> create(Gson gson, final TypeToken<T> type) {

        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);

        return new TypeAdapter<T>() {

            public void write(com.google.gson.stream.JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            public T read(com.google.gson.stream.JsonReader in) throws IOException {
                JsonElement jsonElement = elementAdapter.read(in);
                return delegate.fromJsonTree(jsonElement);
            }
        }.nullSafe();
    }
}


