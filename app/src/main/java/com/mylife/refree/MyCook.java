package com.mylife.refree;

import java.util.ArrayList;

public class MyCook {
    String foodname;
    String foodbrief;
    String foodrecipe;
    ArrayList<String> ingredients;

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

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }
}
