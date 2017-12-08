package com.hci.project.cerebro;

import java.sql.Time;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HeaderMap;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Malavika Ramprasad on 12/4/2017.
 */

public interface RequestTutorAPI {
    @FormUrlEncoded
    @PUT("questions/{id}/request_tutor")
    Call<SubmitQuestion> reqTutor(@HeaderMap Map<String, String> headers, @Path("id") int id, @Field("tutor_id") int tutor_id, @Field("start_time") Time start_time, @Field("end_time") Time end_time);
}
