package com.example.parkingfinder.frags;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingfinder.R;

import java.util.List;

public class vehicle_adapterclass extends RecyclerView.Adapter<vehicle_viewholder> {

    private final vehicleInterface VehicleInterface;
    Context context;
    List<vehicle> items;

    public vehicle_adapterclass(Context context, List<vehicle> items, vehicleInterface VehicleInterface) {
        this.context = context;
        this.items = items;
        this.VehicleInterface = VehicleInterface;
    }

    @NonNull
    @Override
    public vehicle_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new vehicle_viewholder(LayoutInflater.from(context).inflate(R.layout.rv_item,parent,false), VehicleInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull vehicle_viewholder holder, int position) {
        holder.nameView.setText(items.get(position).getName());
        holder.vehicleNumView.setText(items.get(position).getVeh_num());
        holder.imageView.setImageResource(items.get(position).getImage());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
