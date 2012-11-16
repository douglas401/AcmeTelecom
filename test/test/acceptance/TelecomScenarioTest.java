package test.acceptance;

import org.junit.Test;

public class TelecomScenarioTest {
    TelecomTestContext telecom = new TelecomTestContext();

    /*
    * Simple application running check
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
                .expectNumberOfCalls(1)
                .expectCallFromPhoneNumber("447722113434");
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
