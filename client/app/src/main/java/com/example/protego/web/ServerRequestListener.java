package com.example.protego.web;

import org.json.JSONException;

public interface ServerRequestListener {
    public void recieveCompletedRequest(ServerRequest req);
}
