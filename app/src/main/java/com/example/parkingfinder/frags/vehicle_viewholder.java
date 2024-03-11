package com.example.parkingfinder.frags;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingfinder.R;

public class vehicle_viewholder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView nameView;
    TextView vehicleNumView;


    public vehicle_viewholder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.vehImageView);
        nameView = itemView.findViewById(R.id.vehName);
        vehicleNumView = itemView.findViewById(R.id.vehNumberView);
    }
}
