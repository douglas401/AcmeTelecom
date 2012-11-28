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
    public void TestRunWithSingleCallNoCustomerFound() {
        telecom.whileApplicationRuns()
                .newCallAt(Now())
                .from("447711111111")
                .to("447722113434")
                .forSeconds(30)
                .afterGeneratingBills()
                .expectNoCalls();
    }

    @Test
    public void TestRunWithSingleCall() {
        telecom.whileApplicationRuns()
                .newCallAt(Now())
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
                .newCallAt(Now()).from("447722113434").to("447766511332").forSeconds(20)
                .newCallAt(Now()).from("447777765432").to("447711111111").forSeconds(40)
                .newCallAt(Now()).from("447722113434").to("447711111111").forMinutes(3)
                .afterGeneratingBills()
                .expectTotalNumberOfCalls(3);
    }



    /*
    * Tests to determine peak/off-peak duration
    * */




























    private long Now() {
        return System.currentTimeMillis();
    }

 }
