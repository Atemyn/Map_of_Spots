package com.example.mapofspotsdrawer.model;

import androidx.annotation.NonNull;

public class SportType {
    private Integer id;

    private String name;

    private String transportName;

    public SportType() {
    }

    public SportType(Integer id, String name, String transportName) {
        this.id = id;
        this.name = name;
        this.transportName = transportName;
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

    public String getTransportName() {
        return transportName;
    }

    public void setTransportName(String transportName) {
        this.transportName = transportName;
    }

    @NonNull
    @Override
    public String toString() {
        return "SportType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", transportName='" + transportName + '\'' +
                '}';
    }
}
