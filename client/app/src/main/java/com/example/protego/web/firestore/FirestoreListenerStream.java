package com.example.protego.web.firestore;

public interface FirestoreListenerStream<T> {
    public void streamResult(T object);
    public void getError(Exception e, String msg);
}
