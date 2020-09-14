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

import io.realm.RealmResults;

public class CookingredientRecyclerviewAdapter extends RecyclerView.Adapter {
    private RealmResults<RealmclassIngredient> foods;
    static Context context;
    public CookingredientRecyclerviewAdapter(RealmResults<RealmclassIngredient> foods){
        this.foods=foods;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView ingre_name;
        public TextView new_add;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ingre_name=itemView.findViewById(R.id.cook_ingre_name);
            new_add=itemView.findViewById(R.id.new_add);
            context=itemView.getContext();
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cook_total_ingre_recyclerview_item, parent, false);
        MyViewHolder vh=new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder=(MyViewHolder)holder;
        myViewHolder.ingre_name.setText(foods.get(position).getFood());
        final int pos=position;
        myViewHolder.new_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setMessage("추가할까요?");
                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((AddnewCook)AddnewCook.cook_context).selectadapter.remove_and_add(foods.get(pos).getFood());
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return foods==null?0:foods.size();
    }

    public void removeall_and_update(RealmResults<RealmclassIngredient> foods){
        if(this.foods!=null){
            this.foods=null;
        }
        this.foods=foods;
        notifyDataSetChanged();
    }
}
