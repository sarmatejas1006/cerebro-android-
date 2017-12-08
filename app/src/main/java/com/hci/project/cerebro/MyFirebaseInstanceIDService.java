package com.hci.project.cerebro;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    public MyFirebaseInstanceIDService() {
    }
    String refreshedToken="";
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        System.out.println("Device Token :: " + refreshedToken);

    }

    public void sendDeviceToken(String token, int user_id) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(CreateUser.class, new CustomGsonAdapter.UserAdapter())
                .setLenient()
                .create();
        final String BASE_URL = "http://cerebro-api.herokuapp.com/api/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        Map<String, String> map = new HashMap<>();
        map.put("X-Authorization", token);


        DeviceTokenAPI sendToken = retrofit.create(DeviceTokenAPI.class);
        DeviceToken sendDeviceToken = new DeviceToken(user_id, refreshedToken);
        sendToken.postDeviceToken(map, sendDeviceToken).enqueue(new Callback<DeviceToken>() {
            @Override
            public void onResponse(Call<DeviceToken> call, Response<DeviceToken> response) {
                if (response.isSuccessful()) {
                    System.out.println("Response from Token" + response.body());
                }
            }

            @Override
            public void onFailure(Call<DeviceToken> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
