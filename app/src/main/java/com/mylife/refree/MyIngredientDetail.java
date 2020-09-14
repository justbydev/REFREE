package com.mylife.refree;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MyIngredientDetail extends AppCompatActivity {
    TextView food_name, register_date, text_date, text_cnt, text_contain, text_guitar;
    EditText edit_date, edit_cnt, edit_guitar;
    Spinner edit_contain;
    Button fix, close;
    Intent intent;
    String food, register, validate, remain, contain, guitar;
    int origin_cat_num, origin_contain;
    String origin_food, origin_register_date, origin_date, origin_cnt, origin_guitar;
    int cat_num;
    int cnt;
    int button_flag=0;
    Realm realm;
    RealmclassMyingredient realmclassMyingredient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myingredientdetail);
//        Realm.init(this);
//        RealmConfiguration realmConfiguration=new RealmConfiguration.Builder()
//                .schemaVersion(0)
//                .migration(new Migration())
//                .build();
//        Realm.setDefaultConfiguration(realmConfiguration);
        realm=Realm.getDefaultInstance();

        setView();
        getIntentDetail();
        realmclassMyingredient=realm.where(RealmclassMyingredient.class)
                .equalTo("food", food)
                .equalTo("register_date", register)
                .equalTo("date", validate)
                .equalTo("contain", cnt)
                .equalTo("guitar", guitar)
                .findFirst();
        setString_to_View();
    }


    public void setView(){
        food_name=findViewById(R.id.food_name);
        register_date=findViewById(R.id.register_date);
        text_date=findViewById(R.id.text_date);
        text_cnt=findViewById(R.id.text_cnt);
        text_contain=findViewById(R.id.text_contain);
        text_guitar=findViewById(R.id.text_guitar);
        edit_date=findViewById(R.id.edit_date);
        edit_cnt=findViewById(R.id.edit_cnt);
        edit_guitar=findViewById(R.id.edit_guitar);
        edit_contain=findViewById(R.id.spinner_contain);
        fix=findViewById(R.id.fix);
        close=findViewById(R.id.close);
        fix.setOnClickListener(change_click);
        close.setOnClickListener(change_click);
    }
    public void getIntentDetail(){
        intent=getIntent();
        food=intent.getStringExtra("food");
        register=intent.getStringExtra("register_date");
        validate=intent.getStringExtra("validate_date");
        remain=intent.getStringExtra("remain");
        cnt=intent.getIntExtra("contain", 0);
        if(cnt==0){
            contain="냉장";
        }
        else if(cnt==1){
            contain="냉동";
        }
        else if(cnt==2){
            contain="실온";
        }
        guitar=intent.getStringExtra("guitar");
        cat_num=intent.getIntExtra("cat", 0);
        origin_cat_num=cat_num;
        origin_contain=cnt;
        origin_food=food;
        origin_register_date=register;
        origin_date=validate;
        origin_cnt=remain;
        origin_guitar=guitar;
    }
    public void setString_to_View(){
        food_name.setText(food);
        register_date.setText(register);
        text_date.setText(validate);
        text_cnt.setText(remain);
        text_contain.setText(contain);
        text_guitar.setText(guitar);
    }
    public void get_data(){
        validate=edit_date.getText().toString();
        remain=edit_cnt.getText().toString();
        cnt=edit_contain.getSelectedItemPosition();
        if(cnt==0){
            contain="냉장";
        }
        else if(cnt==1){
            contain="냉동";
        }
        else if(cnt==2){
            contain="실온";
        }
        guitar=edit_guitar.getText().toString();
    }
    public void set_data(){
        text_date.setText(validate);
        text_cnt.setText(remain);
        text_contain.setText(contain);
        text_guitar.setText(guitar);
    }
    public void change_food(){
        realm.beginTransaction();
        realmclassMyingredient.setDate(validate);
        realmclassMyingredient.setContain(cnt);
        realmclassMyingredient.setCnt(remain);
        realmclassMyingredient.setGuitar(guitar);
        realm.commitTransaction();
        //realm data amend
        RealmResults<RealmclassMyingredient> foods=realm.where(RealmclassMyingredient.class)
                .equalTo("cat_num", cat_num)
                .equalTo("contain", origin_contain)
                .findAll();
        ((MainActivity)MainActivity.maincontext).my_fragment.adapter.removeall_and_update(foods);
        //change recyclerview UI
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
        //firebase realtime data amend
    }
    public View.OnClickListener change_click=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id=v.getId();
            if(id==R.id.fix){
                if(button_flag==0){
                    button_flag=1;
                    fix.setText("저장");
                    close.setText("취소");
                    text_date.setVisibility(View.GONE);
                    text_cnt.setVisibility(View.GONE);
                    text_contain.setVisibility(View.GONE);
                    text_guitar.setVisibility(View.GONE);
                    edit_date.setVisibility(View.VISIBLE);
                    edit_cnt.setVisibility(View.VISIBLE);
                    edit_contain.setVisibility(View.VISIBLE);
                    edit_guitar.setVisibility(View.VISIBLE);
                    edit_date.setText(validate);
                    edit_cnt.setText(remain);
                    edit_guitar.setText(guitar);
                    edit_contain.setSelection(cnt);
                }
                else{
                    button_flag=0;
                    fix.setText("수정");
                    close.setText("닫기");
                    get_data();
                    set_data();
                    text_date.setVisibility(View.VISIBLE);
                    text_cnt.setVisibility(View.VISIBLE);
                    text_contain.setVisibility(View.VISIBLE);
                    text_guitar.setVisibility(View.VISIBLE);
                    edit_date.setVisibility(View.GONE);
                    edit_cnt.setVisibility(View.GONE);
                    edit_contain.setVisibility(View.GONE);
                    edit_guitar.setVisibility(View.GONE);

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
                    close.setText("닫기");
                    text_date.setVisibility(View.VISIBLE);
                    text_cnt.setVisibility(View.VISIBLE);
                    text_contain.setVisibility(View.VISIBLE);
                    text_guitar.setVisibility(View.VISIBLE);
                    edit_date.setVisibility(View.GONE);
                    edit_cnt.setVisibility(View.GONE);
                    edit_contain.setVisibility(View.GONE);
                    edit_guitar.setVisibility(View.GONE);
                    set_data();

                }
            }
        }
    };

    @Override
    public void onBackPressed() {
        finish();
    }
}
