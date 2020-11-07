package com.example.appquanli12;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appquanli12.Interface.ItemClickListener;
import com.example.appquanli12.Model.Table;
import com.example.appquanli12.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;

    FirebaseDatabase database;
    DatabaseReference table;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*---------------------*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Danh sách bàn");
        /*---------------------*/
        setSupportActionBar(toolbar);
        /*---------------------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                 R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        table = database.getReference("Table");

        //Load menu
        recycler_menu = (RecyclerView) findViewById(R.id.recycler_table);
        recycler_menu.setHasFixedSize(true);
        recycler_menu.setLayoutManager(new GridLayoutManager(this,3));

        FirebaseRecyclerOptions<Table> options= new FirebaseRecyclerOptions.Builder<Table>().setQuery(table,Table.class).build();

            FirebaseRecyclerAdapter<Table, MenuViewHolder> adapter = new FirebaseRecyclerAdapter<Table, MenuViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull Table model) {

                    holder.txtMenuName.setText(model.getName());
                    final Table clickItem = model;
                    holder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {
                            startActivity(new Intent(getApplicationContext(),food_category.class));
                        }
                    });
                }

                @NonNull
                @Override
                public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item,parent, false);
                    MenuViewHolder viewHolder=  new MenuViewHolder(view);
                    return viewHolder;
                }
            };

        recycler_menu.setAdapter(adapter);
        adapter.startListening();


        }


    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {super.onBackPressed();
        }
    }
    /*---mang hinh trai--*/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:

                break;

            case R.id.nav_list_food:
                Intent intent = new Intent(MainActivity.this,SettingCategory.class);
                startActivity(intent);
                break;

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
                break;


            case R.id.nav_share: Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show(); break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}