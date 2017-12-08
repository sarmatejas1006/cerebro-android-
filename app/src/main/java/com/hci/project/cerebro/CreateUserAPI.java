package com.hci.project.cerebro;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.Call;

/**
 * Created by Malavika Ramprasad on 11/29/2017.
 */

public interface CreateUserAPI {
    @Headers("content_type: application/json")
    @POST("users")
    //Call<UserToken> addPost(@Body @Root("user") CreateUser userobj);
    Call<UserToken> addPost(@Body @Root("user") CreateUser userobj);
    }
