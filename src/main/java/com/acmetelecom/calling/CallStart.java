package com.acmetelecom.calling;

import org.joda.time.DateTimeUtils;

/**
 * Represents a call start event.
 */
public class CallStart extends CallEvent {
    public CallStart(String caller, String callee) {
        super(caller, callee, DateTimeUtils.currentTimeMillis());
    }
}
