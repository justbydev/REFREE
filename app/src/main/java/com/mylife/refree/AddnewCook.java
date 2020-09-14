package com.mylife.refree;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

public class AddnewCook extends AppCompatActivity {
    public static Context cook_context;
    EditText edit_cook_name, recipe, brief;
    TextView c0,c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11;
    RecyclerView cook_ingre_recyclerview, cook_myingre_recyclerview;
    CookingredientRecyclerviewAdapter adapter;
    SelectRecyclerviewAdapter selectadapter;
    Button cook_cancel, cook_add;
    ArrayList<TextView> category;
    Realm realm;
    RealmResults<RealmclassIngredient> ingredients;
    ArrayList<String> select_ingredients;
    int cat_num;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnewcook);
        cook_context=this;
        Init();
//        Realm.init(this);
//        RealmConfiguration realmConfiguration=new RealmConfiguration.Builder()
//                .schemaVersion(0)
//                .migration(new Migration())
//                .build();
//        Realm.setDefaultConfiguration(realmConfiguration);
        realm=Realm.getDefaultInstance();
        Init_recyclerview();

    }

    public void Init(){
        category=new ArrayList<>();
        select_ingredients=new ArrayList<>();
        edit_cook_name=findViewById(R.id.edit_cook_name);
        recipe=findViewById(R.id.recipe);
        brief=findViewById(R.id.brief);
        c0=findViewById(R.id.c0);
        c1=findViewById(R.id.c1);
        c2=findViewById(R.id.c2);
        c3=findViewById(R.id.c3);
        c4=findViewById(R.id.c4);
        c5=findViewById(R.id.c5);
        c6=findViewById(R.id.c6);
        c7=findViewById(R.id.c7);
        c8=findViewById(R.id.c8);
        c9=findViewById(R.id.c9);
        c10=findViewById(R.id.c10);
        c11=findViewById(R.id.c11);
        category.add(c0);
        category.add(c1);
        category.add(c2);
        category.add(c3);
        category.add(c4);
        category.add(c5);
        category.add(c6);
        category.add(c7);
        category.add(c8);
        category.add(c9);
        category.add(c10);
        category.add(c11);
        for(int i=0; i<12; i++){
            category.get(i).setOnClickListener(categorypick);
        }
        cook_ingre_recyclerview=findViewById(R.id.cook_ingre_recyclerview);
        cook_myingre_recyclerview=findViewById(R.id.cook_myingre_recyclerview);
        c0.setTextColor(Color.BLUE);
        cat_num=0;
        cook_cancel=findViewById(R.id.cook_cancel);
        cook_add=findViewById(R.id.cook_add);
        cook_cancel.setOnClickListener(finish_make_cook);
        cook_add.setOnClickListener(finish_make_cook);
    }
    public void Init_recyclerview(){
        ingredients=realm.where(RealmclassIngredient.class)
                .equalTo("cat_num", cat_num)
                .findAll();
        cook_ingre_recyclerview.setLayoutManager(new GridLayoutManager(cook_context, 3));
        adapter=new CookingredientRecyclerviewAdapter(ingredients);
        cook_ingre_recyclerview.setAdapter(adapter);

        cook_myingre_recyclerview.setLayoutManager(new GridLayoutManager(cook_context, 3));
        selectadapter=new SelectRecyclerviewAdapter(select_ingredients);
        cook_myingre_recyclerview.setAdapter(selectadapter);
    }
    public View.OnClickListener categorypick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id=v.getId();
            for(int i=0; i<12; i++){
                if(id==category.get(i).getId()){
                    cat_num=i;
                    changecolor(cat_num);
                    ingredients=realm.where(RealmclassIngredient.class)
                            .equalTo("cat_num", cat_num)
                            .findAll();
                    adapter.removeall_and_update(ingredients);
                    return;
                }
            }
        }
    };
    public void changecolor(int num){
        for(int i=0; i<12; i++){
            if(i==num){
                category.get(i).setTextColor(Color.BLUE);
            }
            else{
                category.get(i).setTextColor(Color.BLACK);
            }
        }
    }
    public View.OnClickListener finish_make_cook=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id=v.getId();
            if(id==R.id.cook_cancel){
                finish();
            }
            else if(id==R.id.cook_add){
                if(select_ingredients!=null){
                    select_ingredients=null;
                }
                select_ingredients=selectadapter.getselectIngre();
                final String foodname=edit_cook_name.getText().toString();
                final String foodbrief=brief.getText().toString();
                final String foodrecipe=recipe.getText().toString();
                if(TextUtils.isEmpty(foodname)){
                    Toast.makeText(cook_context, "요리 이름을 적어주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(select_ingredients.size()==0){
                    Toast.makeText(cook_context, "재료를 한 개 이상 선택해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                final MyCook myCook=new MyCook();
                myCook.setFoodname(foodname);
                if(TextUtils.isEmpty(foodbrief)){
                    myCook.setFoodbrief("맛있는 "+foodname);
                }
                else{
                    myCook.setFoodbrief(foodbrief);
                }
                myCook.setFoodrecipe(foodrecipe);
                myCook.setIngredients(select_ingredients);
                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Mycook");
                databaseReference.push().setValue(myCook);
                //realtime database
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmclassMyCook mycook=realm.createObject(RealmclassMyCook.class);
                        if(TextUtils.isEmpty(foodbrief)){
                            mycook.setFoodbrief("맛있는 "+foodname);
                        }
                        else{
                            mycook.setFoodbrief(foodbrief);
                        }
                        mycook.setFoodname(foodname);
                        mycook.setFoodrecipe(foodrecipe);
                        for(int i=0; i<select_ingredients.size(); i++){
                            RealmMyCookEachIngre eachIngre=realm.createObject(RealmMyCookEachIngre.class);
                            eachIngre.ingredient=select_ingredients.get(i);
                            mycook.ingredients.add(eachIngre);
                        }
                    }
                });
                //Realm DB
                RealmResults<RealmclassMyCook> cooks=realm.where(RealmclassMyCook.class).findAll();
                ((MainActivity)MainActivity.maincontext).mycook_fragment.myCookAdapter.remove_and_update(cooks);
                //change mycook recyclerview UI
                finish();
            }
        }
    };
}
