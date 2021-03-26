package com.furkanmeydan.prototip2.Models;

import java.io.Serializable;

public class Post implements Serializable {
    private String  postID,
                    city,
                    destination,
                    description,
                    carDetail,
                    userGender,
                    ownerID;
    private Double toLat,
            toLng,
            fromLat,
            fromLng;

    private int direction; // 0-->kuzey doğu 1-->güney doğu 2-->güney batı 3--> kuzey batı
    private long timestamp;
    private int status; /// 0--> not active, 1--> active, 3--> full
    private int passengerCount;

    public Post(){

    }



    public Post(String postID, String ownerID,String city, int passengerCount,
                String destination, String description, long timestamp, String carDetail,
                Double toLat, Double toLng, Double fromLat, Double fromLng, int status, String userGender, int direction) {
        this.city = city;
        this.passengerCount = passengerCount;
        this.destination = destination;
        this.description = description;
        this.timestamp = timestamp;
        this.carDetail = carDetail;
        this.toLat = toLat;
        this.toLng = toLng;
        this.fromLat = fromLat;
        this.fromLng = fromLng;
        this.status = status;
        this.userGender = userGender;
        this.direction = direction;
        this.ownerID = ownerID;
        this.postID = postID;


    }


    public String getCity() {
        return city;
    }

    public int getPassengerCount() {
        return passengerCount;
    }

    public String getDestination() {
        return destination;
    }

    public String getDescription() {
        return description;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getCarDetail() {
        return carDetail;
    }

    public Double getToLat() {
        return toLat;
    }

    public Double getToLng() {
        return toLng;
    }

    public Double getFromLat() {
        return fromLat;
    }

    public Double getFromLng() {
        return fromLng;
    }

    public String getUserGender() {
        return userGender;
    }

    public int getStatus() {
        return status;
    }

    public int getDirection() {
        return direction;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public String getPostID() {
        return postID;
    }
}
