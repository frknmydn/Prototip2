package com.furkanmeydan.prototip2.DataLayer;

import com.furkanmeydan.prototip2.Models.Request;

import java.util.List;

public abstract class RequestCallback {
        public void onRequestSent(){

        }

        public void getRequestsToMe(List<Request> list){

        }

        public void onRequestAccepted(){

        }

        public void onRequestRejected(){

        }
        public void onRequestRetrievedNotNull(){

        }
        public void onRequestRetrievedNull(){

        }
}