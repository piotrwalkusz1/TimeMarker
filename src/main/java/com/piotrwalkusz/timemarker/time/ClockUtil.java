package com.piotrwalkusz.timemarker.time;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.function.Supplier;

public class ClockUtil extends Clock {

    private static final ClockUtil instance = new ClockUtil();

    private Supplier<Instant> timeFunction = Clock.systemDefaultZone()::instant;

    private ClockUtil() {}

    public static void setTimeFunction(Supplier<Instant> timeFunction) {
        instance.timeFunction = timeFunction;
    }

    public static Clock getClock() {
        return instance;
    }

    public static LocalDateTime now() {
        return LocalDateTime.now(instance);
    }

    @Override
    public ZoneId getZone() {
        return ZoneId.systemDefault();
    }

    @Override
    public Clock withZone(ZoneId zoneId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Instant instant() {
        return timeFunction.get();
    }
}
