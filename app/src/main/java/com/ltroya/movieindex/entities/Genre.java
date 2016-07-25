package com.ltroya.movieindex.entities;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Genre extends RealmObject {
    @PrimaryKey
    String id;
    String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * it's used for convert list to string
     */
    @Override
    public String toString() {
        return this.name;
    }
}
