package com.example.soumit.grocerylist.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.soumit.grocerylist.R;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = "DetailsActivity";

    private TextView itemName;
    private TextView quantity;
    private TextView dateAdded;
    private int groceryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        itemName = (TextView) findViewById(R.id.itemNameDet_id);
        quantity = (TextView) findViewById(R.id.quantityDet_id);
        dateAdded = (TextView) findViewById(R.id.dateDet_id);

        Intent getIntent = getIntent();

        if(getIntent != null){
            itemName.setText(getIntent.getStringExtra("name"));
            quantity.setText(getIntent.getStringExtra("quantity"));
            dateAdded.setText(getIntent.getStringExtra("date"));
//            Log.d(TAG, "onCreate: name : " + getIntent.getStringExtra("name"));
//            Log.d(TAG, "onCreate: Id : " + getIntent.getIntExtra("id", 0));
            groceryId = getIntent.getIntExtra("id",0);
        }

    }
}
