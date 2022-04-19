package com.example.protego.web;

public interface FirestoreListenerStream<T> {
    public void streamResult(T object);
    public void getError(Exception e, String msg);
}
