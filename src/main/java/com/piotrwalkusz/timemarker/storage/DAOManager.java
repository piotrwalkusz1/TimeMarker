package com.piotrwalkusz.timemarker.storage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public final class DAOManager {

    private static ApplicationContext context;

    private DAOManager() {}

    static {
        context = new AnnotationConfigApplicationContext(RepositoryConfig.class);
    }

    public static ActivityRepository getActivityRepository() {
        return context.getBean(ActivityRepository.class);
    }

    public static TimeMarkerRepository getTimeMarkerRepository() {
        return context.getBean(TimeMarkerRepository.class);
    }
}
