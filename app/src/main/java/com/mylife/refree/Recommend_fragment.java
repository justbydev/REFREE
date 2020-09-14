package com.mylife.refree;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

public class Recommend_fragment extends Fragment {
    RecyclerView possible_cook_recyclerview, order_recyclerview;
    Button order_include_button, order_exclude_button;
    TextView recommend_food, cook_update;
    Context context;
    ArrayList<String> order_food;
    OrderfoodRecyclerviewAdapter orderadapter;
    PossiblefoodRecyclerViewAdapter possibleadapter;
    Realm realm;
    RealmResults<RealmclassMyCook> cooks;
    RealmResults<RealmclassMyingredient> myingredients;
    ArrayList<String> possible_makefood;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.recommend, container, false);
        context=getContext();
//        Realm.init(context);
//        RealmConfiguration realmConfiguration=new RealmConfiguration.Builder()
//                .schemaVersion(0)
//                .migration(new Migration())
//                .build();
//        Realm.setDefaultConfiguration(realmConfiguration);
        realm=Realm.getDefaultInstance();
        possible_makefood=new ArrayList<>();
        order_food=new ArrayList<>();
        cooks=realm.where(RealmclassMyCook.class).findAll();
        myingredients=realm.where(RealmclassMyingredient.class).findAll();

        Init(v);
        setting_possible_food();
        Init_recyclerview(v);

        return v;
    }
    public void Init(View v){
        possible_cook_recyclerview=v.findViewById(R.id.possible_cook_recyclerview);
        order_recyclerview=v.findViewById(R.id.order_recyclerview);
        order_include_button=v.findViewById(R.id.order_include_button);
        order_exclude_button=v.findViewById(R.id.order_exclude_button);
        recommend_food=v.findViewById(R.id.recommend_food);
        cook_update=v.findViewById(R.id.mycook_update);
        order_include_button.setOnClickListener(start_random_pick);
        order_exclude_button.setOnClickListener(start_random_pick);
        cook_update.setOnClickListener(update);
        order_food.add("햄버거");
        order_food.add("피자");
        order_food.add("치킨");
        order_food.add("빵");
        order_food.add("분식");
        order_food.add("도너츠");
        order_food.add("배달음식");
    }
    public void setting_possible_food(){
        for(int i=0; i<cooks.size(); i++){
            RealmList<RealmMyCookEachIngre> cook_ingre=cooks.get(i).getIngredients();
            int exist_flag=1;
            for(int j=0; j<cook_ingre.size(); j++){
                int flag=0;
                String nw_ingre=cook_ingre.get(j).getIngredient();
                for(int k=0; k<myingredients.size(); k++){
                    if(nw_ingre.equals(myingredients.get(k).getFood())){
                        flag=1;
                        break;
                    }
                }
                if(flag==0){
                    exist_flag=0;
                    break;
                }
            }
            if(exist_flag==1){
                possible_makefood.add(cooks.get(i).getFoodname());
            }
        }
    }
    public void Init_recyclerview(View v){
        order_recyclerview.setLayoutManager(new GridLayoutManager(context, 3));
        orderadapter=new OrderfoodRecyclerviewAdapter(order_food);
        order_recyclerview.setAdapter(orderadapter);

        possible_cook_recyclerview.setLayoutManager(new GridLayoutManager(context, 3));
        possibleadapter=new PossiblefoodRecyclerViewAdapter(possible_makefood);
        possible_cook_recyclerview.setAdapter(possibleadapter);
    }

    public View.OnClickListener start_random_pick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id=v.getId();
            if(id==R.id.order_include_button){
                int randomNum=(int)(Math.random()*(possible_makefood.size()+order_food.size()));
                if(randomNum>=possible_makefood.size()){
                    randomNum=randomNum-possible_makefood.size();
                    recommend_food.setText(order_food.get(randomNum));
                }
                else{
                    recommend_food.setText(possible_makefood.get(randomNum));
                }

            }
            else if(id==R.id.order_exclude_button){
                if(possible_makefood.size()==0){
                    recommend_food.setText("현재 가능한 요리가 없습니다");
                }
                else{
                    int randomNum=(int)(Math.random()*possible_makefood.size());
                    recommend_food.setText(possible_makefood.get(randomNum));
                }
            }
        }
    };

    public View.OnClickListener update=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(possible_makefood!=null){
                possible_makefood=null;
            }
            possible_makefood=new ArrayList<>();
            setting_possible_food();
            possibleadapter.remove_and_update(possible_makefood);
        }
    };
}
