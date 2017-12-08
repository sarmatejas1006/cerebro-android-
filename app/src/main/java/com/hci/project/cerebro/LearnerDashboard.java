package com.hci.project.cerebro;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
public class LearnerDashboard extends android.support.v4.app.Fragment {

    public static List<SubmitQuestion> accepted;
    public static List<SubmitQuestion> pending;
    ListView lv_new, lv_old;

    public LearnerDashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learner_dashboard, container, false);

        lv_new = view.findViewById(R.id.newRequests);
        lv_old = view.findViewById(R.id.oldRequests);
        // Inflate the layout for this fragment
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setLenient()
                .create();
        final String BASE_URL = "http://cerebro-api.herokuapp.com/api/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("MyPref",0);
        String token = settings.getString("Current_User", "defaultvalue");
        Map<String, String> map = new HashMap<>();
        map.put("X-Authorization", token);
        TutorRequestsAPI requests_api = retrofit.create(TutorRequestsAPI.class);
        requests_api.getRequests(map,"learner_requests").enqueue(new Callback<TutorRequests>()
        {
            @Override
            public void onResponse(Call<TutorRequests> call, Response<TutorRequests> response) {
                if (response.isSuccessful()) {
                    System.out.println("Learner Response Requests :::" + response.body());
                    TutorRequests requests = response.body();
                    accepted = requests.accepted;
                    pending = requests.pending;
                }
                if(accepted.size() == 0) {
                    TextView textView1 = getActivity().findViewById(R.id.textView2);
                    textView1.setText("No New Requests");
                    ListView lv = getActivity().findViewById(R.id.newRequests);
                    lv.setVisibility(View.GONE);
                }
                else{
                    TutorListViewAdapter adapterAccepted = new TutorListViewAdapter(getActivity(), accepted);
                    lv_new.setAdapter(adapterAccepted);
                }
                lv_new.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Intent intent = new Intent(getActivity().getApplicationContext(), Rating.class);
                        view.getId();
                        SubmitQuestion lQuestion = (SubmitQuestion) lv_new.getItemAtPosition(i);
                        intent.putExtra("question_id", lQuestion.getId() );
                        intent.putExtra("questionType","accepted");
                        startActivity(intent);
                    }
                });
            }
            public void onFailure(Call<TutorRequests> call, Throwable t){
                t.printStackTrace();}
        });


        return view;

    }

}
