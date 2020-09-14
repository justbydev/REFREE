package com.mylife.refree;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PossiblefoodRecyclerViewAdapter extends RecyclerView.Adapter {
    public ArrayList<String> possible_foods;
    static Context context;
    public PossiblefoodRecyclerViewAdapter(ArrayList<String> possible_foods){
        this.possible_foods=possible_foods;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView possible_food_name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            possible_food_name=itemView.findViewById(R.id.possible_food_name);
            context=itemView.getContext();
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.possible_food_item, parent, false);
        MyViewHolder vh=new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder=(MyViewHolder)holder;
        myViewHolder.possible_food_name.setText(possible_foods.get(position));

    }

    @Override
    public int getItemCount() {
        return possible_foods==null?0:possible_foods.size();
    }

    public void remove_and_update(ArrayList<String> possible_foods){
        if(this.possible_foods!=null){
            this.possible_foods=null;
        }
        this.possible_foods=possible_foods;
        notifyDataSetChanged();
    }
}
