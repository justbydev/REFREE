package com.mylife.refree;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SelectRecyclerviewAdapter extends RecyclerView.Adapter {
    ArrayList<String> select_ingredients;
    static Context context;
    public SelectRecyclerviewAdapter(ArrayList<String> select){
        this.select_ingredients=select;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView select_item;
        TextView select_delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            select_item=itemView.findViewById(R.id.cook_select_name);
            select_delete=itemView.findViewById(R.id.select_delete);
            context=itemView.getContext();
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_recyclerview_item, parent, false);
        MyViewHolder vh=new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder=(MyViewHolder)holder;
        myViewHolder.select_item.setText(select_ingredients.get(position));
        final int pos=position;
        myViewHolder.select_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setMessage("삭제할까요?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        select_ingredients.remove(pos);
                        remove_and_update(select_ingredients);
                    }
                });
                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return select_ingredients==null?0:select_ingredients.size();
    }

    public void remove_and_update(ArrayList<String> now){
        if(this.select_ingredients!=null){
            this.select_ingredients=null;
        }
        this.select_ingredients=now;
        notifyDataSetChanged();
    }
    public void remove_and_add(String nw){
        this.select_ingredients.add(nw);
        notifyDataSetChanged();
    }
    public ArrayList<String> getselectIngre(){
        return this.select_ingredients;
    }
}
