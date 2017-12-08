package com.hci.project.cerebro;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by Malavika Ramprasad on 12/3/2017.
 */

public interface DeviceTokenAPI {
    @POST("devices")
    Call<DeviceToken> postDeviceToken(@HeaderMap Map<String, String> headers, @Body @Root("devices") DeviceToken deviceObj);
}
