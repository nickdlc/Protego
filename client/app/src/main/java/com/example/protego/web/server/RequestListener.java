package com.example.protego.web.server;

@Deprecated
public interface RequestListener<T> {
    public void getResult(T object);
    public void getError(Exception e, String msg);
}
