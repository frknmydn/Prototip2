package com.furkanmeydan.prototip2.Model;

public class Post {
    private String city,
                    destination,
                    description,
                    carDetail,
                    toLat,
                    toLng,
                    fromLat,
                    fromLng,
                    userGender;
    private long timestamp;
    private int status; /// 0--> not active, 1--> active, 3--> full
    private int passengerCount;
    public Post(){

    }



    public Post(String city, int passengerCount,
                String destination, String description, long timestamp, String carDetail,
                String toLat, String toLng, String fromLat, String fromLng, int status, String userGender) {
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

    public String getToLat() {
        return toLat;
    }

    public String getToLng() {
        return toLng;
    }

    public String getFromLat() {
        return fromLat;
    }

    public String getFromLng() {
        return fromLng;
    }

    public String getUserGender() {
        return userGender;
    }

    public int getStatus() {
        return status;
    }
}
