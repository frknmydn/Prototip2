package com.furkanmeydan.prototip2.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.furkanmeydan.prototip2.DataLayer.Callbacks.AppDatabase;
import com.furkanmeydan.prototip2.DataLayer.CarDAL;
import com.furkanmeydan.prototip2.Models.Car;
import com.furkanmeydan.prototip2.R;
import com.furkanmeydan.prototip2.Views.MainActivity.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MyCarsAdapter extends RecyclerView.Adapter<MyCarsAdapter.PostHolder> {
    private ArrayList<Car> cars;
    MainActivity activity;

    public MyCarsAdapter() {
    }

    public MyCarsAdapter(ArrayList<Car> cars, MainActivity activity) {
        this.cars = cars;
        this.activity = activity;

    }

    @NonNull
    @NotNull
    @Override
    public MyCarsAdapter.PostHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.my_cars_rcl_row,parent,false);

        return new MyCarsAdapter.PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PostHolder holder, int position) {
        holder.txtCarBrand.setText(cars.get(position).getBrand());
        holder.txtCarModel.setText(cars.get(position).getModel());
        Glide.with(activity.getApplicationContext()).load(cars.get(position).getPicURL()).apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(holder.imgCar);

    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        ImageView imgCar;
        TextView txtCarBrand, txtCarModel;
        public PostHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgCar = itemView.findViewById(R.id.imgMyCarRow);
            txtCarBrand = itemView.findViewById(R.id.txtmyCarRowBrand);
            txtCarModel = itemView.findViewById(R.id.txtMyCarRowModel);
        }
    }
}
