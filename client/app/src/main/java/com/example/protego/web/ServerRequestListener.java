package com.example.protego.web;

import org.json.JSONException;

public interface ServerRequestListener {
    public void receiveCompletedRequest(ServerRequest req);
    public void receiveError(Exception e, String msg);
}
