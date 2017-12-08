package com.hci.project.cerebro;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;

/**
 * Created by Malavika Ramprasad on 12/1/2017.
 */

public interface TutorRequestsAPI {
    @GET("current_user/{request}")
    Call<TutorRequests> getRequests(@HeaderMap Map<String, String> headers, @Path("request") String request);
}
