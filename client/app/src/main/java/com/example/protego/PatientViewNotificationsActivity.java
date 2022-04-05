package com.example.protego;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PatientViewNotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_view_notifications_activity);

        TextView notificationText = findViewById(R.id.tvNotificationView);

        String notificationMsg = "No notification message set";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            notificationMsg = extras.getString("msg");
        }

        notificationText.setText(notificationMsg);
    }
}
