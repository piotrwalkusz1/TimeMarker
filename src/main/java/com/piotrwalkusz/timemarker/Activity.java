package com.piotrwalkusz.timemarker;

import javafx.scene.paint.Color;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Activity {

    private final String name;
    private final Color color;

    private final List<TimeMarker> timeMarkers = new ArrayList<>();

    public Activity(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public TimeMarker createTimeMarker(LocalDateTime start) {
        return createTimeMarker(start, start);
    }

    public TimeMarker createTimeMarker(LocalDateTime start, LocalDateTime end) {
        TimeMarker newTimeMarker = new TimeMarker(this, start, end);
        timeMarkers.add(newTimeMarker);
        return newTimeMarker;
    }

    public TimeMarker extendTimeMarkerTo(TimeMarker oldTimeMarker, LocalDateTime newEndTime) {
        if (oldTimeMarker.getActivity() != this)
            throw new IllegalArgumentException("This TimeMarker doesn't belong to this activity");
        if (oldTimeMarker.getEndTime().isAfter(newEndTime))
            throw new IllegalArgumentException("New end time must be after old end time");

        timeMarkers.remove(oldTimeMarker);

        return createTimeMarker(oldTimeMarker.getStartTime(), newEndTime);
    }

    public Duration getTotalTime() {
        return timeMarkers.stream().map(TimeMarker::getDuration).reduce(Duration.ZERO, Duration::plus);
    }

    public Duration getTotalTimeFrom(LocalDateTime from) {
        return timeMarkers.stream().filter(x -> x.getEndTime().isAfter(from)).map(x -> {
            LocalDateTime laterTime = x.getStartTime().isAfter(from) ? x.getStartTime() : from;
            return Duration.between(laterTime, x.getEndTime());
        }).reduce(Duration.ZERO, Duration::plus);
    }

    public List<TimeMarker> getTimeMarkers() {
        return Collections.unmodifiableList(timeMarkers);
    }

    @Override
    public String toString() {
        return name;
    }
}
