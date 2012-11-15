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

    public TestCallEvent from(String caller){
        this.caller = caller;
        return this;
    }

    public TestCallEvent to(String callee){
        this.callee = callee;
        return this;
    }

    public TelecomTestContext forSeconds(int time) {
        this.finishTime = startTime + (time * 1000);
        return this.parent.addCallRecord(this);
    }
}