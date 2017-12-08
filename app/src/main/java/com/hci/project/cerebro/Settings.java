package com.hci.project.cerebro;

import android.content.Intent;
import android.graphics.drawable.DrawableWrapper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button skillbttn = (Button) findViewById(R.id.skillbttn);
        Button locbttn = (Button) findViewById(R.id.locationbttn);
        Button backbttn = (Button) findViewById(R.id.backbttn);
        skillbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, TutorProfileActivity.class);
                intent.putExtra("SettingView","Y");
                startActivity(intent);
            }
        });
        locbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, TutoringLocationActivity.class);
                intent.putExtra("SettingView","Y");
                startActivity(intent);
            }
        });
        backbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, DrawerActivity.class);
                startActivity(intent);
            }
        });

    }
}
