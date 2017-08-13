package com.piotrwalkusz.timemarker.storage.entity;

import com.piotrwalkusz.timemarker.TimeMarker;
import com.sun.javafx.UnmodifiableArrayList;
import javafx.scene.paint.Color;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class ActivityEntity extends BaseEntity {

    private String name;
    private String color;

    @OneToMany(mappedBy = "activity", fetch = FetchType.LAZY)
    private List<TimeMarkerEntity> timeMarkers = Collections.emptyList();

    protected ActivityEntity() {}

    public ActivityEntity(String name, Color color) {
        this(name, color, Collections.emptyList());
    }

    public ActivityEntity(String name, Color color, List<TimeMarkerEntity> timeMarkers) {
        setName(name);
        setColor(color);
        setTimeMarkers(timeMarkers);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return Color.web(color);
    }

    public void setColor(Color color) {
        this.color = String.format("%02x%02x%02x", (int)(255 * color.getRed()), (int)(255 * color.getGreen()),
                (int)(255 * color.getBlue()));
    }

    public List<TimeMarkerEntity> getTimeMarkers() {
        return timeMarkers;
    }

    public void setTimeMarkers(List<TimeMarkerEntity> timeMarkers) {
        this.timeMarkers = timeMarkers;
    }
}
