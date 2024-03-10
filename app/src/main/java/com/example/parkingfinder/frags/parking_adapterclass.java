package com.example.parkingfinder.frags;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingfinder.R;

import java.util.List;

public class parking_adapterclass extends RecyclerView.Adapter<parking_viewholder> {

    Context context;
    List<parking_list> items;

    public parking_adapterclass(Context context, List<parking_list> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public parking_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new parking_viewholder(LayoutInflater.from(context).inflate(R.layout.item_view_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull parking_viewholder holder, int position) {
        holder.list_category.setText(items.get(position).getCategory());
        holder.list_address.setText(items.get(position).getAddress());
        holder.list_phonenumber.setText(items.get(position).getPhone());
        holder.imageView.setImageResource(items.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
