package com.codewithankit.mygrocerylist.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.codewithankit.mygrocerylist.Model.Grocery;
import com.codewithankit.mygrocerylist.R;
import com.codewithankit.mygrocerylist.data.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.DialogTitle;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText groceryItem;
    private EditText groceryQuantity;
    private Button saveButton;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup();
            }
        });

        Button gotobtn = (Button) findViewById(R.id.mainactivityButtonID);
        gotobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.getGroceryCount() >= 1)
                    startActivity(new Intent(MainActivity.this , ListActivity.class));
                else {
                    Toast.makeText(MainActivity.this ,  "The List is Empty", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showPopup(){

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup , null);

        //now since we have now this view object means now we can interact with every thing that is
        //inside the popup.xml file , so lets instantiate every thing that is present inside the popup.xml

        //instantiation of every widget on the popup...
        groceryItem = (EditText) view.findViewById(R.id.grocery_item);
        groceryQuantity = (EditText) view.findViewById(R.id.grocery_quantity);
        saveButton = (Button) view.findViewById(R.id.saveButton);

        //dialog creater..
        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        //now the save button functionality..

        saveButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo:save the data that we enetered in the above dialog.
                //Todo:Go to next screen which will show the list of grocery items.
                if(!groceryItem.getText().toString().equals("") && !groceryQuantity.getText().toString().equals("")) {
                    saveGroceryToDb(v);
                }
                else {
                    Toast.makeText(MainActivity.this , "Please Enter valid Item Name and Quantity" , Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveGroceryToDb(View v) {

        Grocery g = new Grocery();
        String new_grocery = groceryItem.getText().toString();
        String new_grocery_quantity = groceryQuantity.getText().toString();
        g.setName(new_grocery);
        g.setQuantity(new_grocery_quantity);
        db.addGrocery(g);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                //start a new activity.
                startActivity(new Intent(MainActivity.this , ListActivity.class));
            }
        },80);
    }
}
