package com.acmetelecom.acceptance;

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

    public TestBillAssert expectBillOnPhoneNumber(String number) {
        assertThat(bills, HasBilledForCaller(number));
        return this;
    }

    private Matcher<List<TestBill>> HasBilledForCaller(final String number) {
        return new TypeSafeMatcher<List<TestBill>>() {
            @Override
            public boolean matchesSafely(List<TestBill> testBills) {
                TestBill caller = null;
                for(TestBill bill : bills){
                    if(bill.getCustomer().getPhoneNumber().equals(number)){
                        caller = bill;
                    }
                }
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
}
