package com.example.demo.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Value;

public class DateConverterUtil {

    @Value("${app.timezone}") // gets value of variable defined in the resources/application.properties
    private String timeZone;

    // Convert UTC LocalDateTime to Sri Lanka's LocalTime (Asia/Colombo)
    public LocalDateTime convertUtcToSriLankaTime(LocalDateTime utcDateTime) {
        ZonedDateTime utcZonedDateTime = utcDateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime sriLankaTime = utcZonedDateTime.withZoneSameInstant(ZoneId.of(timeZone));
        return sriLankaTime.toLocalDateTime();
    }
}
