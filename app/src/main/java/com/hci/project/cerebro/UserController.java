package com.hci.project.cerebro;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Malavika Ramprasad on 11/29/2017.
 */

public class UserController implements Callback<User> {
    static final String BASE_URL = "http://cerebro-api.herokuapp.com/api/";

    public void start() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();



//        Map<String, String> map = new HashMap<>();
//        map.put("X-Authorization", token);
//
        CerebroAPI cerebro_api = retrofit.create(CerebroAPI.class);


        //Call<User> call = cerebro_api.loadChanges();
        //call.enqueue(this);
    }


    @Override
    public void onResponse(Call<User> call, Response<User> response) {
        if(response.isSuccessful()) {
            User user = response.body();
            System.out.println("The name is : " + user.first_name + " " + user.last_name);
            Log.i("User Details", user.toString());
        } else {
            try {
                System.out.println(response.errorBody().string());
                Log.i("Error :", response.errorBody().string());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //System.out.println("Error : " + res)
            //Log.i("Error :", response.errorBody().string());
        }
    }

    @Override
    public void onFailure(Call<User> call, Throwable t) {
        t.printStackTrace();
    }

}
