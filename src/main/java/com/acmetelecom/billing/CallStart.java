package com.acmetelecom.billing;

import org.joda.time.DateTimeUtils;

public class CallStart extends CallEvent {
    public CallStart(String caller, String callee) {
        super(caller, callee, DateTimeUtils.currentTimeMillis());
    }
}
