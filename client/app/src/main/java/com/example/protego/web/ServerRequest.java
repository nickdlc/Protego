package com.example.protego.web;

import android.util.Log;

import org.json.JSONObject;

import java.util.Map;

public class ServerRequest {
    private Endpoint endpoint;
    private RequestManager.RequestType requestType;
    private boolean completed;
    private Object result;

    public ServerRequest(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    // Return String uid of created request
    public void post(Map<String, Object> jsonParams, ServerRequestListener serverRequestListener) {
        // verify this request was used only once
        if (this.completed)
            return;

        ServerRequest thisRequest = this;
        this.requestType = RequestManager.RequestType.POST;
        String uri = UriFormatter.formatUrl(RequestManager.prefixURL, this.endpoint);

        RequestManager.getInstance()
                    .postRequest(jsonParams, uri, new RequestListener<String>() {
                        @Override
                        public void getResult(String object) {
                            setResult(object.toString());
                            completed = true;
                            serverRequestListener.recieveCompletedRequest(thisRequest);
                        }
                    });
    }

    // Return JSONObject of get request
    public void get(Map<String, String> urlParams, ServerRequestListener serverRequestListener) {
        // verify this request was used only once
        if (this.completed)
            return;

        ServerRequest thisRequest = this;
        this.requestType = RequestManager.RequestType.POST;
        String uri = UriFormatter.formatUrl(RequestManager.prefixURL, this.endpoint, urlParams);

        RequestManager.getInstance()
                .getRequest(urlParams, uri, new RequestListener<String>() {
                    @Override
                    public void getResult(String object) {
                        setResult(object.toString());
                        completed = true;
                        serverRequestListener.recieveCompletedRequest(thisRequest);
                    }
                });
    }

    public RequestManager.RequestType getRequestType() {
        return this.requestType;
    }

    public Object getResult() {
        switch (this.requestType) {
            case POST: return (String)this.result;
            case GET: return (JSONObject)this.result;
            default: return null; // invalid request type
        }
    }

    public void setResult(Object object) {
        this.result = object;
    }
}
