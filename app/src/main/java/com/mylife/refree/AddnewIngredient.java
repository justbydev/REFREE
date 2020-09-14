package com.mylife.refree;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class AddnewIngredient extends AppCompatActivity {
    Spinner food_spinner;
    EditText ingre_date, ingre_number, edit_guitar;
    Button refre_up, refre_down, outside, ingre_cancel, ingre_add;
    Context context;
    public int cat;
    static Realm realm;
    public String food, date, cnt, guitar=null, now_date;
    public int contain=-1;
    public int main_cat, main_contain;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnewingredient);

//        Realm.init(this);
//        RealmConfiguration realmConfiguration=new RealmConfiguration.Builder()
//                .schemaVersion(0)
//                .migration(new Migration())
//                .build();
//        Realm.setDefaultConfiguration(realmConfiguration);
        realm=Realm.getDefaultInstance();

        Intent intent=getIntent();
        cat=intent.getIntExtra("category", -1);
        main_cat=intent.getIntExtra("main_cat", 0);
        main_contain=intent.getIntExtra("main_contain", 0);
        context=this;
        food_spinner=findViewById(R.id.food_spinner);
        ingre_date=findViewById(R.id.ingre_date);
        ingre_number=findViewById(R.id.ingre_number);
        edit_guitar=findViewById(R.id.edit_guitar);
        refre_up=findViewById(R.id.refre_up);
        refre_down=findViewById(R.id.refre_down);
        outside=findViewById(R.id.outside);
        ingre_cancel=findViewById(R.id.ingre_cancel);
        ingre_add=findViewById(R.id.ingre_add);


        RealmResults<RealmclassIngredient> foods=realm.where(RealmclassIngredient.class)
                .equalTo("cat_num", cat)
                .findAll();
        ArrayList<String> foods_array=new ArrayList<>();
        for(int i=0; i<foods.size(); i++){
            foods_array.add(foods.get(i).getFood());
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_item, foods_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        food_spinner.setAdapter(adapter);
        food=food_spinner.getSelectedItem().toString();
        food_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                food=food_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        refre_up.setOnClickListener(contain_way_pick);
        refre_down.setOnClickListener(contain_way_pick);
        outside.setOnClickListener(contain_way_pick);

        ingre_add.setOnClickListener(addnewfood);

        ingre_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public View.OnClickListener contain_way_pick= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id=v.getId();
            switch(id){
                case R.id.refre_up:
                    contain=0;
                    refre_up.setTextColor(Color.BLUE);
                    refre_down.setTextColor(Color.BLACK);
                    outside.setTextColor(Color.BLACK);
                    return;
                case R.id.refre_down:
                    contain=1;
                    refre_up.setTextColor(Color.BLACK);
                    refre_down.setTextColor(Color.BLUE);
                    outside.setTextColor(Color.BLACK);
                    return;
                case R.id.outside:
                    contain=2;
                    refre_up.setTextColor(Color.BLACK);
                    refre_down.setTextColor(Color.BLACK);
                    outside.setTextColor(Color.BLUE);
                    return;
            }
        }
    };
    public View.OnClickListener addnewfood=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            food=food_spinner.getSelectedItem().toString();
            date=ingre_date.getText().toString().trim();
            cnt=ingre_number.getText().toString().trim();
            guitar=edit_guitar.getText().toString().trim();
            if(TextUtils.isEmpty(date)){
                Toast.makeText(context, "기한을 적어주세요", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(cnt)){
                Toast.makeText(context, "수량을 적어주세요", Toast.LENGTH_SHORT).show();
                return;
            }
            if(contain==-1){
                Toast.makeText(context, "보관 방법을 선택해주세요", Toast.LENGTH_SHORT).show();
                return;
            }
            long now=System.currentTimeMillis();
            Date nowdate=new Date(now);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd");
            now_date=sdf.format(nowdate);
            //get current date
            Myingredient myingredient=new Myingredient();
            myingredient.setCat_num(cat);
            myingredient.setFood(food);
            myingredient.setCnt(cnt);
            myingredient.setContain(contain);
            myingredient.setGuitar(guitar);
            myingredient.setRegister_date(now_date);
            myingredient.setDate(date);
            databaseReference= FirebaseDatabase.getInstance().getReference("Myfood");
//            String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
//            email=email.replace('.', '-');
            databaseReference.push().setValue(myingredient);
            //store in firebase realtime database

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmclassMyingredient myingre=realm.createObject(RealmclassMyingredient.class);
                    myingre.setCat_num(cat);
                    myingre.setFood(food);
                    myingre.setCnt(cnt);
                    myingre.setContain(contain);
                    myingre.setGuitar(guitar);
                    myingre.setRegister_date(now_date);
                    myingre.setDate(date);
                }
            });
            //store in Realm inner DB

            if(main_cat==cat&&main_contain==contain){
                RealmResults<RealmclassMyingredient> foods=realm.where(RealmclassMyingredient.class)
                        .equalTo("cat_num", main_cat)
                        .equalTo("contain", main_contain)
                        .findAll();
                ((MainActivity)MainActivity.maincontext).my_fragment.adapter.removeall_and_update(foods);
            }

            finish();

        }
    };
}
