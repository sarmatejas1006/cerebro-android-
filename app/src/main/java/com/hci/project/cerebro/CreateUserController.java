package com.hci.project.cerebro;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Malavika Ramprasad on 11/29/2017.
 */

public class CreateUserController implements Callback<CreateUser>{

    static  final String BASE_URL = "http://cerebro-api.herokuapp.com/api/";

    public void start() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(CreateUser.class, new CustomGsonAdapter.UserAdapter())
                .create();

        Retrofit.Builder builder = new Retrofit.Builder();

        Log.i("Controller", "Called");

        Retrofit retrofit = builder
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
//        builder.addConverterFactory(new JSONConverterFactory(gson));

        CreateUserAPI create_user = retrofit.create(CreateUserAPI.class);
//        CreateUser newUserReq = new CreateUser();


        //Call<UserToken> call = create_user.addPost(newUserReq);
//        call.enqueue(this);
    }

    public void onResponse(Call<CreateUser> create_user, Response<CreateUser> response )
    {
        if(response.isSuccessful()) {
            CreateUser cruser = (CreateUser) response.body();
            System.out.println("The name is : " + cruser.first_name + " " + cruser.last_name);
            System.out.println("Response Bodyyyyy : :: : " + response.toString());
            Log.i("Response Body : : : ", response.toString());
        } else {
            try {
                System.out.println(response.errorBody().string());
                Log.i("Error in Create User :", response.errorBody().string());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onFailure(Call<CreateUser> call, Throwable t) {
        t.printStackTrace();
    }
}

