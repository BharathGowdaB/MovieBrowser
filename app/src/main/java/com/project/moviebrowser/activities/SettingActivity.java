package com.project.moviebrowser.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.moviebrowser.R;
import com.project.moviebrowser.configuration.Configuration;
import com.project.moviebrowser.networking.ApiEndpoint;
import com.project.moviebrowser.networking.Certification;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    private Switch switchReminder;
    private Switch switchRelease;

    private Spinner ceritificationSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        switchReminder = findViewById(R.id.swDailyReminder);
        switchRelease = findViewById(R.id.swRealeseToday);
        ceritificationSpinner = findViewById(R.id.certificationList);

        ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(), R.layout.season_list, Certification.certificationList);
        adapter.setDropDownViewResource(R.layout.season_list);
        ceritificationSpinner.setAdapter(adapter);
        ceritificationSpinner.setSelection(Configuration.getInstance().getCertificationLevel());

        ceritificationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Configuration.getInstance().setCertificationLevel(ceritificationSpinner.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
