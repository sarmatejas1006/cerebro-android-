package com.hci.project.cerebro;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TutoringLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double latitude, longitude;
    Button conf_location;
    TextView location_prompt, location_details;
    private FusedLocationProviderClient mFusedLocationClient;
    Time starttime, endtime;
    private double lat, lng;
    private String sflag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutoring_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserTime",0);
        String start_time = sp.getString("StartTime", "defaultvalue");
        String end_time = sp.getString("EndTime", "defaultvalue");
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        try {
            starttime = new java.sql.Time(formatter.parse(start_time).getTime());
            System.out.println("Start Time ::" + starttime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            endtime = new java.sql.Time(formatter.parse(end_time).getTime());
            System.out.println("Start Time ::" + endtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sflag = getIntent().getStringExtra("SettingView");
        Button location = (Button) findViewById(R.id.conf_location);
        if(sflag == null){}
        else
        {
        if(!sflag.equalsIgnoreCase("Y"))
        {
            location.setText("CONFIRM LOCATION");
        }
        else
        {
            location.setText("UPDATE LOCATION");
            TextView tc = findViewById(R.id.location_prompt);
            tc.setText("Update your location");
        }}
    }

    //GET ADDRESS from location latitude, longitude
    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getSubThoroughfare()).append(" ");
                result.append(address.getThoroughfare());
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        location_prompt = findViewById(R.id.location_prompt);
        location_details = findViewById(R.id.location_details);
        conf_location = findViewById(R.id.conf_location);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                            LatLng chosen_location = new LatLng(latitude, longitude);
                            mMap.addMarker(new MarkerOptions().position(chosen_location).title("Your Current Location")).showInfoWindow();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(chosen_location, 16));
                        }
                    }
                });

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                lat = latLng.latitude;
                lng = latLng.longitude;
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
//                markerOptions.title(getAddress(lat, lng));
//                Toast.makeText(getApplicationContext(), getAddress(lat, lng), Toast.LENGTH_LONG).show();
                location_details.setText("Your Selected Address is:\n " + getAddress(lat, lng));
                conf_location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Call Retrofit
                        Gson gson = new GsonBuilder()
                                .registerTypeAdapter(CreateUser.class, new CustomGsonAdapter.UserAdapter())
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

                        UserLocAndTimeAPI update_user = retrofit.create(UserLocAndTimeAPI.class);
                        UserLocAndTime updateloc = new UserLocAndTime(lat,lng,starttime,endtime);
                        update_user.updateUser(map,updateloc).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()) {
                                    User changesList = response.body();
                                    System.out.println("Response Bodyyyyy : :: : " + changesList);
                                    if(sflag != null) {
                                        if (!sflag.equalsIgnoreCase("Y")) {
                                            Intent intent = new Intent(TutoringLocationActivity.this, DrawerActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Intent intent = new Intent(TutoringLocationActivity.this, Settings.class);
                                            startActivity(intent);
                                        }
                                    }
                                    else
                                    {
                                        Intent intent = new Intent(TutoringLocationActivity.this, DrawerActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });

                        //Intent intent = new Intent(TutoringLocationActivity.this, DrawerActivity.class);
                       // startActivity(intent);
                    }
                });
                googleMap.clear();
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.addMarker(markerOptions);
            }
        });
    }
}
