package com.piotrwalkusz.timemarker;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeMarker {

    private final Activity activity;
    private final LocalDateTime start;
    private final LocalDateTime end;

    TimeMarker(Activity activity, LocalDateTime start, LocalDateTime end) {
        if (activity == null)
            throw new IllegalArgumentException("Activity mustn't be null");
        if (start == null)
            throw new IllegalArgumentException("Start time mustn't be null");
        if (end == null)
            throw new IllegalArgumentException("End time mustn't be null");
        if (start.isAfter(end))
            throw new IllegalArgumentException("Start time mustn't be after end time");

        this.activity = activity;
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStartTime() {
        return start;
    }

    public LocalDateTime getEndTime() {
        return end;
    }

    public Duration getDuration() {
        return Duration.between(start, end);
    }

    public Activity getActivity() {
        return activity;
    }

    public TimeMarker updateEndTime(LocalDateTime newEndTime) {
        return activity.extendTimeMarkerTo(this, newEndTime);
    }
}
