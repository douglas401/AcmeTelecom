package com.acmetelecom.acceptance;

import com.acmetelecom.billing.BillingSystem;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

import java.util.List;

import static org.junit.Assert.*;

public class TestBillAssert {

    private List<TestBill> bills;

    public TestBillAssert(List<TestBill> bills){
        this.bills = bills;
    }

    public TestBillAssert expectNoCalls() {
        assertThat(bills, HasRecordedNumberOfCalls(0));
        return this;
    }

    public TestBillAssert expectTotalNumberOfCalls(int calls) {
        assertThat(bills, HasRecordedNumberOfCalls(calls));
        return this;
    }

    public TestBillAssertIndividual expectBillOnPhoneNumber(String number) {
        assertThat(bills, HasBilledForCaller(number));
        return new TestBillAssertIndividual(this, number);
    }

    private Matcher<List<TestBill>> HasBilledForCaller(final String number) {
        return new TypeSafeMatcher<List<TestBill>>() {
            @Override
            public boolean matchesSafely(List<TestBill> testBills) {
                TestBill caller = getMatchedBill(number);
                assertNotNull(caller);
                return caller.getCalls().size() > 0;
            }

            public void describeTo(Description description) { return; } // Ignore
        };
    }

    private Matcher<List<TestBill>> HasRecordedNumberOfCalls(final int expectedCalls) {
        return new TypeSafeMatcher<List<TestBill>>() {
            @Override
            public boolean matchesSafely(List<TestBill> testBills) {
                int numberOfCalls = 0;
                for (TestBill bill : bills){
                    numberOfCalls += bill.getCalls().size();
                }
                return (numberOfCalls == expectedCalls);
            }

            public void describeTo(Description description) { return; } // Ignore
        };
    }

    private TestBill getMatchedBill(String number){
        TestBill caller = null;
        for(TestBill bill : bills){
            if(bill.getCustomer().getPhoneNumber().equals(number)){
                caller = bill;
            }
        }
        return caller;
    }

    public void noOtherCustomerGettingChargedAnything() {
        for(TestBill each : bills ){
            if(! each.isVerified() ){
                assertEquals(each.getCalls().size(),0);
            }
        }

    }

    public class TestBillAssertIndividual{

        private TestBillAssert parent;
        private TestBill bill;
        private int curItemIndex;
        private int numberOfItemChecked=0;

        public TestBillAssertIndividual(TestBillAssert testBillAssert, String number){
            this.parent = testBillAssert;
            this.bill = parent.getMatchedBill(number);
        }

        public TestBillAssertIndividual call(int item) {
            this.curItemIndex = item-1;
            numberOfItemChecked++;
            assertTrue(bill.getCalls().size() >= item && item >= 0);
            return this;
        }

        public TestBillAssertIndividual cost(String cost) {
            assertEquals(bill.getCalls().get(curItemIndex).cost().toString(), cost);
            return this;
        }

        public TestBillAssert noOtherBillItemForThisPhoneNumber() {
            assertTrue(numberOfItemChecked == bill.getCalls().size());
            bill.setVerified();
            return this.parent;
        }

        public TestBillAssertIndividual toNumber(String calleeNumber) {
            assertEquals(bill.getCalls().get(curItemIndex).callee(), calleeNumber);
            return this;
        }

        public TestBillAssertIndividual lastedInMinutes(String duration) {
            assertEquals(bill.getCalls().get(curItemIndex).durationMinutes(), duration);
            return this;
        }

    }
}
