package com.project.moviebrowser.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.moviebrowser.R;

public class SettingActivity extends AppCompatActivity {

    private Switch switchReminder;
    private Switch switchRelease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        switchReminder = findViewById(R.id.swDailyReminder);
        switchRelease = findViewById(R.id.swRealeseToday);

        // Switch Reminder OnClick
        switchReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchReminder.isChecked()) {
                   Toast.makeText(getApplicationContext(), "Daily reminders activated", Toast.LENGTH_SHORT).show();
                } else {
                   Toast.makeText(getApplicationContext(), "Daily reminders not activated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Switch Release OnClick
        switchRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchRelease.isChecked()) {
                   Toast.makeText(getApplicationContext(), "Release reminder activated", Toast.LENGTH_SHORT).show();
                } else {
                   Toast.makeText(getApplicationContext(), "Release reminder not activated", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}
