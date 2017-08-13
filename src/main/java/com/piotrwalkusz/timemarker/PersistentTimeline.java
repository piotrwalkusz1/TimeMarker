package com.piotrwalkusz.timemarker;

import com.piotrwalkusz.timemarker.storage.ActivityRepository;
import com.piotrwalkusz.timemarker.storage.DAOManager;
import com.piotrwalkusz.timemarker.storage.TimeMarkerRepository;
import com.piotrwalkusz.timemarker.storage.entity.ActivityEntity;
import com.piotrwalkusz.timemarker.storage.entity.TimeMarkerEntity;
import javafx.scene.paint.Color;

public class PersistentTimeline extends Timeline {

    private final ActivityRepository activityRepository;
    private final TimeMarkerRepository timeMarkerRepository;

    public PersistentTimeline() {
        activityRepository = DAOManager.getActivityRepository();
        timeMarkerRepository = DAOManager.getTimeMarkerRepository();

        for (ActivityEntity activityEntity : activityRepository.findAllEagerly()) {
            Activity activity = new Activity(activityEntity.getName(), activityEntity.getColor());
            for (TimeMarkerEntity timeMarkerEntity : activityEntity.getTimeMarkers()) {
                activity.createTimeMarker(timeMarkerEntity.getStartTime(), timeMarkerEntity.getEndTime());
            }
            activities.add(activity);
        }
    }

    @Override
    public Activity createActivity(String name, Color color) {
        Activity newActivity = super.createActivity(name, color);

        activityRepository.save(new ActivityEntity(newActivity.getName(), newActivity.getColor()));

        return newActivity;
    }

    @Override
    public void createTimeMarker(Activity activity) {
        super.createTimeMarker(activity);

        ActivityEntity activityEntity = activityRepository.findByName(activity.getName());
        timeMarkerRepository.save(
                new TimeMarkerEntity(lastTimeMarker.getStartTime(), lastTimeMarker.getEndTime(), activityEntity));
    }

    @Override
    public void updateLastTimeMarker() {
        super.updateLastTimeMarker();

        ActivityEntity activityEntity = activityRepository.findByName(lastTimeMarker.getActivity().getName());
        TimeMarkerEntity timeMarkerEntity =
                timeMarkerRepository.findFirstByActivityOrderByEndTimeDesc(activityEntity);
        timeMarkerEntity.setEndTime(lastTimeMarker.getEndTime());
        timeMarkerRepository.save(timeMarkerEntity);
    }
}
