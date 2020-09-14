package com.mylife.refree;

import io.realm.RealmObject;

public class RealmMyCookEachIngre extends RealmObject {
    String ingredient;

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}
