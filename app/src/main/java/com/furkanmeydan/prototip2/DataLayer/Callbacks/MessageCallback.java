package com.furkanmeydan.prototip2.DataLayer.Callbacks;

import com.furkanmeydan.prototip2.Models.Message;
import com.furkanmeydan.prototip2.Models.Request;


import java.util.List;

public abstract class MessageCallback {
    public void onMessageSent(){

    }

    public void onMessageReceived(Message message){

    }

    public void onProfilesLoaded(List<Request> users){

    }
}
