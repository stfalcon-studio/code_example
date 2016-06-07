package com.stfalcon.android.network.rest.api.client;

import android.content.Context;

import com.stfalcon.android.data.models.responses.base.BaseResponse;
import com.stfalcon.android.network.Status;
import com.stfalcon.android.network.rest.custom.CustomCallback;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by troy379 on 13.05.16.
 */
public class StfalconApiCallback<T extends BaseResponse> extends CustomCallback<T> {

    public StfalconApiCallback(Context context, Call<T> call, CustomCallback.SuccessListener<T> onSuccessListener,
                               CustomCallback.FailureListener onFailureListener) {
        super(context, call, onSuccessListener, onFailureListener);
    }

    private void onUnauthorized() {
        refrashToken();
        getCall().clone().enqueue(this);
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            super.onResponse(call, response);
        } else {
            switch (Status.fromInt(response.code())) {
                case UNAUTHORIZED:
                    onUnauthorized();
                    break;
                default:
                    super.onFailure(call, new Throwable(response.message()));
            }
        }
    }
}
