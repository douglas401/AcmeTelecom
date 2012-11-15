package test.acceptance;

import com.acmetelecom.BillingSystem;

import java.util.ArrayList;
import java.util.List;

public class TestBillingSystem extends BillingSystem{

    private List<TestCallEvent> callLog = new ArrayList<TestCallEvent>();
    private List<TestCallEventResult> result = new ArrayList<TestCallEventResult>();

    public void addRecord(TestCallEvent testCallEvent) {
       callLog.add(testCallEvent);
    }
}
