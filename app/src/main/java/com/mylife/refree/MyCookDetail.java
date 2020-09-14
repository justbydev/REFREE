package com.mylife.refree;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MyCookDetail extends AppCompatActivity {
    RecyclerView ingre_recyclerview;
    TextView text_brief, text_recipe, cook_detail_name;
    EditText edit_brief, edit_recipe;
    Button fix, cancel;
    Context context;
    Intent intent;
    int button_flag=0;
    String foodname, foodbrief, foodrecipe;
    ArrayList<String> foodingredients;
    CookDetailRecyclerviewAdapter adapter;
    Realm realm;
    RealmclassMyCook realmclassMyCook;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycookdetail);
//        Realm.init(this);
//        RealmConfiguration realmConfiguration=new RealmConfiguration.Builder()
//                .schemaVersion(0)
//                .migration(new Migration())
//                .build();
//        Realm.setDefaultConfiguration(realmConfiguration);
        realm=Realm.getDefaultInstance();

        context=this;
        intent=getIntent();
        foodname=intent.getStringExtra("foodname");
        foodbrief=intent.getStringExtra("foodbrief");
        foodrecipe=intent.getStringExtra("foodrecipe");
        foodingredients=intent.getStringArrayListExtra("foodingredients");
        Init();

        realmclassMyCook=realm.where(RealmclassMyCook.class)
                .equalTo("foodname", foodname)
                .equalTo("foodbrief", foodbrief)
                .equalTo("foodrecipe", foodrecipe)
                .findFirst();
    }


    public void Init(){
        cook_detail_name=findViewById(R.id.cookdetail_name);
        ingre_recyclerview=findViewById(R.id.cook_ingre_detail_recyclerview);
        text_brief=findViewById(R.id.text_brief);
        text_recipe=findViewById(R.id.text_recipe);
        edit_brief=findViewById(R.id.edit_brief);
        edit_recipe=findViewById(R.id.edit_recipe);
        fix=findViewById(R.id.fix);
        cancel=findViewById(R.id.close);
        fix.setOnClickListener(change_click);
        cancel.setOnClickListener(change_click);
        cook_detail_name.setText(foodname);
        text_recipe.setText(foodrecipe);
        text_brief.setText(foodbrief);
        ingre_recyclerview.setLayoutManager(new GridLayoutManager(context, 3));
        adapter=new CookDetailRecyclerviewAdapter(foodingredients);
        ingre_recyclerview.setAdapter(adapter);
    }

    public View.OnClickListener change_click=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id=v.getId();
            if(id==R.id.fix){
                if(button_flag==0){
                    button_flag=1;
                    fix.setText("저장");
                    cancel.setText("취소");
                    text_brief.setVisibility(View.GONE);
                    text_recipe.setVisibility(View.GONE);
                    edit_brief.setVisibility(View.VISIBLE);
                    edit_recipe.setVisibility(View.VISIBLE);
                    edit_brief.setText(foodbrief);
                    edit_recipe.setText(foodrecipe);
                }
                else{
                    button_flag=0;
                    fix.setText("수정");
                    cancel.setText("닫기");
                    foodbrief=edit_brief.getText().toString();
                    foodrecipe=edit_recipe.getText().toString();
                    edit_brief.setVisibility(View.GONE);
                    edit_recipe.setVisibility(View.GONE);
                    text_brief.setVisibility(View.VISIBLE);
                    text_recipe.setVisibility(View.VISIBLE);
                    text_brief.setText(foodbrief);
                    text_recipe.setText(foodrecipe);
                }
            }
            else if(id==R.id.close){
                if(button_flag==0){
                    change_food();
                    onBackPressed();
                }
                else{
                    button_flag=0;
                    fix.setText("수정");
                    cancel.setText("닫기");
                    edit_brief.setVisibility(View.GONE);
                    edit_recipe.setVisibility(View.GONE);
                    text_brief.setVisibility(View.VISIBLE);
                    text_recipe.setVisibility(View.VISIBLE);
                    text_brief.setText(foodbrief);
                    text_recipe.setText(foodrecipe);
                }
            }
        }
    };


    public void change_food(){
        realm.beginTransaction();
        realmclassMyCook.setFoodbrief(foodbrief);
        realmclassMyCook.setFoodrecipe(foodrecipe);
        realm.commitTransaction();
        //change realm DB
        RealmResults<RealmclassMyCook> cooks=realm.where(RealmclassMyCook.class).findAll();
        ((MainActivity)MainActivity.maincontext).mycook_fragment.myCookAdapter.remove_and_update(cooks);
        //change recyclerview UI
        DatabaseReference databaseReferenc= FirebaseDatabase.getInstance().getReference("Mycook");
        databaseReferenc.getRef().removeValue();
        for(int i=0; i<cooks.size(); i++){
            MyCook now=new MyCook();
            now.setFoodname(cooks.get(i).getFoodname());
            now.setFoodrecipe(cooks.get(i).getFoodrecipe());
            now.setFoodbrief(cooks.get(i).getFoodbrief());
            ArrayList<String> ings=new ArrayList<>();
            for(int j=0; j<cooks.get(i).getIngredients().size(); j++){
                ings.add(cooks.get(i).getIngredients().get(j).getIngredient());
            }
            now.setIngredients(ings);
            databaseReferenc.push().setValue(now);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
