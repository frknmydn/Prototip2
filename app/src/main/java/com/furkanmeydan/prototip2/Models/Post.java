package com.furkanmeydan.prototip2.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class Post implements Serializable, Comparable<Post>{
    private String  postID,
                    city,
                    destination,
                    description,
                    carDetail,
                    userGender,
                    ownerID,
                    ownerOneSignalID;
    private Car car;

    private Double toLat,
            toLng,
            fromLat,
            fromLng;

    private int direction; // 0-->kuzey doğu 1-->güney doğu 2-->güney batı 3--> kuzey batı
    private long timestamp;
    private int status; /// 0--> Silinmiş, 1--> active, 2 --> Tarihi geçmiş, 3--> full
    private int passengerCount;
    private ArrayList<String> wishArray;
    private int hasStarted; // 0 --> başlamamış 1--> başlamış

    public Post(){

    }

    public Post(String postID, String ownerID, String city, int passengerCount,
                String destination, String description, long timestamp,
                Double toLat, Double toLng, Double fromLat, Double fromLng, int status, int hasStarted,
                String userGender, int direction, ArrayList<String> wishArray, String ownerOneSignalID,Car car) {
        this.postID = postID;
        this.city = city;
        this.destination = destination;
        this.description = description;
        this.carDetail = carDetail;
        this.userGender = userGender;
        this.ownerID = ownerID;
        this.ownerOneSignalID = ownerOneSignalID;
        this.car = car;
        this.toLat = toLat;
        this.toLng = toLng;
        this.fromLat = fromLat;
        this.fromLng = fromLng;
        this.direction = direction;
        this.timestamp = timestamp;
        this.status = status;
        this.passengerCount = passengerCount;
        this.wishArray = wishArray;
        this.hasStarted = hasStarted;
    }

    /*
    public Post(String postID, String ownerID, String city, int passengerCount,
                String destination, String description, long timestamp, String carDetail,
                Double toLat, Double toLng, Double fromLat, Double fromLng, int status, int hasStarted,
                String userGender, int direction, ArrayList<String> wishArray, String ownerOneSignalID) {
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
        this.wishArray = wishArray;
        this.ownerOneSignalID = ownerOneSignalID;
        this.hasStarted = hasStarted;


    }

     */


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

    public String getOwnerOneSignalID() {
        return ownerOneSignalID;
    }

    public int getHasStarted() {
        return hasStarted;
    }

    public ArrayList<String> getWishArray() {
        return wishArray;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCarDetail(String carDetail) {
        this.carDetail = carDetail;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public void setOwnerOneSignalID(String ownerOneSignalID) {
        this.ownerOneSignalID = ownerOneSignalID;
    }

    public void setToLat(Double toLat) {
        this.toLat = toLat;
    }

    public void setToLng(Double toLng) {
        this.toLng = toLng;
    }

    public void setFromLat(Double fromLat) {
        this.fromLat = fromLat;
    }

    public void setFromLng(Double fromLng) {
        this.fromLng = fromLng;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setPassengerCount(int passengerCount) {
        this.passengerCount = passengerCount;
    }

    public void setWishArray(ArrayList<String> wishArray) {
        this.wishArray = wishArray;
    }

    public void setHasStarted(int hasStarted) {
        this.hasStarted = hasStarted;
    }

    @Override
    public int compareTo(Post o) {
        return Long.compare(o.getTimestamp(),this.getTimestamp());
    }
}




