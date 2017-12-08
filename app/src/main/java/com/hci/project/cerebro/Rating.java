package com.hci.project.cerebro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Rating extends Activity {
    float tutRating = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        Intent intent = getIntent();
        final int question_id = intent.getIntExtra("question_id", 0);

        Gson gson1 = new GsonBuilder()
                .setLenient()
                .create();

        final String BASE_URL = "http://cerebro-api.herokuapp.com/api/";
        final Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson1))
                .build();

        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyPref",0);
        String token = sp.getString("Current_User", "defaultvalue");
        final int user_id = sp.getInt("Current_User_Id", 0);

        final Map<String, String> map = new HashMap<>();
        map.put("X-Authorization", token);

        GetQuestionAPI reqApi= retrofit1.create(GetQuestionAPI.class);
        reqApi.getQuestionDetails(map,question_id).enqueue(new Callback<SubmitQuestion>()
        {
            @Override
            public void onResponse(Call<SubmitQuestion> call, Response<SubmitQuestion> response)
            {
                if (response.isSuccessful()) {
                    System.out.println("response from accpt:" + response.body());
                    TextView view = findViewById(R.id.req_question);
                    view.setText(response.body().getDescription().toString());

                }
            }
            public void onFailure(Call<SubmitQuestion> call, Throwable t)
            {
                t.printStackTrace();
            }
        });



        //TextView tutorName = (TextView) findViewById(R.id.tutor);
        TextView req_question = (TextView) findViewById(R.id.req_question);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        //req_question.setText(qn.getDescription());
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                System.out.println("Rating Selected :: " + String.valueOf(rating));
                //txtRatingValue.setText(String.valueOf(rating));
                tutRating = rating;

            }
        });

        Button submitbutton = (Button) findViewById(R.id.submitbttn);

        submitbutton.setOnClickListener(new View.OnClickListener() {

            TextView reviewField = findViewById(R.id.editText);
            String review = reviewField.getText().toString();

            public void onClick(View v) {
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                final String BASE_URL = "http://cerebro-api.herokuapp.com/api/";
                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                TutorReviewAPI tutorRev_api = retrofit.create(TutorReviewAPI.class);
                TutorReview submitQuestion = new TutorReview(question_id, review, tutRating);
                Intent intent = new Intent(Rating.this,DrawerActivity.class);
                startActivity(intent);
                tutorRev_api.addreview(map,submitQuestion).enqueue(new Callback<TutorReview>() {
                    @Override
                    public void onResponse(Call<TutorReview> call, Response<TutorReview> response) {

                        if (response.isSuccessful()) {
                            System.out.println("Review Response :: " + response.body());
                            Intent intent = new Intent(Rating.this,DrawerActivity.class);
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onFailure(Call<TutorReview> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });

    }
}
