package com.piotrwalkusz.timemarker.storage.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class TimeMarkerEntity extends BaseEntity {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @ManyToOne(fetch = FetchType.LAZY)
    private ActivityEntity activity;

    protected TimeMarkerEntity() {}

    public TimeMarkerEntity(LocalDateTime startTime, LocalDateTime endTime, ActivityEntity activity) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.activity = activity;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
