package com.mylife.refree;

import io.realm.RealmObject;

public class RealmclassEat_record extends RealmObject {
    String date;
    int when;
    String food;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getWhen() {
        return when;
    }

    public void setWhen(int when) {
        this.when = when;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }
}
