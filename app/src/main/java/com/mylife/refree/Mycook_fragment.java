package com.mylife.refree;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class Mycook_fragment extends Fragment {
    TextView cookadd_button;
    EditText cook_search;
    Button cook_search_button;
    RecyclerView cook_reyclcerview;
    public static Context mycook_context;
    Realm realm;
    MyCookAdapter myCookAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.mycook, container, false);

        cookadd_button=v.findViewById(R.id.cookadd_button);
        cook_search=v.findViewById(R.id.cook_search);
        cook_search_button=v.findViewById(R.id.cook_search_button);
        cook_reyclcerview=v.findViewById(R.id.cook_recyclerview);
        mycook_context=getContext();

//        Realm.init(getContext());
//        RealmConfiguration realmConfiguration=new RealmConfiguration.Builder()
//                .schemaVersion(0)
//                .migration(new Migration())
//                .build();
//        Realm.setDefaultConfiguration(realmConfiguration);
        realm=Realm.getDefaultInstance();
        cookadd_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mycook_context, AddnewCook.class));
            }
        });

        cook_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input=cook_search.getText().toString().trim();
                if(TextUtils.isEmpty(input)){
                    Toast.makeText(mycook_context, "입력된 검색어가 없습니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                input=cook_search.getText().toString().trim();
                int flag=0;
                RealmResults<RealmclassMyCook> cooks=realm.where(RealmclassMyCook.class).findAll();
                for(int i=0; i<cooks.size(); i++){
                    if(input.equals(cooks.get(i).getFoodname())){
                        flag=1;
                        Intent intent=new Intent(mycook_context, MyCookDetail.class);
                        intent.putExtra("foodname", cooks.get(i).getFoodname());
                        ArrayList<String> ings=new ArrayList<>();
                        for(int j=0; j<cooks.get(i).getIngredients().size(); j++){
                            ings.add(cooks.get(i).getIngredients().get(j).getIngredient());
                        }
                        intent.putExtra("foodingredients", ings);
                        intent.putExtra("foodbrief", cooks.get(i).getFoodbrief());
                        intent.putExtra("foodrecipe", cooks.get(i).getFoodrecipe());
                        mycook_context.startActivity(intent);
                        break;
                    }
                }
                if(flag==0){
                    Toast.makeText(mycook_context, "기록되지 않은 요리입니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RealmResults<RealmclassMyCook> mycook=realm.where(RealmclassMyCook.class).findAll();
        cook_reyclcerview.setLayoutManager(new GridLayoutManager(mycook_context, 2));
        myCookAdapter=new MyCookAdapter(mycook);
        cook_reyclcerview.setAdapter(myCookAdapter);

        return v;
    }
}
