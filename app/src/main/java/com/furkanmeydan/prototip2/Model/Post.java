package com.furkanmeydan.prototip2.Model;

public class Post {
    private String city,
                    ownerid,
                    date,
                    time,
                    passengerCount,
                    destination,
                    description,
                    timestamp,
                    carDetail,
                    toLat,
                    toLng,
                    fromLat,
                    fromLng;

    private Object request;

    public Post(String city, String ownerid, String date, String time, String passengerCount,
                String destination, String description, String timestamp, String carDetail,
                String toLat, String toLng, String fromLat, String fromLng, Object request) {
        this.city = city;
        this.ownerid = ownerid;
        this.date = date;
        this.time = time;
        this.passengerCount = passengerCount;
        this.destination = destination;
        this.description = description;
        this.timestamp = timestamp;
        this.carDetail = carDetail;
        this.toLat = toLat;
        this.toLng = toLng;
        this.fromLat = fromLat;
        this.fromLng = fromLng;
        this.request = request;
    }


    public String getCity() {
        return city;
    }

    public String getOwnerid() {
        return ownerid;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
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

    public Object getRequest() {
        return request;
    }
}
