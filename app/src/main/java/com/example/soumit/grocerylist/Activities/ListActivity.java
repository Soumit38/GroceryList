package com.example.soumit.grocerylist.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.soumit.grocerylist.Data.DatabaseHandler;
import com.example.soumit.grocerylist.Model.Grocery;
import com.example.soumit.grocerylist.R;
import com.example.soumit.grocerylist.UI.RecyclerviewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = "ListActivity";
    private Context context = ListActivity.this;

    private RecyclerView recyclerView;
    private RecyclerviewAdapter recyclerviewAdapter;
    private List<Grocery> groceryList;
    private List<Grocery> listItems;
    private DatabaseHandler db;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private EditText groceryItem;
    private EditText quantity;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: FAB clicked!");
                createPopupDialog();
            }
        });

        db = new DatabaseHandler(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        groceryList = new ArrayList<>();
        listItems = new ArrayList<>();

        groceryList = db.getAllGroceries();

        for(Grocery c : groceryList){
            Grocery grocery = new Grocery();
            grocery.setName(c.getName());
            grocery.setQuantity("Qty : " + c.getQuantity());
            grocery.setId(c.getId());
//            Log.d(TAG, "onCreate: Id : " + c.getId());
            grocery.setDateItemAdded("Added on : " + c.getDateItemAdded());

            listItems.add(grocery);
        }

        recyclerviewAdapter = new RecyclerviewAdapter(this, listItems);
        recyclerView.setAdapter(recyclerviewAdapter);
        recyclerviewAdapter.notifyDataSetChanged();
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
                    dialog.dismiss();

                    //restarting
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
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

        //Reloading Activity


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                dialog.dismiss();
//                Intent intent = new Intent(MainActivity.this, ListActivity.class);
//                startActivity(intent);
//            }
//        }, 1000);

//        Log.d(TAG, "saveGroceryToDB: Item count : " + db.getGroceriesCount());
    }

}























