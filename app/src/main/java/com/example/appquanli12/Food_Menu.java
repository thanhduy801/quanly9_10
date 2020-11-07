package com.example.appquanli12;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.appquanli12.Interface.ItemClickListener;
import com.example.appquanli12.Model.Category;
import com.example.appquanli12.Model.Food;
import com.example.appquanli12.ViewHolder.CategoryViewHolder;
import com.example.appquanli12.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Food_Menu extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference foods;

    RecyclerView recyclerfood;
    RecyclerView.LayoutManager layoutManager;

    String categoryId="";

    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    //Search Food
    FirebaseRecyclerAdapter<Food,FoodViewHolder> searchAdapter;
    List<String> suggestList =new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food__menu);

        Toolbar toolbar=findViewById(R.id.toolbar_food);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Danh sách món ăn");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");

        //Load menu
        recyclerfood = (RecyclerView) findViewById(R.id.recycler_food);
        recyclerfood.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerfood.setLayoutManager(layoutManager);

        //Get Intent here
        if (getIntent() !=null)
            categoryId=getIntent().getStringExtra("CategoryId");
        if(!categoryId.isEmpty()&&categoryId!=null)
        {
            loadListFood (categoryId);
        }
        //search
        materialSearchBar=(MaterialSearchBar)findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter your food");
        materialSearchBar.setSpeechMode(false);
        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> suggest=new ArrayList<String>();
                for (String search:suggestList){
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled)
                    recyclerfood.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        FirebaseRecyclerOptions<Food> options= new FirebaseRecyclerOptions.Builder<Food>().setQuery(foods.orderByChild("Name").equalTo(text.toString()),Food.class).build();
        searchAdapter=new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {
                holder.txtFoodName.setText(model.getName());
                holder.txtFoodMoney.setText(model.getPrice()+" đ");
                Picasso.with(getBaseContext()).load(model.getImg()).into(holder.imgfood);
                final Food clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(Food_Menu.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,parent, false);
                FoodViewHolder viewHolder=  new FoodViewHolder(view);
                return viewHolder;
            }
        };
        recyclerfood.setAdapter(searchAdapter);
        searchAdapter.startListening();
    }

    private void loadSuggest() {
        foods.orderByChild("MenuId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postsnapshot:snapshot.getChildren()){
                    Food item =postsnapshot.getValue(Food.class);
                    suggestList.add(item.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadListFood(String categoryId) {
        FirebaseRecyclerOptions<Food> options= new FirebaseRecyclerOptions.Builder<Food>().setQuery(foods.orderByChild("MenuId").equalTo(categoryId),Food.class).build();
        adapter=new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {
                holder.txtFoodName.setText(model.getName());
                holder.txtFoodMoney.setText(model.getPrice()+" đ");
                Picasso.with(getBaseContext()).load(model.getImg()).into(holder.imgfood);
                final Food clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodDetail = new Intent(Food_Menu.this,FoodDetail.class);
                        foodDetail.putExtra("FoodId",adapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });
            }
            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,parent, false);
                FoodViewHolder viewHolder=  new FoodViewHolder(view);
                return viewHolder;
            }
        };


        recyclerfood.setAdapter(adapter);
        adapter.startListening();
    }
}