package com.hci.project.cerebro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LearnerFragment.LearnerListener {

    public static double latitude;
    public static double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        // Start - Defaulting the application with Learner fragment -
        String flag = getIntent().getStringExtra("RequestDecision");
        if(flag == null) {
            LearnerFragment fragment1 = new LearnerFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.frag_frame, fragment1);
            fragmentTransaction.commit();
            hideLearnerItem();

        } else if(flag.equals("accept")){
            TutorFragment fragment1 = new TutorFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.frag_frame, fragment1);
            fragmentTransaction.commit();
            hideTutorItem();
            Toast.makeText(getApplicationContext(),"You have accepted the request. The learner will be notified", Toast.LENGTH_LONG).show();
        }
        else if(flag.equals("reject")){
            TutorFragment fragment1 = new TutorFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.frag_frame, fragment1);
            fragmentTransaction.commit();
            hideTutorItem();
            Toast.makeText(getApplicationContext(),"You have rejected the request. The learner will be notified", Toast.LENGTH_LONG).show();
        }

        else {
            LearnerFragment fragment1 = new LearnerFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.frag_frame, fragment1);
            fragmentTransaction.commit();
            hideTutorItem();
            Toast.makeText(getApplicationContext(),"Your request has been sent to the Tutor. You will get a notifictaion if the tutor accepts your request", Toast.LENGTH_LONG).show();
        }
        // End

        //fetch device token
        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyPref",0);
        String token = sp.getString("Current_User", "defaultvalue");
        int user_id = sp.getInt("Current_User_Id", 0);
        MyFirebaseInstanceIDService id = new MyFirebaseInstanceIDService();
        id.onCreate();
        id.onTokenRefresh();
        id.sendDeviceToken(token,user_id);
        //End

        //fetch user details
        fetchUserDetails();
        //end

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set user name and email in hamburger menu
        SharedPreferences settings = getApplicationContext().getSharedPreferences("MyPref",0);
        String fname = settings.getString("Current_User_fName", "defaultvalue");
        String lname = settings.getString("Current_User_lName", "defaultvalue");
        String email = settings.getString("Current_User_email", "defaultvalue");
        String username = fname + " " + lname;

        //End
        //Fetch user location
        FusedLocationProviderClient mFusedLocationClient;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                });
        //End


    }
    NavigationView navigationView;

    private void fetchUserDetails()
    {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .setLenient()
                .create();
        final String BASE_URL = "http://cerebro-api.herokuapp.com/api/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        SharedPreferences settings = getApplicationContext().getSharedPreferences("MyPref",0);
        String token = settings.getString("Current_User", "defaultvalue");

        Map<String, String> map = new HashMap<>();
        map.put("X-Authorization", token);

        CerebroAPI cerebro_api = retrofit.create(CerebroAPI.class);

        cerebro_api.loadChanges(map).enqueue(new Callback<User>()
        {
            @Override
            public void onResponse(Call<User> call, Response<User> response)
            {
                if (response.isSuccessful()) {
                    System.out.println("Fetching User details :: " + response.toString());
                    String first_name = response.body().first_name;
                    String last_name = response.body().last_name;
                    String emailID = response.body().email;
                    int userID = response.body().id;
                    float rating = response.body().rating;
                    float x_coordinate = response.body().x_coordinate;
                    float y_coordinate = response.body().y_coordinate;
//                    Time start_time = response.body().start_time;
//                    Time end_time = response.body().end_time;
                    String start_time_string =" ";
                    String end_time_string = " ";
//                    if(start_time != null){
//                        start_time_string = start_time.toString();
//                    }
//                    if(end_time != null){
//                        end_time_string = end_time.toString();
//                    }
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    //on the login store the login
                    editor.putString("Current_User_fName", first_name);
                    editor.putInt("Current_User_Id", userID);
                    editor.putString("Current_User_lName", last_name);
                    editor.putString("Current_User_email", emailID);
                    editor.putFloat("Current_User_rating", rating);
                    editor.putFloat("Current_User_x_coordinate", x_coordinate);
                    editor.putFloat("Current_User_y_coordinate", y_coordinate);
                    editor.putString("Current_User_starttime",start_time_string );
                    editor.putString("Current_User_endtime", end_time_string);
                    editor.commit();
                    TextView txtProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tab_username);
                    TextView txtProfileEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tab_email);
                    String username = first_name + " " + last_name;
                    txtProfileName.setText(username);
                    txtProfileEmail.setText(emailID);
                }
            }
            public void onFailure(Call<User> call, Throwable t)
            {
                t.printStackTrace();
            }
        });
    }


    private void hideLearnerItem() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_learner).setVisible(false);
        nav_Menu.findItem(R.id.nav_tutor).setVisible(true);
    }
    private void hideTutorItem() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_learner).setVisible(true);
        nav_Menu.findItem(R.id.nav_tutor).setVisible(false);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void onLoad() {
        Button submitQuestion = (Button) findViewById(R.id.submit_question);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
//    LearnerFragment fragment1 = new LearnerFragment();
//    TutorFragment fragment2 = new TutorFragment();

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        Log.i("NavSelected :", "Inside");
        int id = item.getItemId();
        LearnerFragment fragment1 = new LearnerFragment();
        TutorFragment fragment2 = new TutorFragment();
        LearnerDashboard fragment3 = new LearnerDashboard();
        if (id == R.id.nav_learner) {
            // Handle the camera action
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frag_frame, fragment1);
            hideLearnerItem();
            fragmentTransaction.commit();

        } else if (id == R.id.nav_tutor) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frag_frame, fragment2);
            hideTutorItem();
            fragmentTransaction.commit();

        } else if (id == R.id.nav_rating) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frag_frame, fragment3);
            //hideTutorItem();
            fragmentTransaction.commit();

        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(DrawerActivity.this,Settings.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
                SharedPreferences myPrefs = getApplicationContext().getSharedPreferences("MyPref",0);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.clear();
                editor.commit();
                //AppState.getSingleInstance().setLoggingOut(true);
                setLoginState(true);
                Log.d("Logout ?? ", "Now log out and start the activity login");
                Intent intent = new Intent(DrawerActivity.this,
                        LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Logout","Y");
                startActivity(intent);
                finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void setNavigationViewListner() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void setLoginState(boolean status) {
        SharedPreferences sp = getSharedPreferences("LoginState",
                MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("setLoggingOut", status);
        ed.commit();
    }
}
