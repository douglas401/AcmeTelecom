package com.acmetelecom.acceptance;

import com.acmetelecom.calling.CallInformation;

public class TestCallEvent {

    private TelecomTestContext parent;
    private CallInformation caller;
    private CallInformation callee;
    private long startTime;
    private long finishTime;

    public TestCallEvent(TelecomTestContext context, long start){
        this.parent = context;
        this.startTime = start;
    }

    public String getCaller() {
        return caller.getNumber();
    }

    public String getCallee() {
        return callee.getNumber();
    }

    public long getStartTime() {
        return startTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public TestCallEvent from(String caller){
        this.caller = new CallInformation(caller);
        return this;
    }

    public TestCallEvent to(String callee){
        this.callee = new CallInformation(callee);
        return this;
    }

    public TelecomTestContext forSeconds(int seconds) {
        this.finishTime = startTime + (seconds * 1000);
        return this.parent.addToCallLog(this);
    }

    public TelecomTestContext forMinutes(int minutes) {
        return forSeconds(minutes * 60);
    }

    public TelecomTestContext forHours(int hours) {
        return forMinutes(hours * 60);
    }
}