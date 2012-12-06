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
                .call(1).toNumber("447766511332").lastedInMinutes("0:20").cost("6")
                .noOtherBillItemForThisPhoneNumber()
                .expectBillOnPhoneNumber("447777765432")
                .call(1).toNumber("447711111111").lastedInMinutes("4:00").cost("48")
                .noOtherBillItemForThisPhoneNumber()
                .noOtherCustomerGettingChargedAnything();
    }

    /*
    * Tests to determine peak/off-peak duration
    * */

    @Test
    public void SingleCallAtPeakTimeBusinessTariff() {
        telecom.whileApplicationRuns()
                .newCallAt(StartOfPeak)
                .from("447722113434")
                .to("447766511332")
                .forMinutes(20)
                .afterGeneratingBills()
                .expectBillOnPhoneNumber("447722113434")
                .call(1).toNumber("447766511332").lastedInMinutes("20:00").cost("360")
                .noOtherBillItemForThisPhoneNumber()
                .noOtherCustomerGettingChargedAnything();
    }

    @Test
    public void SingleCallAtOffPeakTimeBusinessTariff() {
        telecom.whileApplicationRuns()
                .newCallAt(EndOfPeak)
                .from("447722113434")
                .to("447766511332")
                .forMinutes(20)
                .afterGeneratingBills()
                .expectBillOnPhoneNumber("447722113434")
                .call(1).toNumber("447766511332").lastedInMinutes("20:00").cost("360")
                .noOtherBillItemForThisPhoneNumber()
                .noOtherCustomerGettingChargedAnything();
    }

    @Test
    public void SingleCallAtMixedTimeBusinessTariff() {
        telecom.whileApplicationRuns()
                .newCallAt(EndOfPeak.minusMinutes(10))
                .from("447722113434")
                .to("447766511332")
                .forMinutes(20)
                .afterGeneratingBills()
                .expectBillOnPhoneNumber("447722113434")
                .call(1).toNumber("447766511332").lastedInMinutes("20:00").cost("360")
                .noOtherBillItemForThisPhoneNumber()
                .noOtherCustomerGettingChargedAnything();
    }

    @Test
    public void SingleCallAtPeakTime() {
        telecom.whileApplicationRuns()
                .newCallAt(StartOfPeak)
                .from("447721232123")
                .to("447766511332")
                .forMinutes(20)
                .afterGeneratingBills()
                .expectBillOnPhoneNumber("447721232123")
                .call(1).toNumber("447766511332").lastedInMinutes("20:00").cost("600")
                .noOtherBillItemForThisPhoneNumber()
                .noOtherCustomerGettingChargedAnything();
    }

    @Test
    public void SingleCallAtOffPeakTime() {
        telecom.whileApplicationRuns()
                .newCallAt(EndOfPeak)
                .from("447721232123")
                .to("447766511332")
                .forMinutes(20)
                .afterGeneratingBills()
                .expectBillOnPhoneNumber("447721232123")
                .call(1).toNumber("447766511332").lastedInMinutes("20:00").cost("240")
                .noOtherBillItemForThisPhoneNumber()
                .noOtherCustomerGettingChargedAnything();
    }

    @Test
    public void SingleCallAtMixedTime() {
        telecom.whileApplicationRuns()
                .newCallAt(EndOfPeak.minusMinutes(10))
                .from("447721232123")
                .to("447766511332")
                .forMinutes(20)
                .afterGeneratingBills()
                .expectBillOnPhoneNumber("447721232123")
                .call(1).toNumber("447766511332").lastedInMinutes("20:00").cost("420")
                .noOtherBillItemForThisPhoneNumber()
                .noOtherCustomerGettingChargedAnything();
    }

}