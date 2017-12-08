package com.hci.project.cerebro;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TutorProfileActivity extends AppCompatActivity {

    //SKILLS Variables
    EditText skill1, skill2, skill3, skill4, skill5, skill6;
    List<String> skills;
    Button from_time, till_time;
    Button go_location, skip;
    private int mHour, mMinute;
    private Time startTime, endTime;
    String sflag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);
        Button updateBttn= findViewById(R.id.updateBttn);
        sflag = getIntent().getStringExtra("SettingView");
        if(sflag == null){updateBttn.setVisibility(View.GONE);}
        else{
        if(!sflag.equalsIgnoreCase("Y"))
        {
            updateBttn.setVisibility(View.GONE);
        }
        else
        {
            Button skip= findViewById(R.id.skip);
            skip.setVisibility(View.GONE);
            Button location = findViewById(R.id.go_location);
            location.setVisibility(View.GONE);
            TextView tv = findViewById(R.id.textView6);
            tv.setText("Update your skills");
        }}
        //SKILLS
        skill1 = findViewById(R.id.skill1);
        skill2 = findViewById(R.id.skill2);
        skill3 = findViewById(R.id.skill3);
        skill4 = findViewById(R.id.skill4);
        skill5 = findViewById(R.id.skill5);
        skill6 = findViewById(R.id.skill6);

        //TIMING PREFERENCES
        from_time = findViewById(R.id.from_time);
        till_time = findViewById(R.id.till_time);

        //Go to choose location
        go_location = findViewById(R.id.go_location);
        go_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call Retrofit
                updateUserSkills();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("UserTime", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                //on the login store the login
                if (startTime != null && endTime != null) {
                    editor.putString("StartTime", startTime.toString());
                    editor.putString("EndTime", endTime.toString());
                    editor.commit();
                }
            }
        });

        //SKIP to dashboard
        skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TutorProfileActivity.this, DrawerActivity.class);
                startActivity(intent);
            }
        });
        updateBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserSkills();
            }
        });

    }

    private void updateUserSkills(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .registerTypeAdapter(CreateUser.class, new CustomGsonAdapter.UserAdapter())
                .setLenient()
                .create();
        final String BASE_URL = "http://cerebro-api.herokuapp.com/api/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        //get skills and add to ArrayList
        skills = new ArrayList<>();
        if(!skill1.getText().toString().isEmpty())
        {
            skills.add(skill1.getText().toString());
        }
        if(!skill2.getText().toString().isEmpty())
        {
            skills.add(skill2.getText().toString());
        }
        if(!skill3.getText().toString().isEmpty())
        {
            skills.add(skill3.getText().toString());
        }
        if(!skill4.getText().toString().isEmpty())
        {
            skills.add(skill4.getText().toString());
        }
        if(!skill5.getText().toString().isEmpty())
        {
            skills.add(skill5.getText().toString());
        }
        if(!skill6.getText().toString().isEmpty()) {
            skills.add(skill6.getText().toString());
        }
        // Logic to check emptines sof skills
        if(skills.isEmpty() || startTime ==null || endTime==null)
        {
            if(sflag!=null) {
                if (!sflag.equalsIgnoreCase("Y")) {
                    new AlertDialog.Builder(this).setTitle("Warning!!").setMessage("Please enter atleast 1 skill and select your favourable time or click skip button to update later").setNeutralButton("Close", null).show();
                } else {
                    if(skills.isEmpty() && startTime ==null && endTime==null){
                        new AlertDialog.Builder(this).setTitle("Warning!!").setMessage("Please enter atleast 1 skill and select your favourable time").setNeutralButton("Close", null).show();
                    }
                    else
                    {
                        SharedPreferences settings = getApplicationContext().getSharedPreferences("MyPref", 0);
                        String token = settings.getString("Current_User", "defaultvalue");
                        Map<String, String> map = new HashMap<>();
                        map.put("X-Authorization", token);

                        PostSkillsAPI submitQn_api = retrofit.create(PostSkillsAPI.class);
                        PostSkills submitQuestion = new PostSkills(skills);
                        submitQn_api.postSkills(map, submitQuestion).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {

                                if (response.isSuccessful()) {
                                    User changesList = response.body();
                                    System.out.println("Response Bodyyyyy : :: : " + changesList);
                                    Intent intent;
                                    if(sflag!=null) {
                                        if (!sflag.equalsIgnoreCase("Y")) {
                                            intent = new Intent(TutorProfileActivity.this, TutoringLocationActivity.class);
                                        } else {
                                            intent = new Intent(TutorProfileActivity.this, Settings.class);
                                        }
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        intent = new Intent(TutorProfileActivity.this, TutoringLocationActivity.class);
                                        startActivity(intent);
                                    }

                                }
                            }
                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    }
                }
            }
            else
            {
                new AlertDialog.Builder(this).setTitle("Warning!!").setMessage("Please enter atleast 1 skill and select your favourable time").setNeutralButton("Close", null).show();
            }

        }
        else {
            SharedPreferences settings = getApplicationContext().getSharedPreferences("MyPref", 0);
            String token = settings.getString("Current_User", "defaultvalue");
            Map<String, String> map = new HashMap<>();
            map.put("X-Authorization", token);

            PostSkillsAPI submitQn_api = retrofit.create(PostSkillsAPI.class);
            PostSkills submitQuestion = new PostSkills(skills);
            submitQn_api.postSkills(map, submitQuestion).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    if (response.isSuccessful()) {
                        User changesList = response.body();
                        System.out.println("Response Bodyyyyy : :: : " + changesList);
                        Intent intent;
                        if(sflag!=null) {
                            if (!sflag.equalsIgnoreCase("Y")) {
                                intent = new Intent(TutorProfileActivity.this, TutoringLocationActivity.class);
                            } else {
                                intent = new Intent(TutorProfileActivity.this, Settings.class);
                            }
                            startActivity(intent);
                        }
                        else
                        {
                            intent = new Intent(TutorProfileActivity.this, TutoringLocationActivity.class);
                            startActivity(intent);
                        }

                    }
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    public void onClick(View v) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
        if (v==from_time) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            from_time.setText(hourOfDay + ":" + minute);
                            startTime = new Time(hourOfDay, minute,0);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if (v==till_time) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            till_time.setText(hourOfDay + ":" + minute);
                            endTime = new Time(hourOfDay,minute,0);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
}