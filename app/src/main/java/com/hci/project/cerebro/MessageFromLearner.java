package com.hci.project.cerebro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MessageFromLearner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_from_learner);

        Intent intent = getIntent();
        //Log.i("Question Id", "This is the questionId : " + intent.getStringExtra("question_id"));
        final int question_id = intent.getIntExtra("question_id", 0);
        String question_type = intent.getStringExtra("questionType");

        Gson gson1 = new GsonBuilder()
                .setLenient()
                .create();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(CreateUser.class, new CustomGsonAdapter.UserAdapter())
                .setLenient()
                .create();

        final String BASE_URL = "http://cerebro-api.herokuapp.com/api/";
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyPref",0);
        String token = sp.getString("Current_User", "defaultvalue");
        final int user_id = sp.getInt("Current_User_Id", 0);

        final Map<String, String> map = new HashMap<>();
        map.put("X-Authorization", token);

        final Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GetQuestionAPI reqApi= retrofit1.create(GetQuestionAPI.class);
        reqApi.getQuestionDetails(map,question_id).enqueue(new Callback<SubmitQuestion>()
        {
            @Override
            public void onResponse(Call<SubmitQuestion> call, Response<SubmitQuestion> response)
            {
                if (response.isSuccessful()) {
                    System.out.println("response from accpt:" + response.body());
                    TextView textView10 = findViewById(R.id.textView100);
                    textView10.setText(response.body().getDescription());

                }
            }
            public void onFailure(Call<SubmitQuestion> call, Throwable t)
            {
                t.printStackTrace();
            }
        });

        Button bttnAccept = (Button) findViewById(R.id.button5);
        Button bttnReject = (Button) findViewById(R.id.button6);





        bttnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestDecisionAPI reqApi= retrofit.create(RequestDecisionAPI.class);
                reqApi.sendDecision(map,question_id,"accept").enqueue(new Callback<SubmitQuestion>()
                    {
                        @Override
                        public void onResponse(Call<SubmitQuestion> call, Response<SubmitQuestion> response)
                        {
                            if (response.isSuccessful()) {
                                System.out.println("response from accpt:" + response.body());
                                Intent intent = new Intent(MessageFromLearner.this, DrawerActivity.class);
                                intent.putExtra("RequestDecision","accept");
                                startActivity(intent);
                            }
                        }
                        public void onFailure(Call<SubmitQuestion> call, Throwable t)
                        {
                            t.printStackTrace();
                        }
                    });
            }
        });
        bttnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestDecisionAPI reqApi= retrofit.create(RequestDecisionAPI.class);
                reqApi.sendDecision(map,question_id,"reject").enqueue(new Callback<SubmitQuestion>()
                {
                    @Override
                    public void onResponse(Call<SubmitQuestion> call, Response<SubmitQuestion> response)
                    {
                        if (response.isSuccessful()) {
                            System.out.println("response from accpt:" + response.body());
                            Intent intent = new Intent(MessageFromLearner.this, DrawerActivity.class);
                            intent.putExtra("RequestDecision","reject");
                            startActivity(intent);
                        }
                    }
                    public void onFailure(Call<SubmitQuestion> call, Throwable t)
                    {
                        t.printStackTrace();
                    }
                });
            }
        });


    }
}
