package com.mylife.refree;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderfoodRecyclerviewAdapter extends RecyclerView.Adapter {
    public ArrayList<String> out_foods;
    static Context context;
    public OrderfoodRecyclerviewAdapter(ArrayList<String> out_foods){
        this.out_foods=out_foods;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView order_food_name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            order_food_name=itemView.findViewById(R.id.order_food_name);
            context=itemView.getContext();
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_food_item, parent, false);
        MyViewHolder vh=new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder=(MyViewHolder)holder;
        myViewHolder.order_food_name.setText(out_foods.get(position));
    }

    @Override
    public int getItemCount() {
        return out_foods==null?0:out_foods.size();
    }
}
