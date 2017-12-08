package com.hci.project.cerebro;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Malavika Ramprasad on 12/4/2017.
 */

public interface RequestDecisionAPI {
    @PUT("questions/{id}/{decision}")
    Call<SubmitQuestion> sendDecision(@HeaderMap Map<String, String> headers, @Path("id") int id, @Path("decision") String decision);
}
