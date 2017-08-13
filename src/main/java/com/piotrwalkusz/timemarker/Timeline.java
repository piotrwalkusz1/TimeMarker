package com.piotrwalkusz.timemarker;

import com.piotrwalkusz.timemarker.time.ClockUtil;
import io.reactivex.Observable;
import javafx.scene.paint.Color;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Timeline {

    protected final List<Activity> activities = new ArrayList<>();

    protected TimeMarker lastTimeMarker;

    public Activity createActivity(String name, Color color) {
        Activity newActivity = new Activity(name, color);
        activities.add(newActivity);
        return newActivity;
    }

    public void createTimeMarker(Activity activity) {
        lastTimeMarker = activity.createTimeMarker(ClockUtil.now(), ClockUtil.now());
    }

    public void updateLastTimeMarker() {
        if (lastTimeMarker == null)
            throw new IllegalStateException("The last TimeMarket doesn't exist");

        lastTimeMarker = lastTimeMarker.updateEndTime(ClockUtil.now());
    }

    public final List<Activity> getActivities() {
        return Collections.unmodifiableList(activities);
    }

    public final List<TimeMarker> getSeenTimeMarkers() {
        return activities.stream()
                .flatMap(act -> act.getTimeMarkers().stream())
                .filter(tm -> tm.getEndTime().isAfter(ClockUtil.now().truncatedTo(ChronoUnit.DAYS)))
                .sorted(Comparator.comparing(TimeMarker::getStartTime))
                .collect(Collectors.toList());
    }

    public final boolean isAnyActivity() {
        return !activities.isEmpty();
    }

    public final Duration getTotalActivitiesTimeFrom(LocalDateTime time) {
        return activities.stream().map(x -> x.getTotalTimeFrom(time)).reduce(Duration.ZERO, Duration::plus);
    }
}
