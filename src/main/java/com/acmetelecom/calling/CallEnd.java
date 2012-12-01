package com.acmetelecom.calling;

import org.joda.time.DateTimeUtils;

/**
 * Represents a call end event.
 */
public class CallEnd extends CallEvent {
    public CallEnd(String caller, String callee) {
        super(caller, callee, DateTimeUtils.currentTimeMillis());
    }
}
