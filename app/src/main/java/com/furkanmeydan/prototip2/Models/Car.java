package com.furkanmeydan.prototip2.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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




    public Car(int carID,String brand, String model, String color, String type, String optionalInfo, int year) {
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
}
