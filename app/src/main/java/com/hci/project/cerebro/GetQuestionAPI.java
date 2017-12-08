package com.hci.project.cerebro;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;

/**
 * Created by Malavika Ramprasad on 12/5/2017.
 */

public interface GetQuestionAPI {

    @GET("questions/{id}")
    Call<SubmitQuestion> getQuestionDetails(@HeaderMap Map<String, String> headers, @Path("id") int id);
}
