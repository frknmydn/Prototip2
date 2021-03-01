package com.furkanmeydan.prototip2.Model;

public class Post {
    private String city,
                    ownerId,
                    passengerCount,
                    destination,
                    description,
                    timestamp,
                    carDetail,
                    toLat,
                    toLng,
                    fromLat,
                    fromLng;

    public Post(){

    }



    public Post(String city, String ownerId,String passengerCount,
                String destination, String description, String timestamp, String carDetail,
                String toLat, String toLng, String fromLat, String fromLng) {
        this.city = city;
        this.ownerId = ownerId;
        this.passengerCount = passengerCount;
        this.destination = destination;
        this.description = description;
        this.timestamp = timestamp;
        this.carDetail = carDetail;
        this.toLat = toLat;
        this.toLng = toLng;
        this.fromLat = fromLat;
        this.fromLng = fromLng;

    }


    public String getCity() {
        return city;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getPassengerCount() {
        return passengerCount;
    }

    public String getDestination() {
        return destination;
    }

    public String getDescription() {
        return description;
    }

    public String getTimestamp() {
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


}
