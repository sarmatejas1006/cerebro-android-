package com.hci.project.cerebro;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;

/**
 * Created by Malavika Ramprasad on 11/30/2017.
 */

public interface SkillAPI {
    //@Headers("X-Authorization: eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoidnBhbmRpMkB1aWMuZWR1IiwiZXhwIjoxNTEyMjQ1NzY1fQ.pp_w7vKTcA6OTHDmifHAIXc9QCrt7O9GzwO66JhynRQ")
    @GET("skills")
    Call<List<Skill>> getSkills(@HeaderMap Map<String, String> headers);
}
