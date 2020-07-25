package com.codewithankit.mygrocerylist.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.codewithankit.mygrocerylist.R;

public class DetailsActivity extends AppCompatActivity {


    private TextView name , date , qty;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        name = (TextView) findViewById(R.id.itemNameDet);
        date = (TextView) findViewById(R.id.dataAddedDet);
        qty  = (TextView) findViewById(R.id.itemQuantityDet);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            name.setText(extras.getString("name"));
            date.setText(extras.getString("date"));
            qty.setText(extras.getString("quantity"));
            id = extras.getInt("id");
        }
    }
}
