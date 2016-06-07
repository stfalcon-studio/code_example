package com.stfalcon.android.network.rest.api.services;

import com.stfalcon.android.data.models.bodies.ProfileBody;
import com.stfalcon.android.data.models.responses.profile.ProfileFormErrorsResponse;
import com.stfalcon.android.data.models.responses.profile.ProfileEditResponse;
import com.stfalcon.android.data.models.responses.profile.ProfileResponse;
import com.stfalcon.android.network.rest.custom.Call;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

/**
 * Created by troy379 on 03.05.16.
 */
public interface ProfileService {

    @GET("profile")
    Call<ProfileResponse> getCurrent();


    @PUT("profile")
    Call<ProfileFormErrorsResponse> save(
            @Body ProfileBody profileBody
    );


    @GET("profile/edit")
    Call<ProfileEditResponse> getForEdit();
}
