package com.example.mapofspotsdrawer.model;

import com.yandex.mapkit.geometry.Point;

public class Placemark {
    private final Point position;
    private final String labelText;

    public Placemark(Point position, String labelText) {
        this.position = position;
        this.labelText = labelText;
    }

    public Point getPosition() {
        return position;
    }

    public String getLabelText() {
        return labelText;
    }
}
