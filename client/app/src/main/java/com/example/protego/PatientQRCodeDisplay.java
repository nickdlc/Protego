package com.example.protego;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.protego.web.Endpoint;
import com.example.protego.web.RequestManager;
import com.example.protego.web.UriFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Map;

public class PatientQRCodeDisplay extends AppCompatActivity {
    public static final String TAG = "PatientQRCodeDisplay";
    private FirebaseAuth mAuth;

    private NetworkImageView nv;
    private ImageLoader imageLoader;
    private Button btnReturn;

    private int width;
    private int height;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_qr_code_display);

        mAuth = FirebaseAuth.getInstance();

        btnReturn = findViewById(R.id.qrCodeReturnBtn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PatientDashboardActivity.class);
                startActivity(i);
                finish();
            }
        });

        // TODO: figure out how to show based off dimensions
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        width = displayMetrics.widthPixels;
//        height = displayMetrics.heightPixels;
        width = height = 250;

        try {
            Bitmap bm = encodeAsBitmap(mAuth.getCurrentUser().getUid());
            ImageView iv = (ImageView) findViewById(R.id.qrCode);
            iv.setImageBitmap(bm);
        } catch (WriterException e) {
            Log.e(TAG, "Failed to generate QR Code for patient", e);
        }
    }

    private Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, width, height, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, w, h);
        return bitmap;
    }
}
