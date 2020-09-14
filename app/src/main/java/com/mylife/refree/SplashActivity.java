package com.mylife.refree;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmSchema;

public class SplashActivity extends AppCompatActivity {
    private Context context;
    ProgressBar progressBar;
    SharedPreferences prefs;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
//        Realm.init(this);
//        RealmConfiguration realmConfiguration=new RealmConfiguration.Builder()
//                .name("Myrefree.realm")
//                .schemaVersion(0)
//                .migration(new Migration())
//                .build();
//        Realm.setDefaultConfiguration(realmConfiguration);

        progressBar=findViewById(R.id.progress_circular);
        context=this;

        prefs=getSharedPreferences("REFREE", MODE_PRIVATE);
        boolean isFirstDownload=prefs.getBoolean("FirstDownload", true);
        boolean isUpdate=prefs.getBoolean("Update", true);
        if(isFirstDownload){//First download
            ConnectivityManager manager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                NetworkCapabilities capabilities=manager.getNetworkCapabilities(manager.getActiveNetwork());
                if(capabilities!=null){
                    Realm.init(context);
                    RealmConfiguration realmConfiguration=new RealmConfiguration.Builder()
                            .name("Myrefree.realm")
                            .schemaVersion(0)
                            .migration(new Migration())
                            .build();
                    Realm.setDefaultConfiguration(realmConfiguration);
                    DataLoadTask dataLoadTask=new DataLoadTask(progressBar, prefs, context);
                    dataLoadTask.execute();
                }
                else{
                    Toast.makeText(this, "네트워크 연결을 찾을 수 없습니다.", Toast.LENGTH_LONG).show();
                }
            }
            else{
                NetworkInfo activeNetwork=manager!=null?manager.getActiveNetworkInfo():null;
                if(activeNetwork!=null){
                    Realm.init(context);
                    RealmConfiguration realmConfiguration=new RealmConfiguration.Builder()
                            .name("Myrefree.realm")
                            .schemaVersion(0)
                            .migration(new Migration())
                            .build();
                    Realm.setDefaultConfiguration(realmConfiguration);
                    DataLoadTask dataLoadTask=new DataLoadTask(progressBar, prefs, context);
                    dataLoadTask.execute();
                }
                else{
                    Toast.makeText(this, "네트워크 연결을 찾을 수 없습니다", Toast.LENGTH_LONG).show();
                }
            }
        }
        else{//already download the data
            //여기에 update 관련 코드 추가 필요
            //최신 버전이 있고 갖고 있는 버전과 일치하지 않는다면
            //update를 진행시켜주어야 하고 그때, Datadownload도 실행되며 Realm file의 version 교체도 이루어지게 된다(configuration 설정)
            //version 교체는 new Migration class를 통해 migration이 진행되게 한다
            if(isUpdate){
                ConnectivityManager manager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    NetworkCapabilities capabilities=manager.getNetworkCapabilities(manager.getActiveNetwork());
                    if(capabilities!=null){
                        Realm.init(context);
                        RealmConfiguration realmConfiguration=new RealmConfiguration.Builder()
                                .name("Myrefree.realm")
                                .schemaVersion(0)
                                .build();
                        Realm.setDefaultConfiguration(realmConfiguration);
                        DataLoadTask dataLoadTask=new DataLoadTask(progressBar, prefs, context);
                        dataLoadTask.execute();
                    }
                    else{
                        Toast.makeText(this, "네트워크 연결을 찾을 수 없습니다.", Toast.LENGTH_LONG).show();
                    }
                }
                else{

                    NetworkInfo activeNetwork=manager!=null?manager.getActiveNetworkInfo():null;
                    if(activeNetwork!=null){
                        Realm.init(context);
                        RealmConfiguration realmConfiguration=new RealmConfiguration.Builder()
                                .name("Myrefree.realm")
                                .schemaVersion(0)
                                .build();
                        Realm.setDefaultConfiguration(realmConfiguration);
                        DataLoadTask dataLoadTask=new DataLoadTask(progressBar, prefs, context);
                        dataLoadTask.execute();
                    }
                    else{
                        Toast.makeText(this, "네트워크 연결을 찾을 수 없습니다", Toast.LENGTH_LONG).show();
                    }
                }
            }
            Realm.init(context);
            RealmConfiguration realmConfiguration=new RealmConfiguration.Builder()
                    .name("Myrefree.realm")
                    .schemaVersion(0)
                    .build();
            Realm.setDefaultConfiguration(realmConfiguration);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
            }, 2000);
        }
    }
}
