package com.codewithankit.mygrocerylist.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.TaskExecutor;
import androidx.recyclerview.widget.RecyclerView;

import com.codewithankit.mygrocerylist.Activities.DetailsActivity;
import com.codewithankit.mygrocerylist.Activities.ListActivity;
import com.codewithankit.mygrocerylist.Model.Grocery;
import com.codewithankit.mygrocerylist.R;
import com.codewithankit.mygrocerylist.data.DatabaseHandler;
import com.google.android.material.snackbar.Snackbar;

import java.util.BitSet;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Grocery> groceryList;
    private AlertDialog.Builder alertDialoBuilder;
    private LayoutInflater inflator;
    private AlertDialog dialog;

    public RecyclerViewAdapter(Context context, List<Grocery> groceryList) {
        this.context = context;
        this.groceryList = groceryList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row , parent , false);
        return new ViewHolder(v , context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        //here the binding of the data and the views takes place.
        Grocery grocery = groceryList.get(position);

        holder.groceryItemName.setText(grocery.getName());
        holder.quantity.setText(grocery.getQuantity());
        holder.dateAdded.setText(grocery.getDateItemAdded());

    }

    @Override
    public int getItemCount() {
        return groceryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView groceryItemName;
        public TextView quantity;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;
        public int id;

        public ViewHolder(@NonNull View itemView , Context ctx) {
            super(itemView);

            context = ctx;
            groceryItemName = (TextView)itemView.findViewById(R.id.name);
            quantity = (TextView) itemView.findViewById(R.id.quantity);
            dateAdded = (TextView)itemView.findViewById(R.id.dataAdded);
            editButton = (Button) itemView.findViewById(R.id.editButton);
            deleteButton = (Button) itemView.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //go to next screen.
//                    int pos = getAdapterPosition();
//                    Grocery x = groceryList.get(pos);
//
//                    Intent intent = new Intent(context , DetailsActivity.class);
//                    intent.putExtra("name" , x.getName());
//                    intent.putExtra("quantity" , x.getQuantity());
//                    intent.putExtra("date" , x.getDateItemAdded());
//                    intent.putExtra("id" , x.getId());
//                    context.startActivity(intent);
//                }
//            });
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.editButton:
                    int pos = getAdapterPosition();
                    Grocery x = groceryList.get(pos);
                    updateItem(x);
                    break;

                case R.id.deleteButton:
                    int position = getAdapterPosition();
                    Grocery tmp = groceryList.get(position);
                    deleteItem(tmp.getId());
                    break;
            }
        }

        public void deleteItem(final int id){
            //now here before deleting we should creare a dialog by inflating the confirmation_dialog.xml file.
            alertDialoBuilder = new AlertDialog.Builder(context);
            inflator = LayoutInflater.from(context);
            View v = inflator.inflate(R.layout.confirmation_dialog , null);

            Button nobtn = (Button)v.findViewById(R.id.noButtonID);
            Button yesbtn  = (Button)v.findViewById(R.id.yesButtonID);

            alertDialoBuilder.setView(v);
            dialog = alertDialoBuilder.create();
            dialog.show();

            nobtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            yesbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //now here we have to delete the item from the database.
                    DatabaseHandler db = new DatabaseHandler(context);
                    db.deleteGrocery(id);
                    groceryList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    dialog.dismiss();
                }
            });
        }

        public void updateItem(final Grocery g){
            alertDialoBuilder = new AlertDialog.Builder(context);
            inflator = LayoutInflater.from(context);

            View v = inflator.inflate(R.layout.popup , null);

            TextView title = (TextView) v.findViewById(R.id.title);
            title.setText("Edit Grocery Item");


            final EditText gitem = (EditText)v.findViewById(R.id.grocery_item);
            final EditText gqty = (EditText) v.findViewById(R.id.grocery_quantity);
            Button savebtn = (Button) v.findViewById(R.id.saveButton);

            alertDialoBuilder.setView(v);
            dialog = alertDialoBuilder.create();
            dialog.show();

            savebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler(context);

                    g.setName(gitem.getText().toString());
                    g.setQuantity(gqty.getText().toString());

                    if(!gitem.getText().toString().equals("") && !gqty.getText().toString().equals("")) {

                        db.updateGrocery(g);
                        notifyItemChanged(getAdapterPosition() , g);
                        context.startActivity(new Intent(context , ListActivity.class));
                        dialog.dismiss();
                    }else{
                        Snackbar.make(v , "Something is Missing :(" , Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
