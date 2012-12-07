package com.acmetelecom.acceptance;

import com.acmetelecom.billing.BillingSystem;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
// TODO: Add javadoc into this class

public class TelecomTestContext {
	private BillingSystem billingSystem;
    private TestBillGenerator billGenerator;

    public TelecomTestContext whileApplicationRuns(){
        billGenerator = new TestBillGenerator();
        billingSystem = new BillingSystem(billGenerator);
        return this;
	}

	public TestCallEvent newCallAt(DateTime start){
        return new TestCallEvent(this, start.getMillis());
    }

    public TelecomTestContext addToCallLog(TestCallEvent testCallEvent) {
        DateTimeUtils.setCurrentMillisFixed(testCallEvent.getStartTime());
        billingSystem.callInitiated(testCallEvent.getCaller(), testCallEvent.getCallee());
        DateTimeUtils.setCurrentMillisFixed(testCallEvent.getFinishTime());
        billingSystem.callCompleted(testCallEvent.getCaller(), testCallEvent.getCallee());
        return this;
    }

    public TestBillAssert afterGeneratingBills(){
        billingSystem.createCustomerBills();
        return new TestBillAssert(billGenerator.getBills());
    }
}
