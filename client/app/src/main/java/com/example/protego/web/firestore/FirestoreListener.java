package com.example.protego.web.firestore;

public interface FirestoreListener<T> {
    public void getResult(T object);
    public void getError(Exception e, String msg);
}
