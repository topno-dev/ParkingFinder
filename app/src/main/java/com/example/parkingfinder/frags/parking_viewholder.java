package com.example.parkingfinder.frags;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingfinder.R;

public class parking_viewholder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView list_category, list_address, list_phonenumber;
    public parking_viewholder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
        super(itemView);
        imageView = itemView.findViewById(R.id.list_imageview);
        list_category = itemView.findViewById(R.id.list_category);
        list_address =itemView.findViewById(R.id.list_address);
        list_phonenumber =itemView.findViewById(R.id.list_phonenumber);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerViewInterface != null){
                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION){
                        recyclerViewInterface.onItemClick(pos);
                    }
                }
            }
        });
    }

    private void setAnimation(View viewToAnimate, int position){

    }

}
