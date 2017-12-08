package com.hci.project.cerebro;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

/**
 * Created by Malavika Ramprasad on 12/5/2017.
 */

public interface TutorReviewAPI {
    @POST("REVIEW")
        //Call<UserToken> addPost(@Body @Root("user") CreateUser userobj);
    Call<TutorReview> addreview(@HeaderMap Map<String, String> headers, @Body @Root("review") TutorReview reviewObj);
}
