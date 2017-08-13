package com.piotrwalkusz.timemarker.storage;

import com.piotrwalkusz.timemarker.storage.entity.ActivityEntity;
import com.piotrwalkusz.timemarker.storage.entity.TimeMarkerEntity;
import org.springframework.data.repository.CrudRepository;

public interface TimeMarkerRepository extends CrudRepository<TimeMarkerEntity, Long> {

    TimeMarkerEntity findFirstByActivityOrderByEndTimeDesc(ActivityEntity activity);
}
