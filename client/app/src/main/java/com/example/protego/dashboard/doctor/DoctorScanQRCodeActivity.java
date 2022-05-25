package com.example.protego.dashboard.doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.protego.R;

public class DoctorScanQRCodeActivity extends AppCompatActivity {

    // input fields here
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_scan_qrcode);

        //Connects the Cancel QR Code button to the Doctor Dashboard activity
        connectButtonToActivity(R.id.DoctorQRCodeCancelButton, DoctorDashboardActivity.class);

    }

    // navigate to next activity
    private void connectButtonToActivity(Integer buttonId, Class nextActivityClass) {

        button = findViewById(buttonId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), nextActivityClass);
                startActivity(i);
                finish();
            }
        });
    }
}




