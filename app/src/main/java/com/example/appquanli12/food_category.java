package com.example.appquanli12;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.appquanli12.Interface.ItemClickListener;
import com.example.appquanli12.Model.Category;
import com.example.appquanli12.Model.Table;
import com.example.appquanli12.ViewHolder.CategoryViewHolder;
import com.example.appquanli12.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class food_category extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference category;

    RecyclerView recyclercategory;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_category);

        //back toolbar
        Toolbar toolbar=findViewById(R.id.toolbar_category);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Danh mục món ăn");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        //Init Firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        //Load menu
        recyclercategory = (RecyclerView) findViewById(R.id.recycler_category);
        recyclercategory.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclercategory.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Category> options= new FirebaseRecyclerOptions.Builder<Category>().setQuery(category,Category.class).build();


        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull Category model) {
                holder.txtCategoryName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImg()).into(holder.imgcategory);
                final Category clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodList=new Intent(food_category.this,Food_Menu.class);
                        foodList.putExtra("CategoryId",adapter.getRef(position).getKey());
                        startActivity(foodList);
                    }
                });

            }

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent, false);
                CategoryViewHolder viewHolder=  new CategoryViewHolder(view);
                return viewHolder;
            }
        };


        recyclercategory.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.startListening();




    }


}
