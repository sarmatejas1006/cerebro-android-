package com.hci.project.cerebro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.hci.project.cerebro.LearnerFragment.question;

public class TutorProfilePage extends AppCompatActivity {
    Button request, slot1, slot2, slot3;
    RatingBar rating;
    TextView skill_tv, tutor_name, skill_chosen;
    ListView skill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Boolean[] selectedFlag = {false};
        setContentView(R.layout.tutorprofilepage);
        int position=getIntent().getIntExtra("key_position",0);
        ArrayList<User> userList = LearnerFragment.userList;
        System.out.println(userList.get(position).email);
        final User user = userList.get(position);

        tutor_name = findViewById(R.id.tutor_name);
        rating = findViewById(R.id.rating);
        slot1 = findViewById(R.id.slot1);
        slot2 = findViewById(R.id.slot2);
        slot3 = findViewById(R.id.slot3);
        request = findViewById(R.id.request);
        skill_tv = findViewById(R.id.skill_tv);

        tutor_name.setText(user.first_name);
        rating.setRating(user.rating);
        skill_tv.setText("I can help you with your question on " + LearnerFragment.asked_topic + " at one of these times");
        skill_chosen = findViewById(R.id.time_chosen);

        //on selecting slot 1
        slot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFlag[0] = true;
                skill_chosen.setText("I'd like to request you to help me from:\n" + slot1.getText());
            }
        });

        //on selecting slot 2
        slot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFlag[0] = true;
                skill_chosen.setText("I'd like to request you to help me from:\n" + slot2.getText());
            }
        });

        //on selecting slot 3
        slot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFlag[0] = true;
                skill_chosen.setText("I'd like to request you to help me from:\n" + slot3.getText());
            }
        });

        //retrieve the data of the specific tutor at that position
        //and populate the tutorprofilepage
//        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
//        cal.set(user.booked_slots.get(0).getStart_time().get);
//        slot1.setText(String.valueOf(user.start_time))
//        slot1.setText(String.valueOf(user.start_time));

        Button sendRequest = (Button) findViewById(R.id.request);
        sendRequest.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View rootView) {
                if(selectedFlag[0] == false)
                {
                    Toast.makeText(getApplicationContext(),"Please Select a time slot", Toast.LENGTH_LONG).show();
                }
                else {
                    SharedPreferences sp = getApplication().getSharedPreferences("", 0);
                    String deviceToken = sp.getString("", "");

                    Gson gson = new GsonBuilder()
                            .setLenient()
                            .create();
                    final String BASE_URL = "http://cerebro-api.herokuapp.com/api/";
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();

                    SharedPreferences settings = getApplication().getApplicationContext().getSharedPreferences("MyPref", 0);
                    String usertoken = settings.getString("Current_User", "defaultvalue");
                    int userId = user.id;
                    int qId = question.id;
                    // Need to fetch later

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
                    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
//                    String start_time= dateFormat.format(calendar.getTime());
//                    String end_time = dateFormat.format(calendar.getTime());

//                    Time start_time = new java.sql.Time( cal.getTime().getTime() );
                    //Time end_time = new java.sql.Time( cal.getTime().getTime() );
//                    System.out.println(start_time);
//                    System.out.println(start_time);
                    Time start_time = new Time(503000837);
                    Time end_time = new Time(233300077);

                    Map<String, String> map = new HashMap<>();
                    map.put("X-Authorization", usertoken);
                    RequestTutorAPI reqtutor_api = retrofit.create(RequestTutorAPI.class);
                    //RequestTutor requestTutor = new RequestTutor(userId, start_time, end_time);
                    reqtutor_api.reqTutor(map, qId, userId, start_time, end_time ).enqueue(new Callback<SubmitQuestion>() {
                        @Override
                        public void onResponse(Call<SubmitQuestion> call, Response<SubmitQuestion> response) {
                            if (response.isSuccessful()) {
                                System.out.println("Request Submitted :: " + response.body());
                                Intent intent = new Intent(TutorProfilePage.this, DrawerActivity.class);
                                intent.putExtra("RequestDecision", "Y");
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<SubmitQuestion> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });

                }
            }
        });
        }
}
