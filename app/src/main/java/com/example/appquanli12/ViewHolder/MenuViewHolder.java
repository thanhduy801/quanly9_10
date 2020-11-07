package com.example.appquanli12.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appquanli12.Interface.ItemClickListener;
import com.example.appquanli12.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtMenuName;
    private ItemClickListener itemClickListener;


    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);

        txtMenuName =(TextView)itemView.findViewById(R.id.menu_name);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }
}
