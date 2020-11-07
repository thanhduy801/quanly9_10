package com.example.appquanli12.ViewHolder;

import android.media.Image;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appquanli12.Common.Common;
import com.example.appquanli12.Interface.ItemClickListener;
import com.example.appquanli12.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnCreateContextMenuListener
{

    public TextView txtCategoryName;
    public ImageView imgcategory;
    private ItemClickListener itemClickListener;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        txtCategoryName =(TextView)itemView.findViewById(R.id.category_txt);
        imgcategory =(ImageView) itemView.findViewById(R.id.category_img);

        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select the action");

        contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
        contextMenu.add(0,0,getAdapterPosition(), Common.DELETE);

    }
}
