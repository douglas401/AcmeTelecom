package test.acceptance;

/**
 *
 */
public class TestCallEvent {

    private TelecomTestContext parent;
    private String caller;
    private String callee;
    private long startTime;
    private long finishTime;

    public TestCallEvent(TelecomTestContext context, long start){
        this.parent = context;
        this.startTime = start;
    }

    public static TestCallEvent newCall(TelecomTestContext context, long start) {
        return new TestCallEvent(context, start);
    }

    public String getCaller() {
        return caller;
    }

    public String getCallee() {
        return callee;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public TestCallEvent from(String caller){
        this.caller = caller;
        return this;
    }

    public TestCallEvent to(String callee){
        this.callee = callee;
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