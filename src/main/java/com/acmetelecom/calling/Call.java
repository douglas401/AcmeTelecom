package com.acmetelecom.calling;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  Represents a call, with the set of start and end event
 */
public class Call {
    private CallEvent start;
    private CallEvent end;

    /**
     * Initialises a call
     * @param start the start call event
     * @param end   the end call event
     */
    public Call(CallEvent start, CallEvent end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Get the callee of the call
     * @return callee in String
     */
    public String callee() {
        return start.getCallee();
    }

    /**
     * Get the duration of the call time in second
     * @return call duration in second
     */
    public int durationSeconds() {
        return (int) (((end.time() - start.time()) / 1000));
    }

    /**
     * Get the date of the call in String
     * @return String of the call start date
     */
    public String date() {
        return SimpleDateFormat.getInstance().format(new Date(start.time()));
    }

    /**
     * Get the start date of the call
     * @return the call start date
     */
    public Date startTime() {
        return new Date(start.time());
    }

    /**
     * Get the end date of the call
     * @return the call end date
     */
    public Date endTime() {
        return new Date(end.time());
    }
}
