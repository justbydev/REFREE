package com.mylife.refree;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Record_fragment extends Fragment {
    CalendarView calendar;
    TextView breakfast, lunch, dinner;
    Realm realm;
    String click_date;
    RealmclassEat_record b, l, dn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.record, container, false);

//        Realm.init(getContext());
//        RealmConfiguration realmConfiguration=new RealmConfiguration.Builder()
//                .schemaVersion(0)
//                .migration(new Migration())
//                .build();
//        Realm.setDefaultConfiguration(realmConfiguration);
        realm=Realm.getDefaultInstance();


        calendar=v.findViewById(R.id.calendar);
        breakfast=v.findViewById(R.id.breakfast);
        lunch=v.findViewById(R.id.lunch);
        dinner=v.findViewById(R.id.dinner);
        breakfast.setOnClickListener(input_dialog);
        lunch.setOnClickListener(input_dialog);
        dinner.setOnClickListener(input_dialog);

        Calendar cal=Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH)+1;
        int day=cal.get(Calendar.DAY_OF_MONTH);
        click_date=year+Integer.toString(month)+day;
        b=realm.where(RealmclassEat_record.class)
                .equalTo("date", click_date)
                .equalTo("when", 0).findFirst();
        l=realm.where(RealmclassEat_record.class)
                .equalTo("date", click_date)
                .equalTo("when", 1).findFirst();
        dn=realm.where(RealmclassEat_record.class)
                .equalTo("date", click_date)
                .equalTo("when", 2).findFirst();
        if(b==null){
            breakfast.setText("입력없음");
        }
        else{
            breakfast.setText(b.getFood());
        }
        if(l==null){
            lunch.setText("입력없음");
        }
        else{
            lunch.setText(l.getFood());
        }
        if(dn==null){
            dinner.setText("입력없음");
        }
        else{
            dinner.setText(dn.getFood());
        }
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                click_date= year +Integer.toString(month+1)+ dayOfMonth;
                b=realm.where(RealmclassEat_record.class)
                        .equalTo("date", click_date)
                        .equalTo("when", 0).findFirst();
                l=realm.where(RealmclassEat_record.class)
                        .equalTo("date", click_date)
                        .equalTo("when", 1).findFirst();
                dn=realm.where(RealmclassEat_record.class)
                        .equalTo("date", click_date)
                        .equalTo("when", 2).findFirst();
                if(b==null){
                    breakfast.setText("입력없음");
                }
                else{
                    breakfast.setText(b.getFood());
                }
                if(l==null){
                    lunch.setText("입력없음");
                }
                else{
                    lunch.setText(l.getFood());
                }
                if(dn==null){
                    dinner.setText("입력없음");
                }
                else{
                    dinner.setText(dn.getFood());
                }
            }
        });

        return v;
    }

    public View.OnClickListener input_dialog=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id=v.getId();

            if(id==R.id.breakfast){
                make_dialog(click_date, 0);
            }
            else if(id==R.id.lunch){
                make_dialog(click_date, 1);
            }
            else if(id==R.id.dinner){
                make_dialog(click_date, 2);
            }
        }
    };
    public void make_dialog(String date, int when){
        final String d=date;
        final int w=when;
        final EditText input=new EditText(getActivity());
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setMessage("무엇을 드셨나요?");
        builder.setView(input);
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("등록", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String food=input.getText().toString();
                RealmclassEat_record eat=realm.where(RealmclassEat_record.class)
                        .equalTo("date", d)
                        .equalTo("when", w)
                        .findFirst();
                if(eat==null){
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            RealmclassEat_record now=realm.createObject(RealmclassEat_record.class);
                            now.setDate(d);
                            now.setFood(food);
                            now.setWhen(w);
                        }
                    });
                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Record");
                    Eat_record now=new Eat_record();
                    now.setDate(d);
                    now.setFood(food);
                    now.setWhen(w);
                    databaseReference.push().setValue(now);
                    if(w==0){
                        breakfast.setText(food);
                    }
                    else if(w==1){
                        lunch.setText(food);
                    } else if (w == 2){
                        dinner.setText(food);
                    }
                }
                else if(eat!=null){
                    realm.beginTransaction();
                    eat.setFood(food);
                    realm.commitTransaction();
                    final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Record");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                String json=snapshot.getValue().toString();
                                try {
                                    JSONObject jsonObject=new JSONObject(json);
                                    String now_d=jsonObject.getString("date");
                                    int now_w=jsonObject.getInt("when");
                                    if(now_w==w&&now_d.equals(d)){
                                        snapshot.getRef().removeValue();
                                        Eat_record now=new Eat_record();
                                        now.setDate(d);
                                        now.setWhen(w);
                                        now.setFood(food);
                                        databaseReference.push().setValue(now);
                                        break;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    if(w==0){
                        breakfast.setText(food);
                    }
                    else if(w==1){
                        lunch.setText(food);
                    } else if (w == 2){
                        dinner.setText(food);
                    }
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
