package com.example.demo.filter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.Marker;

import static net.logstash.logback.marker.Markers.append;

public class FileJsonLogAuditFilter extends Filter<Object> {

    @Override
    public FilterReply decide(Object eventObject) {
        LoggingEvent event = (LoggingEvent) eventObject;
        Level level = event.getLevel();
        Marker marker = event.getMarker();

        if (level != Level.ERROR) {
            if (marker != null && marker.contains(append("type", "audit"))) {
                return FilterReply.ACCEPT;
            } else {
                return FilterReply.DENY;
            }
        }
        return FilterReply.DENY;
    }
}
