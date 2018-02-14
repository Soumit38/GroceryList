package com.example.soumit.grocerylist.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.soumit.grocerylist.Activities.DetailsActivity;
import com.example.soumit.grocerylist.Activities.ListActivity;
import com.example.soumit.grocerylist.Activities.MainActivity;
import com.example.soumit.grocerylist.Data.DatabaseHandler;
import com.example.soumit.grocerylist.Model.Grocery;
import com.example.soumit.grocerylist.R;

import java.util.List;

/**
 * Created by Soumit on 2/13/2018.
 */

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder> {

    private Context context;
    private List<Grocery> groceryItems;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RecyclerviewAdapter(Context context, List<Grocery> groceryItems) {
        this.context = context;
        this.groceryItems = groceryItems;
    }

    @Override
    public RecyclerviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerviewAdapter.ViewHolder holder, int position) {

        Grocery grocery = groceryItems.get(position);

        holder.groceryItemName.setText(grocery.getName());
        holder.quantiy.setText(grocery.getQuantity());
        holder.dateAdded.setText(grocery.getDateItemAdded());

    }

    @Override
    public int getItemCount() {
        return groceryItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView groceryItemName;
        public TextView quantiy;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;
        public int id;
//        public FloatingActionButton fab ;

        public ViewHolder(View itemView, Context cntxt) {
            super(itemView);
            context = cntxt;

            groceryItemName = (TextView) itemView.findViewById(R.id.name);
            quantiy = (TextView) itemView.findViewById(R.id.qty);
            dateAdded = (TextView) itemView.findViewById(R.id.dateAdded);

            editButton = (Button) itemView.findViewById(R.id.editButton);
            deleteButton = (Button) itemView.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    Grocery grocery = groceryItems.get(position);
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("name", grocery.getName());
                    intent.putExtra("quantity", grocery.getQuantity());
                    intent.putExtra("id", grocery.getId());
                    intent.putExtra("date", grocery.getDateItemAdded());
                    context.startActivity(intent);

                }
            });

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.editButton:
                    int position = getAdapterPosition();
                    Grocery grocery = groceryItems.get(position);

                    editItem(grocery);
                    break;
                case R.id.deleteButton:
                    position = getAdapterPosition();
                    grocery = groceryItems.get(position);

                    deleteItem(grocery.getId());
                    break;

                case R.id.fab:
                    addItem();
                    break;
            }
        }

        private void addItem(){
            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.popup, null);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            Button saveButton = (Button) view.findViewById(R.id.saveBtn);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveGroceryToDB(view);
                }
            });

        }

        private void saveGroceryToDB(View view) {

            Grocery grocery = new Grocery();

            final EditText groceryItem = (EditText) view.findViewById(R.id.item_id);
            final EditText groceryQuantity = (EditText) view.findViewById(R.id.quantity_id);

            String newGrocery = groceryItem.getText().toString();
            String newQuantity = groceryQuantity.getText().toString();

            grocery.setName(newGrocery);
            grocery.setQuantity(newQuantity);

            DatabaseHandler db = new DatabaseHandler(context);
            db.addGrocery(grocery);

            Snackbar.make(view, "Item saved !", Snackbar.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    Intent intent = new Intent(context, ListActivity.class);

                }
            }, 1000);

//        Log.d(TAG, "saveGroceryToDB: Item count : " + db.getGroceriesCount());
        }



        public void editItem(final Grocery grocery){
            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.popup, null);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            final EditText groceryItem = (EditText) view.findViewById(R.id.item_id);
            final EditText groceryQuantity = (EditText) view.findViewById(R.id.quantity_id);
            TextView title = (TextView) view.findViewById(R.id.title_id);
            Button saveButton = (Button) view.findViewById(R.id.saveBtn);

            title.setText("Edit grocery : ");

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseHandler db = new DatabaseHandler(context);

                    //updating here
                    grocery.setName(groceryItem.getText().toString());
                    grocery.setQuantity(groceryQuantity.getText().toString());

                    if(!groceryItem.getText().toString().isEmpty() &&
                            !groceryQuantity.getText().toString().isEmpty()){
                        db.updateGrocery(grocery);
                        notifyItemChanged(getAdapterPosition(), grocery);
                    }else {
                        Snackbar.make(view, "Add grocery and quantity", Snackbar.LENGTH_SHORT).show();
                    }

                    dialog.dismiss();

                }
            });

        }


        public void deleteItem(final int id){
            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialog, null);

            Button noButton = (Button) view.findViewById(R.id.noButton);
            Button yesButton = (Button) view.findViewById(R.id.yesButton);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseHandler db = new DatabaseHandler(context);

                    db.deleteGrocery(id);
                    groceryItems.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss();
                }
            });

        }

    }
}




























