package com.example.minioimplement.model;

import ch.qos.logback.core.spi.LifeCycle;
import lombok.Builder;

@Builder
public class Range {

    private final Long start;

    private final Long end;

    public long getRangeStart(){
        return start;
    }

    public long getRangeEnd(long fileSize) {
        return Math.min(end, fileSize - 1);
    }
    public static Range parseHttpRangeString(String httpRangeString, int defaultChunkSize){
        if (httpRangeString == null) {
            return Range.builder().start(0L).end(Integer.toUnsignedLong(defaultChunkSize)).build();
        }
        int dashIndex = httpRangeString.indexOf("-");
        long startRange = Long.parseLong(httpRangeString.substring(6, dashIndex));
        String endRangeString = httpRangeString.substring(dashIndex + 1);
        if (endRangeString.isEmpty()) {
            return Range.builder().start(startRange).end(startRange + defaultChunkSize).build();
        }
        long endRange = Long.parseLong(endRangeString);
        return Range.builder().start(startRange).end(endRange).build();
    }
}
