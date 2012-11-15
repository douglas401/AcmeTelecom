package test.acceptance;

import com.acmetelecom.BillingSystem;
import org.joda.time.DateTimeUtils;

public class TelecomTestContext {
	BillingSystem billingSystem;
    private TestBillGenerator billGenerator;

    public TelecomTestContext whileApplicationRuns(){
        billGenerator = new TestBillGenerator();
        billingSystem = new BillingSystem(billGenerator);
        return this;
	}

	public TestCallEvent newCallAt(long start){
        return TestCallEvent.newCall(this, start);
	}

    public TelecomTestContext addCallRecord(TestCallEvent testCallEvent) {
        DateTimeUtils.setCurrentMillisFixed(testCallEvent.getStartTime());
        billingSystem.callInitiated(testCallEvent.getCaller(), testCallEvent.getCallee());
        DateTimeUtils.setCurrentMillisFixed(testCallEvent.getFinishTime());
        billingSystem.callCompleted(testCallEvent.getCaller(), testCallEvent.getCallee());
        return this;
    }

    public TestBillGenerator generateBills(){
        billingSystem.createCustomerBills();
        return billGenerator;
    }
}
