package com.furkanmeydan.prototip2.Models;

import java.io.Serializable;

public class Request implements Serializable {

    String requestID,senderID,senderName, senderGender, senderImage, senderBirthdate, senderEmail, postID, postOwnerID, postHeader, requestText;
    Double lat1,lng1,lat2,lng2;
    int status; // 0--> bekliyor, -1--> ret , 1--> onaylanmış

    public Request() {
    }

    public Request(String requestID, String senderID, String senderName, String senderGender,
                   String senderImage, String senderBirthdate, String senderEmail, String postID,
                   String postOwnerID, Double lat1, Double lng1, Double lat2, Double lng2, int status, String postHeader, String requestText) {
        this.requestID = requestID;
        this.senderID = senderID;
        this.senderName = senderName;
        this.senderGender = senderGender;
        this.senderImage = senderImage;
        this.senderBirthdate = senderBirthdate;
        this.senderEmail = senderEmail;
        this.postID = postID;
        this.postOwnerID = postOwnerID;
        this.lat1 = lat1;
        this.lng1 = lng1;
        this.lat2 = lat2;
        this.lng2 = lng2;
        this.status = status;
        this.postHeader = postHeader;
        this.requestText = requestText;
    }

    public String getRequestID() {
        return requestID;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderGender() {
        return senderGender;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public String getSenderBirthdate() {
        return senderBirthdate;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getPostID() {
        return postID;
    }

    public String getPostOwnerID() {
        return postOwnerID;
    }

    public Double getLat1() {
        return lat1;
    }

    public Double getLng1() {
        return lng1;
    }

    public Double getLat2() {
        return lat2;
    }

    public Double getLng2() {
        return lng2;
    }

    public int getStatus() {
        return status;
    }

    public String getPostHeader() {
        return postHeader;
    }

    public String getRequestText() {
        return requestText;
    }
}
