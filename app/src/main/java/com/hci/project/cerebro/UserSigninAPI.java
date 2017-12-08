package com.hci.project.cerebro;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Malavika Ramprasad on 11/30/2017.
 */

public interface UserSigninAPI {
    @Headers("content_type: application/json")
    @POST("tokens")
    Call<UserToken> userSignin(@Body UserSignin userobj);
}
