package com.example.soumit.grocerylist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.soumit.grocerylist.Data.DatabaseHandler;
import com.example.soumit.grocerylist.Model.Grocery;
import com.example.soumit.grocerylist.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText groceryItem;
    private EditText quantity;
    private Button saveBtn;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DatabaseHandler(this);

        byPassActivity();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Hello Soumit", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                createPopupDialog();
            }
        });
    }

    private void createPopupDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        groceryItem = (EditText) view.findViewById(R.id.item_id);
        quantity = (EditText) view.findViewById(R.id.quantity_id);
        saveBtn = (Button) view.findViewById(R.id.saveBtn);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!groceryItem.getText().toString().isEmpty()
                        && !quantity.getText().toString().isEmpty()) {
                    saveGroceryToDB(view);
                }

            }
        });
    }

    private void saveGroceryToDB(View view) {

        Grocery grocery = new Grocery();

        String newGrocery = groceryItem.getText().toString();
        String newQuantity = quantity.getText().toString();

        grocery.setName(newGrocery);
        grocery.setQuantity(newQuantity);

        db.addGrocery(grocery);

        Snackbar.make(view, "Item saved !", Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        }, 1000);

//        Log.d(TAG, "saveGroceryToDB: Item count : " + db.getGroceriesCount());
    }


    private void byPassActivity(){

        if(db.getGroceriesCount() > 0){
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }

    }

}






















