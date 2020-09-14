package com.mylife.refree;

import androidx.annotation.Nullable;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class Migration implements RealmMigration {

//    @Override
//    public int hashCode() {
//        return 37;
//    }
//
//    @Override
//    public boolean equals(@Nullable Object obj) {
//        return (obj instanceof Migration);
//    }

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema=realm.getSchema();

        if(oldVersion==0){
            schema.create("RealmclassIngredient")
                    .addField("cat_num", int.class)
                    .addField("category", String.class)
                    .addField("food", String.class);
            schema.create("RealmclassMyingredient")
                    .addField("cat_num", int.class)
                    .addField("food", String.class)
                    .addField("register_date", String.class)
                    .addField("date", String.class)
                    .addField("cnt", String.class)
                    .addField("contain", int.class)
                    .addField("guitar", String.class);
            schema.create("RealmMyCookEachIngre")
                    .addField("ingredient", String.class);
            schema.create("RealmclassMyCook")
                    .addField("foodname", String.class)
                    .addField("foodbrief", String.class)
                    .addField("foodrecipe", String.class)
                    .addRealmListField("ingredients", schema.get("RealmMyCookEachIngre"));
            schema.create("RealmclassEat_record")
                    .addField("date", String.class)
                    .addField("when", int.class)
                    .addField("food", String.class);
            oldVersion++;
        }


    }
}
