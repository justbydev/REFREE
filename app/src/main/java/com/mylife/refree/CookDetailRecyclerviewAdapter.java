package com.mylife.refree;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CookDetailRecyclerviewAdapter extends RecyclerView.Adapter {
    ArrayList<String> ingredients;
    public CookDetailRecyclerviewAdapter(ArrayList<String> ingredients){
        this.ingredients=ingredients;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView cook_ingredient_name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cook_ingredient_name=itemView.findViewById(R.id.cook_ingredient_name);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cookdetail_ingredients_item, parent, false);
        MyViewHolder vh=new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder=(MyViewHolder)holder;
        myViewHolder.cook_ingredient_name.setText(ingredients.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredients==null?0:ingredients.size();
    }
}
