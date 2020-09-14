package com.mylife.refree;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class DataLoadTask extends AsyncTask<Void, Void, ArrayList<Ingredient>> {
    Context context;
    SharedPreferences prefs;
    ProgressBar progressBar;
    public boolean flag=true;
    public DataLoadTask(ProgressBar progressBar, SharedPreferences prefs, Context context){
        this.progressBar=progressBar;
        this.prefs=prefs;
        this.context=context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Ingredient> doInBackground(Void... voids) {
        DataParsing dataParsing=new DataParsing();
        dataParsing.getjsonfromUrl();
        ArrayList<Ingredient> result=dataParsing.startParsing();
        return result;
    }

    @Override
    protected void onPostExecute(final ArrayList<Ingredient> ingredients) {
        super.onPostExecute(ingredients);
//        Realm.init((SplashActivity)context);
//        RealmConfiguration realmConfiguration=new RealmConfiguration.Builder()
//                .schemaVersion(0)
//                .migration(new Migration())
//                .build();
//        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm=Realm.getDefaultInstance();
        final RealmResults<RealmclassIngredient> check=realm.where(RealmclassIngredient.class).findAll();
        if(check!=null){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    check.deleteAllFromRealm();
                }
            });
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for(int i=0; i<ingredients.size(); i++){
                    RealmclassIngredient resingre=realm.createObject(RealmclassIngredient.class);
                    resingre.setCat_num(ingredients.get(i).getCat_num());
                    resingre.setCategory(ingredients.get(i).getCategory());
                    resingre.setFood(ingredients.get(i).getFood());
                }
            }
        });
        prefs.edit().putBoolean("FirstDownload", false).apply();
        prefs.edit().putBoolean("Update", false).apply();
        ((SplashActivity)context).finish();
        context.startActivity(new Intent(context, LoginActivity.class));

    }
}
