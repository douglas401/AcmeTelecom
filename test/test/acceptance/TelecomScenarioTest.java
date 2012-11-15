package test.acceptance;

import org.junit.Test;

public class TelecomScenarioTest {
    TelecomTestContext telecom = new TelecomTestContext();

    @Test
    public void TestRunNoCalls() {

    }

    @Test
    public void TestRunWithSingleCall() {
        telecom.whileApplicationRuns()
                .newCallAt(System.currentTimeMillis())
                .from("447722113434")
                .to("447766511332")
                .forSeconds(20)
                .generateBills();

//                .assertPhoneNumber("447722113434").getsChargedBy(4.33)
//                .verifyTotalCallUsageInMinutes(100);
    }
}
