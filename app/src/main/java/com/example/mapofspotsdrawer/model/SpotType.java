package com.example.mapofspotsdrawer.model;

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

    @Override
    public String toString() {
        return "SpotType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
