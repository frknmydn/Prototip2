package com.furkanmeydan.prototip2.DataLayer.Callbacks;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.furkanmeydan.prototip2.Models.Car;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface CarDAO {
    @Query("SELECT * FROM car")
    List<Car> getAllCars();

    @Query("SELECT * FROM car WHERE carID IN (:carIDs)")
    List<Car> loadAllCarsByIds(int[] carIDs);

    @Query("SELECT * FROM car WHERE carOwnerID IN (:carownerID)")
    List<Car> loadAllCarsByUserID(String carownerID);

    @Insert
    void insertAll(Car... car);

    @Delete
    void deleteCar(Car car);

}


