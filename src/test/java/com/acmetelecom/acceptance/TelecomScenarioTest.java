package com.acmetelecom.acceptance;

import org.joda.time.DateTime;
import org.junit.Test;

public class TelecomScenarioTest {
    private static final DateTime StartOfDay = new DateTime(2012, 11, 1, 0, 0, 0);
    private static final DateTime StartOfPeak = StartOfDay.plusHours(7);
    private static final DateTime EndOfPeak = StartOfPeak.plusHours(12);

    TelecomTestContext telecom = new TelecomTestContext();

    /*
    * Simple application running scenario
    * */

    /*
    * Simply runs the system, no input
    * */
    @Test
    public void NoCalls() {
        telecom.whileApplicationRuns()
                .afterGeneratingBills()
                .expectNoCalls();
    }

    /*
    * input: a call from valid customer 447722113434 for 20 seconds
    * result; bill recorded on the account of 447722113434
    * */
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

    /*
    * input: a call from unknown customer 447788888888
    * result: no bills are generated into any accounts
    * */
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

    /*
    * input: 3 calls from 2 valid customers
    * result: total number of 3 records are found in the bills
    * */
    @Test
    public void MultipleCalls() {
        telecom.whileApplicationRuns()
                .newCallAt(StartOfDay).from("447722113434").to("447766511332").forSeconds(20)
                .newCallAt(StartOfDay).from("447777765432").to("447711111111").forSeconds(40)
                .newCallAt(StartOfDay).from("447722113434").to("447711111111").forMinutes(3)
                .afterGeneratingBills()
                .expectTotalNumberOfCalls(3);
    }

    /*
    * input: 3 calls received, 2 from valid customers and 1 unknown
    * result: total bill = 0.06 on the account of 447722113434
    *         total bill = 0.48 on the account of 447777765432
    *         no records are found on the unknown customer 447722222222
    * */
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
    * Tests to determine peak/off-peak duration scenario
    * */

    /*
    * input: 1 call from valid customer on business price-plan at peak time for 20 minutes
    * result: total bill = 3.60 on the account of 447722113434
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

    /*
    * input: 1 call from valid customer on business price-plan at off-peak time for 20 minutes
    * result: total bill = 3.60 on the account of 447722113434
    * */
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

    /*
    * input: 1 call from valid customer on business price-plan at peak for 10 minutes and off-peak for 10 minutes
    * result: total bill = 3.60 on the account of 447722113434
    * */
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

    /*
    * input: 1 call from valid customer on standard price-plan at peak for 20 minutes
    * result: total bill = 6.00 on the account of 447721232123
    * */
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

    /*
    * input: 1 call from valid customer on standard price-plan at off-peak time for 20 minutes
    * result: total bill = 2.40 on the account of 447721232123
    * */
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

    /*
    * input: 1 call from valid customer on standard price-plan at peak for 10 minutes and off-peak for 10 minutes
    * result: total bill = 4.20 on the account of 447721232123
    * */
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