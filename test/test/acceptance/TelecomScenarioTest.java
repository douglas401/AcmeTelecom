package test.acceptance;

import org.junit.Test;

public class TelecomScenarioTest {
    TelecomTestContext telecom = new TelecomTestContext();

    /*
    * Simple application running scenario
    * */
    @Test
    public void TestRunNoCalls() {
        telecom.whileApplicationRuns()
                .afterGeneratingBills()
                .expectNoCalls();
    }

    @Test
    public void TestRunWithSingleCall() {
        telecom.whileApplicationRuns()
                .newCallAt(System.currentTimeMillis())
                .from("447722113434")
                .to("447766511332")
                .forSeconds(20)
                .afterGeneratingBills()
                .expectTotalNumberOfCalls(1)
                .expectBillOnPhoneNumber("447722113434");
    }

    @Test
    public void TestRunWithMultipleCalls() {
        telecom.whileApplicationRuns()
                // Add call information
                .afterGeneratingBills()
                // Add assert information
                ;
    }

    /*
    * Tests to determine peak/off-peak duration
    * */
}
