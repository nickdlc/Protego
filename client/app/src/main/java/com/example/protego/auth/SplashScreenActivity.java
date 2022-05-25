package com.example.protego.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.protego.R;

public class SplashScreenActivity extends AppCompatActivity {
    public static final String TAG = "SplashScreenActivity";
    Handler handler;
    ImageView ivSplash;
    TextView tvTitle;
    TextView tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_splash);

        ivSplash = findViewById(R.id.ivSplash);
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);

        if(getSupportActionBar() != null)
            getSupportActionBar().hide();


        handler = new Handler();
        handler.postDelayed(() -> {
            Log.i(TAG, "onDelay");
            Intent intent = new Intent(this, LoginActivity.class);
            Log.i(TAG, "onDelay");
            startActivity(intent);
            finish();
        },2000);

    }
}

