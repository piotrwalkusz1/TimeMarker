package com.piotrwalkusz.timemarker.storage;

import com.piotrwalkusz.timemarker.storage.entity.ActivityEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ActivityRepository extends CrudRepository<ActivityEntity, Long> {

    ActivityEntity findByName(String name);

    ActivityEntity findById(long id);

    @Query("SELECT DISTINCT activity FROM ActivityEntity activity LEFT JOIN FETCH activity.timeMarkers")
    List<ActivityEntity> findAllEagerly();
}
