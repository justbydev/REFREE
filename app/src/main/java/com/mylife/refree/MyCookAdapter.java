package com.mylife.refree;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MyCookAdapter extends RecyclerView.Adapter {
    private RealmResults<RealmclassMyCook> cooks;
    static Context context;
    public Realm realm;
    public MyCookAdapter(RealmResults<RealmclassMyCook> cooks){
        this.cooks=cooks;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView cook_name;
        TextView cook_brief;
        LinearLayout each_cook;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cook_name=itemView.findViewById(R.id.cook_name);
            cook_brief=itemView.findViewById(R.id.cook_brief);
            each_cook=itemView.findViewById(R.id.each_cook);
            context=itemView.getContext();
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cook_recyclerview_item, parent, false);
        MyViewHolder vh=new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder=(MyViewHolder)holder;
        myViewHolder.cook_name.setText(cooks.get(position).getFoodname());
        myViewHolder.cook_brief.setText(cooks.get(position).getFoodbrief());
        final int pos=position;
        myViewHolder.each_cook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, MyCookDetail.class);
                intent.putExtra("foodname", cooks.get(pos).getFoodname());
                ArrayList<String> ings=new ArrayList<>();
                for(int i=0; i<cooks.get(pos).getIngredients().size(); i++){
                    ings.add(cooks.get(pos).getIngredients().get(i).getIngredient());
                }
                intent.putExtra("foodingredients", ings);
                intent.putExtra("foodbrief", cooks.get(pos).getFoodbrief());
                intent.putExtra("foodrecipe", cooks.get(pos).getFoodrecipe());
                context.startActivity(intent);
            }
        });
        myViewHolder.each_cook.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setMessage("삭제할까요?");
                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Realm.init(context);
//                        RealmConfiguration realmConfiguration=new RealmConfiguration.Builder()
//                                .schemaVersion(0)
//                                .migration(new Migration())
//                                .build();
//                        Realm.setDefaultConfiguration(realmConfiguration);
                        realm=Realm.getDefaultInstance();
                        final String delete_name=cooks.get(pos).getFoodname();
                        final String delete_brief=cooks.get(pos).getFoodbrief();
                        final String delete_recipe=cooks.get(pos).getFoodrecipe();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                RealmclassMyCook now=realm.where(RealmclassMyCook.class)
                                        .equalTo("foodname", delete_name)
                                        .equalTo("foodbrief", delete_brief)
                                        .equalTo("foodrecipe", delete_recipe)
                                        .findFirst();
                                if(now!=null){
                                    now.deleteFromRealm();
                                }
                            }
                        });
                        RealmResults<RealmclassMyCook> cooks=realm.where(RealmclassMyCook.class).findAll();
                        remove_and_update(cooks);
                        delete_from_firebase();
                    }
                });
                builder.show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return cooks==null?0:cooks.size();
    }

    public void remove_and_update(RealmResults<RealmclassMyCook> cooks){
        if(this.cooks!=null){
            this.cooks=null;
        }
        this.cooks=cooks;
        notifyDataSetChanged();
    }
    public void delete_from_firebase(){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Mycook");
        databaseReference.getRef().removeValue();
        RealmResults<RealmclassMyCook> cookall=realm.where(RealmclassMyCook.class).findAll();
        for(int i=0; i<cookall.size(); i++){
            MyCook now=new MyCook();
            now.setFoodname(cookall.get(i).getFoodname());
            now.setFoodbrief(cookall.get(i).getFoodbrief());
            now.setFoodrecipe(cookall.get(i).getFoodrecipe());
            ArrayList<String> ingredients=new ArrayList<>();
            for(int j=0; j<cookall.get(i).getIngredients().size(); j++){
                ingredients.add(cookall.get(i).getIngredients().get(j).getIngredient());
            }
            now.setIngredients(ingredients);
            databaseReference.push().setValue(now);
        }
    }
}
