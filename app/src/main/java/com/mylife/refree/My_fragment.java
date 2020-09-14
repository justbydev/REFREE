package com.mylife.refree;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class My_fragment extends Fragment {
    TextView add_button;
    TextView c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11;
    TextView f1, f2, f3;
    RecyclerView food_recyclerview;
    EditText edit_search;
    Button edit_button;
    MyIngredientRecyclerviewAdapter adapter;
    RealmResults<RealmclassMyingredient> myingredients;
    Context context;
    Realm realm;
    public int cat_num=-1;
    public int main_cat_num=0;
    public int main_contain_num=0;
    ArrayList<Button> cat_button;
    ArrayList<TextView> main_cat;
    ArrayList<TextView> main_contain;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.myfragment, container, false);

        context=getContext();
//        Realm.init(context);
//        RealmConfiguration realmConfiguration=new RealmConfiguration.Builder()
//                .schemaVersion(0)
//                .build();
        realm=Realm.getDefaultInstance();

        cat_button=new ArrayList<>();
        main_cat=new ArrayList<>();
        main_contain=new ArrayList<>();
        setView(v);
        Init_recyclerview();


        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_popup();
            }
        });


        return v;
    }

    public void setView(View v){
        add_button=v.findViewById(R.id.add_button);
        c0=v.findViewById(R.id.c0);
        c1=v.findViewById(R.id.c1);
        c2=v.findViewById(R.id.c2);
        c3=v.findViewById(R.id.c3);
        c4=v.findViewById(R.id.c4);
        c5=v.findViewById(R.id.c5);
        c6=v.findViewById(R.id.c6);
        c7=v.findViewById(R.id.c7);
        c8=v.findViewById(R.id.c8);
        c9=v.findViewById(R.id.c9);
        c10=v.findViewById(R.id.c10);
        c11=v.findViewById(R.id.c11);
        main_cat.add(c0);main_cat.add(c1);main_cat.add(c2);main_cat.add(c3);
        main_cat.add(c4);main_cat.add(c5);main_cat.add(c6);main_cat.add(c7);
        main_cat.add(c8);main_cat.add(c9);main_cat.add(c10);main_cat.add(c11);
        f1=v.findViewById(R.id.f1);
        f2=v.findViewById(R.id.f2);
        f3=v.findViewById(R.id.f3);
        main_contain.add(f1);main_contain.add(f2);main_contain.add(f3);
        food_recyclerview=v.findViewById(R.id.food_recyclerview);
        c0.setTextColor(Color.BLUE);
        f1.setTextColor(Color.BLUE);
        for(int i=0; i<12; i++){
            main_cat.get(i).setOnClickListener(main_category_pick);
        }
        for(int i=0; i<3; i++){
            main_contain.get(i).setOnClickListener(main_contain_pick);
        }
        edit_search=v.findViewById(R.id.edit_search);
        edit_button=v.findViewById(R.id.button_search);
        edit_button.setOnClickListener(myingredient_search);
    }


    public void Init_recyclerview(){
        myingredients=realm.where(RealmclassMyingredient.class)
                .equalTo("cat_num", main_cat_num)
                .equalTo("contain", main_contain_num)
                .findAll();
        food_recyclerview.setLayoutManager(new GridLayoutManager(context, 3));
        adapter=new MyIngredientRecyclerviewAdapter(myingredients);
        food_recyclerview.setAdapter(adapter);
    }

    public View.OnClickListener main_category_pick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id=v.getId();
            for(int i=0; i<12; i++){
                if(id==main_cat.get(i).getId()){
                    main_cat.get(i).setTextColor(Color.BLUE);
                    main_cat_num=i;
                }
                else{
                    main_cat.get(i).setTextColor(Color.BLACK);
                }
            }
            myingredients=realm.where(RealmclassMyingredient.class)
                    .equalTo("cat_num", main_cat_num)
                    .equalTo("contain", main_contain_num)
                    .findAll();
            adapter.removeall_and_update(myingredients);
        }
    };

    public View.OnClickListener main_contain_pick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id=v.getId();
            for(int i=0; i<3; i++){
                if(id==main_contain.get(i).getId()){
                    main_contain.get(i).setTextColor(Color.BLUE);
                    main_contain_num=i;
                }
                else{
                    main_contain.get(i).setTextColor(Color.BLACK);
                }
            }
            myingredients=realm.where(RealmclassMyingredient.class)
                    .equalTo("cat_num", main_cat_num)
                    .equalTo("contain", main_contain_num)
                    .findAll();
            adapter.removeall_and_update(myingredients);
        }
    };

    public void add_popup(){
        LinearLayout category=(LinearLayout)View.inflate(context, R.layout.category_pick, null);
        if(cat_button.size()!=0){
            cat_button.clear();
        }
        Button vegetable=category.findViewById(R.id.vegetable);
        Button fruit=category.findViewById(R.id.fruit);
        Button meat=category.findViewById(R.id.meat);
        Button water=category.findViewById(R.id.water);
        Button dairy_prod=category.findViewById(R.id.dairy_prod);
        Button noodle=category.findViewById(R.id.noodle);
        Button bread=category.findViewById(R.id.bread);
        Button bean=category.findViewById(R.id.bean);
        Button drink=category.findViewById(R.id.drink);
        Button seasoning=category.findViewById(R.id.seasoning);
        Button fried=category.findViewById(R.id.fried);
        Button guitar=category.findViewById(R.id.guitar);
        vegetable.setOnClickListener(categorypick);
        fruit.setOnClickListener(categorypick);
        meat.setOnClickListener(categorypick);
        water.setOnClickListener(categorypick);
        dairy_prod.setOnClickListener(categorypick);
        noodle.setOnClickListener(categorypick);
        bread.setOnClickListener(categorypick);
        bean.setOnClickListener(categorypick);
        drink.setOnClickListener(categorypick);
        seasoning.setOnClickListener(categorypick);
        fried.setOnClickListener(categorypick);
        guitar.setOnClickListener(categorypick);
        cat_button.add(vegetable);
        cat_button.add(fruit);
        cat_button.add(meat);
        cat_button.add(water);
        cat_button.add(dairy_prod);
        cat_button.add(noodle);
        cat_button.add(bread);
        cat_button.add(bean);
        cat_button.add(drink);
        cat_button.add(fried);
        cat_button.add(seasoning);
        cat_button.add(guitar);
        final AlertDialog categorydialog=new AlertDialog.Builder(context)
                .setView(category)
                .setCancelable(false)
                .setPositiveButton("확인", null)
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cat_num=-1;
                        dialog.dismiss();
                    }
                })
                .create();
        categorydialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button categoryok=categorydialog.getButton(AlertDialog.BUTTON_POSITIVE);
                categoryok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(cat_num==-1){
                            Toast.makeText(context, "종류를 선택해주세요", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            int tmp=cat_num;
                            cat_num=-1;
                            categorydialog.dismiss();
                            Intent intent=new Intent(context, AddnewIngredient.class);
                            intent.putExtra("category", tmp);
                            intent.putExtra("main_cat", main_cat_num);
                            intent.putExtra("main_contain", main_contain_num);
                            context.startActivity(intent);

                        }
                    }
                });
            }
        });
        categorydialog.show();
    }
    public void changecolor(int flag){
        for(int i=0; i<12; i++){
            if(i==flag){
                cat_button.get(i).setTextColor(Color.BLUE);
            }
            else{
                cat_button.get(i).setTextColor(Color.BLACK);
            }
        }
    }
    public View.OnClickListener categorypick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id=v.getId();
            switch(id){
                case R.id.vegetable:
                    cat_num=0;
                    changecolor(cat_num);
                    return;
                case R.id.fruit:
                    cat_num=1;
                    changecolor(cat_num);
                    return;
                case R.id.meat:
                    cat_num=2;
                    changecolor(cat_num);
                    return;
                case R.id.water:
                    cat_num=3;
                    changecolor(cat_num);
                    return;
                case R.id.dairy_prod:
                    cat_num=4;
                    changecolor(cat_num);
                    return;
                case R.id.noodle:
                    cat_num=5;
                    changecolor(cat_num);
                    return;
                case R.id.bread:
                    cat_num=6;
                    changecolor(cat_num);
                    return;
                case R.id.bean:
                    cat_num=7;
                    changecolor(cat_num);
                    return;
                case R.id.drink:
                    cat_num=8;
                    changecolor(cat_num);
                    return;
                case R.id.fried:
                    cat_num=9;
                    changecolor(cat_num);
                    return;
                case R.id.seasoning:
                    cat_num=10;
                    changecolor(cat_num);
                    return;
                case R.id.guitar:
                    cat_num=11;
                    changecolor(cat_num);
                    return;
            }
        }
    };

    public View.OnClickListener myingredient_search=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String input=edit_search.getText().toString().trim();
            if(TextUtils.isEmpty(input)){
                Toast.makeText(context, "입력된 검색어가 없습니다", Toast.LENGTH_SHORT).show();
                return;
            }
            RealmResults<RealmclassMyingredient> ingre_all=realm.where(RealmclassMyingredient.class).findAll();
            int flag=0;
            for(int i=0; i<ingre_all.size(); i++){
                if(input.equals(ingre_all.get(i).getFood())){
                    flag=1;
                    Intent intent=new Intent(context, MyIngredientDetail.class);
                    intent.putExtra("food", ingre_all.get(i).getFood());
                    intent.putExtra("register_date", ingre_all.get(i).getRegister_date());
                    intent.putExtra("validate_date", ingre_all.get(i).getDate());
                    intent.putExtra("remain", ingre_all.get(i).getCnt());
                    intent.putExtra("contain", ingre_all.get(i).getContain());
                    intent.putExtra("guitar", ingre_all.get(i).getGuitar());
                    intent.putExtra("cat", ingre_all.get(i).getCat_num());
                    main_cat.get(main_cat_num).setTextColor(Color.BLACK);
                    main_contain.get(main_contain_num).setTextColor(Color.BLACK);
                    main_cat_num=ingre_all.get(i).getCat_num();
                    main_contain_num=ingre_all.get(i).getContain();
                    main_cat.get(main_cat_num).setTextColor(Color.BLUE);
                    main_contain.get(main_contain_num).setTextColor(Color.BLUE);
                    context.startActivity(intent);
                    break;
                }
            }
            if(flag==0){
                Toast.makeText(context, "현재 냉장고에 없거나 기록되어 있지 않습니다", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
