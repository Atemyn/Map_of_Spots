package com.example.mapofspotsdrawer.model;

import androidx.annotation.NonNull;

public class SpotType {
    private Integer id;

    private String name;

    public SpotType() {
    }

    public SpotType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return "SpotType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
