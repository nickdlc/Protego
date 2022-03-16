package com.example.protego.web;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

// Our manager to make requests to the server
public class RequestManager {
    private static final String TAG = "RequestManager";
    public static final String prefixURL = "10.0.2.2:8000";

    private static RequestManager instance = null;

    private static RequestQueue queue;

    public enum RequestType {
        GET, POST;
    }

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

    public static synchronized RequestQueue getRequestQueue() {
        if (null == instance.queue)
            throw new IllegalStateException("Queue is not initialized, call getInstance(...) first");
        return instance.queue;
    }

    public void postRequest(Map<String, Object> paramList, String uri, final RequestListener<String> listener)
    {
        StringRequest request = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        // Request completed
                        Log.d(TAG + ": ", "postRequest:recievedResponse : " + response.toString());
                        if (null != response.toString())
                            listener.getResult(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Failed to make the request
                        if (null != error.networkResponse)
                        {
                            Log.d(TAG + ": ", "Error Response code: " + error.networkResponse.statusCode);
                            Log.e(TAG, "Error request : ", error);
                            listener.getResult(null);
                        }
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() {
                        String paramBody = new JSONObject(paramList).toString();
                        return paramBody.getBytes(StandardCharsets.UTF_8);
                    }
                };
        queue.add(request);
    }

    public void getRequest(Map<String, String> urlParams, String uri, final RequestListener<String> listener)
    {
        Log.d(TAG, "URL: " + uri);
        StringRequest request = new StringRequest(Request.Method.GET, uri,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        // Request completed
                        Log.d(TAG + ": ", "postRequest:recievedResponse : " + response.toString());
                        if (null != response)
                            listener.getResult(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Failed to make the request
                        if (null != error.networkResponse)
                        {
                            Log.d(TAG + ": ", "Error Response code: " + error.networkResponse.statusCode);
                        }
                        Log.e(TAG, "Error request : ", error);
                        listener.getResult(null);
                    }
                });
        queue.add(request);
    }
}
