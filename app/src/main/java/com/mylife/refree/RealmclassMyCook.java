package com.mylife.refree;

import io.realm.RealmList;
import io.realm.RealmObject;

public class RealmclassMyCook extends RealmObject {
    String foodname;
    String foodbrief;
    String foodrecipe;
    RealmList<RealmMyCookEachIngre> ingredients;

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getFoodbrief() {
        return foodbrief;
    }

    public void setFoodbrief(String foodbrief) {
        this.foodbrief = foodbrief;
    }

    public String getFoodrecipe() {
        return foodrecipe;
    }

    public void setFoodrecipe(String foodrecipe) {
        this.foodrecipe = foodrecipe;
    }

    public RealmList<RealmMyCookEachIngre> getIngredients() {
        return ingredients;
    }

    public void setIngredients(RealmList<RealmMyCookEachIngre> ingredients) {
        this.ingredients = ingredients;
    }
}
