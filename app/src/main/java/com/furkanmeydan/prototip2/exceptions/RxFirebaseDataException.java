package com.furkanmeydan.prototip2.exceptions;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class RxFirebaseDataException extends Exception{

    protected FirebaseFirestoreException error;

    public RxFirebaseDataException(@NonNull FirebaseFirestoreException error) {
        this.error = error;
    }

    public FirebaseFirestoreException getError() {
        return error;
    }

    @Override
    public String toString() {
        return "RxFirebaseDataException{" +
                "error=" + error +
                '}';
    }

}
