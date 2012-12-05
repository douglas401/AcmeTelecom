package com.acmetelecom.acceptance;

import org.joda.time.DateTime;
import org.junit.Test;

public class TelecomScenarioTest {
    public static final DateTime StartOfDay = new DateTime(2012, 11, 1, 0, 0, 0);
    public static final DateTime StartOfPeak = StartOfDay.plusHours(7);
    public static final DateTime EndOfPeak = StartOfPeak.plusHours(12);

    TelecomTestContext telecom = new TelecomTestContext();

    /*
    * Simple application running scenario
    * */
    @Test
    public void NoCalls() {
        telecom.whileApplicationRuns()
                .afterGeneratingBills()
                .expectNoCalls();
    }

    @Test
    public void SingleCall() {
        telecom.whileApplicationRuns()
                .newCallAt(StartOfDay)
                .from("447722113434")
                .to("447766511332")
                .forSeconds(20)
                .afterGeneratingBills()
                .expectTotalNumberOfCalls(1)
                .expectBillOnPhoneNumber("447722113434");
    }

    @Test
    public void SingleCallNonCustomerFounded() {
        telecom.whileApplicationRuns()
                .newCallAt(StartOfDay)
                .from("447788888888")
                .to("447722113434")
                .forSeconds(30)
                .afterGeneratingBills()
                .expectNoCalls();
    }

    @Test
    public void MultipleCalls() {
        telecom.whileApplicationRuns()
                .newCallAt(StartOfDay).from("447722113434").to("447766511332").forSeconds(20)
                .newCallAt(StartOfDay).from("447777765432").to("447711111111").forSeconds(40)
                .newCallAt(StartOfDay).from("447722113434").to("447711111111").forMinutes(3)
                .afterGeneratingBills()
                .expectTotalNumberOfCalls(3);
    }

    @Test
    public void MultipleCallsNonCustomerFounded() {
        telecom.whileApplicationRuns()
                .newCallAt(StartOfDay).from("447722113434").to("447766511332").forSeconds(20)
                .newCallAt(StartOfDay).from("447777765432").to("447711111111").forMinutes(4)
                .newCallAt(StartOfDay).from("447722222222").to("447711111111").forSeconds(30)
                .afterGeneratingBills()
                .expectTotalNumberOfCalls(2)
                .expectBillOnPhoneNumber("447722113434")
                .expectBillOnPhoneNumber("447777765432");
    }

    /*
    * Tests to determine peak/off-peak duration
    * */
}