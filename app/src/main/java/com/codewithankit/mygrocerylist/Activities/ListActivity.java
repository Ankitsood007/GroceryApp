package com.codewithankit.mygrocerylist.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.codewithankit.mygrocerylist.Model.Grocery;
import com.codewithankit.mygrocerylist.UI.RecyclerViewAdapter;
import com.codewithankit.mygrocerylist.data.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.codewithankit.mygrocerylist.R;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Grocery> groceryList;
    private List<Grocery> listItems;
    private DatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListActivity.this , MainActivity.class));
            }
        });

        db = new DatabaseHandler(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        groceryList = new ArrayList<>();
        listItems = new ArrayList<>();
        groceryList = db.getAllGroceries();

        for(Grocery g :groceryList){
            Grocery x = new Grocery();
            x.setName("Item: " + g.getName());
            x.setQuantity("Qty: " + g.getQuantity());
            x.setDateItemAdded("Added on: " + g.getDateItemAdded());
            x.setId(g.getId());

            listItems.add(x);
        }

        recyclerViewAdapter = new RecyclerViewAdapter(this , listItems);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

}
