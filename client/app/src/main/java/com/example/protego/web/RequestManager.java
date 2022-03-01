package com.example.protego.web;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestManager {
    private static final String TAG = "RequestManager";
    // private static final String prefixURL = "127.0.0.1:8000";
    private static final String prefixURL = "192.168.1.152:8000"; // for kareem


    private static RequestManager instance = null;

    private static RequestQueue queue;

    private RequestManager(Context context) {
        queue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized RequestManager getInstance(Context context) {
        if (null == instance)
            instance = new RequestManager(context);
        return instance;
    }

    public static synchronized RequestManager getInstance() {
        if (null == instance)
            throw new IllegalStateException(RequestManager.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        return instance;
    }
}
