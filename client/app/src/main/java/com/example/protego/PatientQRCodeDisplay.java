package com.example.protego;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.LruCache;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.protego.web.Endpoint;
import com.example.protego.web.RequestManager;
import com.example.protego.web.UriFormatter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class PatientQRCodeDisplay extends AppCompatActivity {
    public static final String TAG = "PatientQRCodeDisplay";
    private FirebaseAuth mAuth;

    private NetworkImageView nv;
    private ImageLoader imageLoader;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_qr_code_display);

        NetworkImageView nv = (NetworkImageView) findViewById(R.id.qrCode);
        RequestQueue q = RequestManager.getRequestQueue();
        imageLoader = new ImageLoader(
                q,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });

        mAuth = FirebaseAuth.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put("patient", mAuth.getCurrentUser().getUid());
        map.put("width", "250");
        map.put("height", "250");

        nv.setImageUrl(UriFormatter.formatUrl(Endpoint.GET_QR_CODE, map), imageLoader);
    }
}
