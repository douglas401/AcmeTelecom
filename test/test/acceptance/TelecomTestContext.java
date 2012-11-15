package test.acceptance;

public class TelecomTestContext {
	TestBillingSystem billingSystem;

	public TelecomTestContext whileApplicationRuns(){
        billingSystem = new TestBillingSystem();
        return this;
	}

	public TestCallEvent newCallAt(long start){
        return TestCallEvent.newCall(this, start);
	}

    public TelecomTestContext addCallRecord(TestCallEvent testCallEvent) {
        this.billingSystem.addRecord(testCallEvent);
        return this;
    }

    public TelecomTestContext afterGenerateBills(){
        // Calculation for bills

        return this;
    }


}
