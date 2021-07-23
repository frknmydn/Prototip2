package com.furkanmeydan.prototip2.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.encoders.annotations.Encodable;

@Entity
public class Car {
    @PrimaryKey
    int carID;
    @ColumnInfo(name = "year")
    int year;
    @ColumnInfo(name = "brand")
    String brand;
    @ColumnInfo(name = "model")
    String model;
    @ColumnInfo(name = "color")
    String color;
    @ColumnInfo(name = "type")
    String type;
    @ColumnInfo(name = "optionalInfo")
    String optionalInfo;
    @ColumnInfo
    String picURL;


    public Car(int carID, int year, String brand, String model, String color, String type, String optionalInfo, String picURL) {
        this.carID = carID;
        this.year = year;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.type = type;
        this.optionalInfo = optionalInfo;
        this.picURL = picURL;
    }

    @Ignore
    public Car(int carID, String brand, String model, String color, String type, String optionalInfo, int year) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.type = type;
        this.optionalInfo = optionalInfo;
        this.year = year;
        this.carID = carID;
    }





    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public String getType() {
        return type;
    }

    public String getOptionalInfo() {
        return optionalInfo;
    }

    public int getYear() {
        return year;
    }

    public int getCarID() {
        return carID;
    }

    public String getPicURL() {
        return picURL;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }


}
