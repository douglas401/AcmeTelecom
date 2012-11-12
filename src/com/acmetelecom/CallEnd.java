package com.acmetelecom;

import org.joda.time.DateTimeUtils;

public class CallEnd extends CallEvent {
    public CallEnd(String caller, String callee) {
        super(caller, callee, DateTimeUtils.currentTimeMillis());
    }
}
