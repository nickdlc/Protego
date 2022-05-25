package com.example.protego.web.server;

@Deprecated
public interface ServerRequestListener {
    public void receiveCompletedRequest(ServerRequest req);
    public void receiveError(Exception e, String msg);
}
