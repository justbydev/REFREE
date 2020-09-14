package com.mylife.refree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    My_fragment my_fragment;
    Mycook_fragment mycook_fragment;
    Record_fragment record_fragment;
    Recommend_fragment recommend_fragment;
    TextView login;
    FirebaseAuth firebaseAuth;
    public static Context maincontext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Realm.init(this);
//        RealmConfiguration realmConfiguration=new RealmConfiguration.Builder()
//                .schemaVersion(0)
//                .build();
//        Realm.setDefaultConfiguration(realmConfiguration);
        bottomNavigationView=findViewById(R.id.bottomnavigation);
        firebaseAuth=FirebaseAuth.getInstance();
        login=findViewById(R.id.login);
        maincontext=this;

        if(firebaseAuth.getCurrentUser()!=null){
            login.setText("로그아웃");
        }
        else{
            login.setText("로그인");
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent=new Intent(maincontext, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });

        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        my_fragment=new My_fragment();
        getSupportFragmentManager().beginTransaction().add(R.id.main_layout, my_fragment).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int select=item.getItemId();
                switch(select) {
                    case R.id.mypage:
                        if (my_fragment == null) {
                            my_fragment = new My_fragment();
                            getSupportFragmentManager().beginTransaction().add(R.id.main_layout, my_fragment).commit();
                        }
                        if (my_fragment != null) {
                            getSupportFragmentManager().beginTransaction().show(my_fragment).commit();
                        }
                        if (mycook_fragment != null) {
                            getSupportFragmentManager().beginTransaction().hide(mycook_fragment).commit();
                        }
                        if (record_fragment != null) {
                            getSupportFragmentManager().beginTransaction().hide(record_fragment).commit();
                        }
                        if (recommend_fragment != null) {
                            getSupportFragmentManager().beginTransaction().hide(recommend_fragment).commit();
                        }
                        break;
                    case R.id.mycook:
                        if (mycook_fragment == null) {
                            mycook_fragment = new Mycook_fragment();
                            getSupportFragmentManager().beginTransaction().add(R.id.main_layout, mycook_fragment).commit();
                        }
                        if (mycook_fragment != null) {
                            getSupportFragmentManager().beginTransaction().show(mycook_fragment).commit();
                        }
                        if (my_fragment != null) {
                            getSupportFragmentManager().beginTransaction().hide(my_fragment).commit();
                        }
                        if (record_fragment != null) {
                            getSupportFragmentManager().beginTransaction().hide(record_fragment).commit();
                        }
                        if (recommend_fragment != null) {
                            getSupportFragmentManager().beginTransaction().hide(recommend_fragment).commit();
                        }
                        break;
                    case R.id.record:
                        if (record_fragment == null) {
                            record_fragment = new Record_fragment();
                            getSupportFragmentManager().beginTransaction().add(R.id.main_layout, record_fragment).commit();
                        }
                        if (record_fragment != null) {
                            getSupportFragmentManager().beginTransaction().show(record_fragment).commit();
                        }
                        if (my_fragment != null) {
                            getSupportFragmentManager().beginTransaction().hide(my_fragment).commit();
                        }
                        if (mycook_fragment != null) {
                            getSupportFragmentManager().beginTransaction().hide(mycook_fragment).commit();
                        }
                        if (recommend_fragment != null) {
                            getSupportFragmentManager().beginTransaction().hide(recommend_fragment).commit();
                        }
                        break;
                    case R.id.recommend:
                        if (recommend_fragment == null) {
                            recommend_fragment = new Recommend_fragment();
                            getSupportFragmentManager().beginTransaction().add(R.id.main_layout, recommend_fragment).commit();
                        }
                        if (recommend_fragment != null) {
                            getSupportFragmentManager().beginTransaction().show(recommend_fragment).commit();
                        }
                        if (my_fragment != null) {
                            getSupportFragmentManager().beginTransaction().hide(my_fragment).commit();
                        }
                        if (mycook_fragment != null) {
                            getSupportFragmentManager().beginTransaction().hide(mycook_fragment).commit();
                        }
                        if (record_fragment != null) {
                            getSupportFragmentManager().beginTransaction().hide(record_fragment).commit();
                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(maincontext);
        builder.setCancelable(false)
                .setMessage("앱을 종료하시겠어요?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}