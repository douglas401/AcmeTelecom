package com.acmetelecom.calling;

/**
 * Represents a call event with caller, callee and the time when the call happens
 */
public abstract class CallEvent {
    private CallInformation caller;
    private CallInformation callee;
    private long time;

    /**
     * Initialises the CallEvent
     * @param caller String of caller
     * @param callee String of callee
     * @param timeStamp The time when the CallEvent happens in long
     */
    public CallEvent(String caller, String callee, long timeStamp) {
        this.caller = new CallInformation(caller);
        this.callee = new CallInformation(callee);
        this.time = timeStamp;
    }

    /**
     * Get the caller
     * @return caller
     */
    public String getCaller() {
        return caller.getNumber();
    }

    /**
     * Get the callee
     * @return callee
     */
    public String getCallee() {
        return callee.getNumber();
    }

    /**
     * Get the time of when the call happens
     * @return time in long
     */
    public long time() {
        return time;
    }
}