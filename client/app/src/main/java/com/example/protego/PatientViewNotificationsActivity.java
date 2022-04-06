package com.example.protego;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PatientViewNotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_view_notifications_activity);

        TextView notificationText = findViewById(R.id.tvNotificationView);
        Button btnAccept = findViewById(R.id.btnAccept);
        Button btnDecline = findViewById(R.id.btnDecline);

        String notificationMsg = "No notification message set";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            notificationMsg = extras.getString("msg");
        }

        notificationText.setText(notificationMsg);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Intent i = new Intent(v.getContext(), nextActivityClass);
                startActivity(i);
                finish();*/
            }
        });
    }
}
