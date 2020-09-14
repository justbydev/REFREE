package com.mylife.refree;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;

public class DataParsing {
    String dataurl;
    String jsonstring;
    public DataParsing(){
        dataurl="https://firebasestorage.googleapis.com/v0/b/refree-f824c.appspot.com/o/%EB%83%89%EC%9E%A5%EA%B3%A0%20%EC%9E%AC%EB%A3%8C.json?alt=media&token=dfe68eab-4474-4136-ae4a-4739789e8b74";
        jsonstring="";
    }
    public void getjsonfromUrl(){
        HttpURLConnection con=null;
        InputStreamReader isr=null;
        BufferedReader br=null;
        try{
            URL Url=new URL(dataurl);
            con=(HttpURLConnection)Url.openConnection();

            isr=new InputStreamReader(con.getInputStream(), "UTF-8");
            br=new BufferedReader(isr);

            String nowline=null;
            while((nowline=br.readLine())!=null){
                jsonstring+=nowline+"\n";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<Ingredient> startParsing() {

        List<String> category= Arrays.asList("채소", "과일", "육류", "수산", "유제품", "면/만두", "빵/떡", "두부/콩/계란", "음료", "튀김", "양념", "기타");
        ArrayList<Ingredient> result=new ArrayList<>();
        try{
            JSONObject jsonObject=new JSONObject(jsonstring);
            for(int i=0; i<category.size(); i++){
                JSONArray jsonArray=jsonObject.getJSONArray(category.get(i));
                for(int j=0; j<jsonArray.length(); j++){
                    Ingredient ingredient=new Ingredient();
                    ingredient.setCat_num(i);
                    ingredient.setCategory(category.get(i));
                    ingredient.setFood(jsonArray.getString(j));
                    result.add(ingredient);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
