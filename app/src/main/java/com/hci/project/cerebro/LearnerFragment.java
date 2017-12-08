package com.hci.project.cerebro;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class LearnerFragment extends Fragment implements View.OnClickListener {

    public static ArrayList<User> userList;
    public static SubmitQuestion question;
    public static SubmitQuestionResponse qResponse;

    public LearnerFragment() {
        // Required empty public constructor
    }

    public static interface LearnerListener {
        public void onLoad();
    }

    public LearnerListener listener1;

    Button btn;
    View rootView;
    public final String[] skillNames = new String[200];
    public final int[] skillID = new int[200];
    String token, fname, lname, start_time, end_time;
    int userId;
    float rating, x_coordinate, y_coordinate;
    public static String asked_topic;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_learner, container, false);
        getSkills();
        btn = (Button) rootView.findViewById(R.id.submit_question);
        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        token = settings.getString("Current_User", "defaultvalue");
        fname = settings.getString("Current_User_fName", "defaultvalue");
        lname = settings.getString("Current_User_lName", "defaultvalue");
        userId = settings.getInt("Current_User_Id", 0);
        rating = settings.getFloat("Current_User_rating", 0);
        x_coordinate = settings.getFloat("Current_User_x_coordinate", 0);
        y_coordinate = settings.getFloat("Current_User_y_coordinate", 0);
        start_time = settings.getString("Current_User_starttime", "defaultValue");
        end_time = settings.getString("Current_User_endtime", "defaultvalue");

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View rootView) {

                Log.i("SubmitButton", "Button1 Clicked");
                System.out.println("Button Clicked");
                View rv = rootView.getRootView();
                EditText topic = rv.findViewById(R.id.topic);
                EditText desc = rv.findViewById(R.id.description);
                String tag = topic.getText().toString();
                asked_topic = tag;
                String description = desc.getText().toString();
                int learner_id = userId;
                if(!tag.isEmpty() && !description.isEmpty()){
                registerQuestion(tag, description,learner_id);
                }
                //listTutors();
                Intent intent = new Intent(getActivity(), ListOfTutors.class);
            }
        });
        return rootView;
    }


    public void getSkills() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:sss")
                .setLenient()
                .create();
        final String BASE_URL = "http://cerebro-api.herokuapp.com/api/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        String token = settings.getString("Current_User", "defaultvalue");

        Map<String, String> map = new HashMap<>();
        map.put("X-Authorization", token);


        SkillAPI skill_api = retrofit.create(SkillAPI.class);

        skill_api.getSkills(map).enqueue(new Callback<List<Skill>>() {
            @Override
            public void onResponse(Call<List<Skill>> call, Response<List<Skill>> response) {
                if (response.isSuccessful()) {
                    System.out.println("Response SKILLS :::" + response.body());
                    List<Skill> arrayList = response.body();
                    int count = arrayList.size();
                    System.out.println(count);
                    int i;
                    ArrayList<SkillNameAdapter> list = new ArrayList<SkillNameAdapter>();
                    for (i=0; i<count-1; i++){
                        skillNames[i] = arrayList.get(i).getName();
                        list.add(new SkillNameAdapter(arrayList.get(i).getName()));
                        skillID[i] = arrayList.get(i).getId();
                        i++;
                    }
                    System.out.println("Name Array ::" + skillNames);
                    System.out.println("ID Array ::" + skillID);

                    AutoCompleteTextView tv = (AutoCompleteTextView) rootView.findViewById(R.id.topic);

                    ArrayAdapter<SkillNameAdapter> adapter = new ArrayAdapter<SkillNameAdapter>(
                            getContext(), android.R.layout.simple_dropdown_item_1line, list);
                    tv.setAdapter(adapter);
                    tv.setThreshold(1);
                }
            }

            @Override
            public void onFailure(Call<List<Skill>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void registerQuestion(String tag, String description, int learner_id) {
        int tag_id = 0;
        for (int i = 0; i < skillNames.length; i++) {
            String currentSkill = skillNames[i];
            System.out.println("Tag Name :: " + currentSkill);
            if (currentSkill.equalsIgnoreCase(tag)) {
                System.out.println("Tag Name :: " + skillNames[i]);
                int currTag = skillID[i];
                tag_id = currTag;
                break;
            }
        }
        if (!TextUtils.isEmpty(tag) && !TextUtils.isEmpty(description)) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
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


            SubmitQuestionAPI submitQn_api = retrofit.create(SubmitQuestionAPI.class);
            SubmitQuestion submitQuestion = new SubmitQuestion(tag_id, description, learner_id, 0);
            submitQn_api.addQuestion(map, submitQuestion).enqueue(new Callback<SubmitQuestionResponse>() {
                @Override
                public void onResponse(Call<SubmitQuestionResponse> call, Response<SubmitQuestionResponse> response) {

                    if (response.isSuccessful()) {
                        qResponse = (SubmitQuestionResponse) response.body();
                        question = qResponse.getQuestion();
                        userList = (ArrayList<User>) qResponse.getTutors();
                        //userList = (ArrayList<User>) response.body();
                        System.out.println("Response Body : :: : " + userList);
                        System.out.println("Token : :: : " + response.body());
                        Intent intent = new Intent(getActivity(), ListOfTutors.class);
//                        intent.putExtra("userList", userList);
                        //System.out.println(userList.get(0).email);
                        //intent.putParcelableArrayListExtra("userList", (ArrayList<? extends Parcelable>) userList);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<SubmitQuestionResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    public void onClick(View v) {
        Button submitQuestion = getActivity().findViewById(R.id.submit_question);
        submitQuestion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("SubmitButton", "Button1 Clicked");
            }
        });
    }

    public void onAttach() {
        Button submitQuestion = getActivity().findViewById(R.id.submit_question);
        submitQuestion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("SubmitButton", "Button1 Clicked");
            }
        });
    }

}
