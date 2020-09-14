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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MyIngredientRecyclerviewAdapter extends RecyclerView.Adapter{
    private RealmResults<RealmclassMyingredient> foods;
    static Context context;
    public Realm realm;
    public MyIngredientRecyclerviewAdapter(RealmResults<RealmclassMyingredient> foods){
        this.foods=foods;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name, remain, dday;
        public LinearLayout each_item;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.ingredient_name);
            remain=itemView.findViewById(R.id.ingredient_remain);
            dday=itemView.findViewById(R.id.ingredient_dday);
            each_item=itemView.findViewById(R.id.each_item_layout);
            context=itemView.getContext();
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myingredient_recyclerview_item, parent, false);
        MyViewHolder vh=new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder=(MyViewHolder)holder;
        myViewHolder.name.setText(foods.get(position).getFood());
        myViewHolder.remain.setText(foods.get(position).getCnt());
        try {
            String Dday=getDday(foods.get(position).getDate());
            myViewHolder.dday.setText(Dday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final int pos=position;
        myViewHolder.each_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, MyIngredientDetail.class);
                intent.putExtra("food", foods.get(pos).getFood());
                intent.putExtra("register_date", foods.get(pos).getRegister_date());
                intent.putExtra("validate_date", foods.get(pos).getDate());
                intent.putExtra("remain", foods.get(pos).getCnt());
                intent.putExtra("contain", foods.get(pos).getContain());
                intent.putExtra("guitar", foods.get(pos).getGuitar());
                intent.putExtra("cat", foods.get(pos).getCat_num());
                context.startActivity(intent);
            }
        });
        myViewHolder.each_item.setOnLongClickListener(new View.OnLongClickListener() {
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
                        int delete_cat_num=foods.get(pos).getCat_num();
                        int delete_contain=foods.get(pos).getContain();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                RealmclassMyingredient now=realm.where(RealmclassMyingredient.class)
                                        .equalTo("cat_num", foods.get(pos).getCat_num())
                                        .equalTo("contain", foods.get(pos).getContain())
                                        .equalTo("food", foods.get(pos).getFood())
                                        .equalTo("cnt", foods.get(pos).getCnt())
                                        .equalTo("register_date", foods.get(pos).getRegister_date())
                                        .equalTo("date", foods.get(pos).getDate())
                                        .equalTo("guitar", foods.get(pos).getGuitar())
                                        .findFirst();
                                if(now!=null){
                                    now.deleteFromRealm();
                                }
                            }
                        });
                        RealmResults<RealmclassMyingredient> foods=realm.where(RealmclassMyingredient.class)
                                .equalTo("cat_num", delete_cat_num)
                                .equalTo("contain", delete_contain)
                                .findAll();
                        removeall_and_update(foods);
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
        return foods==null?0:foods.size();
    }

    public void removeall_and_update(RealmResults<RealmclassMyingredient> foods){
        if(this.foods!=null){
            this.foods=null;
        }
        this.foods=foods;
        notifyDataSetChanged();
    }

    public void delete_from_firebase(){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Myfood");
        databaseReference.getRef().removeValue();
        RealmResults<RealmclassMyingredient> foodall=realm.where(RealmclassMyingredient.class)
                .findAll();
        for(int i=0; i<foodall.size(); i++){
            Myingredient myingredient=new Myingredient();
            myingredient.setCat_num(foodall.get(i).getCat_num());
            myingredient.setFood(foodall.get(i).getFood());
            myingredient.setRegister_date(foodall.get(i).getRegister_date());
            myingredient.setDate(foodall.get(i).getDate());
            myingredient.setCnt(foodall.get(i).getCnt());
            myingredient.setContain(foodall.get(i).getContain());
            myingredient.setGuitar(foodall.get(i).getGuitar());
            databaseReference.push().setValue(myingredient);
        }
    }
    public String getDday(String date) throws ParseException {
        int ONE_DAY=24*60*60*1000;
        long now=(System.currentTimeMillis()-ONE_DAY)/ONE_DAY;
        Date mDate=new SimpleDateFormat("yyyy.MM.dd").parse(date);
        Calendar cal=Calendar.getInstance();
        cal.setTime(mDate);
        long want=cal.getTimeInMillis()/ONE_DAY;
        long result=want-now;
        String strFormat;
        if(result>0){
            strFormat="D-%d";
        }
        else if(result==0){
            strFormat="D-Day";
        }
        else{
            result=now-want;
            strFormat="D+%d";
        }
        String strCount=(String.format(strFormat, result));
        return strCount;
    }
}
