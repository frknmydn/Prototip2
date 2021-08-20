package com.furkanmeydan.prototip2.DataLayer.Callbacks;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.furkanmeydan.prototip2.Models.Car;

@Database(entities = {Car.class}, version =2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CarDAO carDao();
}