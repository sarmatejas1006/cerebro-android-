package com.hci.project.cerebro;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by Malavika Ramprasad on 11/29/2017.
 */

public interface CerebroAPI {
    //@Headers("X-Authorization: eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoidnBhbmRpMkB1aWMuZWR1IiwiZXhwIjoxNTEyMjQ1NzY1fQ.pp_w7vKTcA6OTHDmifHAIXc9QCrt7O9GzwO66JhynRQ")
    @GET("current_user")
    //Call<List<Change>> loadChanges(@Query("q") String status);
    Call<User> loadChanges(@HeaderMap Map<String, String> headers);
}