package com.hci.project.cerebro;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by Malavika Ramprasad on 12/2/2017.
 */

public interface PostSkillsAPI {
    @PUT("current_user/add_skills")
    Call<User> postSkills(@HeaderMap Map<String, String> headers,@Body @Root("user_skills") PostSkills skillObj);
}
