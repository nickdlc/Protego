package com.example.protego.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.protego.R;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private Button btnLoginOption;
    private Button btnSignupOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLoginOption = findViewById(R.id.btnLoginOption);
        btnLoginOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.i(TAG, "onClick login option");
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSignupOption = findViewById(R.id.btnSignupOption);
        btnSignupOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.i(TAG, "onClick signup option");
                Intent intent = new Intent(v.getContext(), SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }


}