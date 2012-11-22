package test.acceptance;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

import java.util.List;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TestBillAssert {

    private List<TestBill> bills;

    public TestBillAssert(List<TestBill> bills){
        this.bills = bills;
    }

    public TestBillAssert expectNoCalls() {
        assertThat(bills, HasRecordedNumberOfCalls(0));
        return this;
    }

    public TestBillAssert expectNumberOfCalls(int calls) {
        assertThat(bills, HasRecordedNumberOfCalls(calls));
        return this;
    }

    public TestBillAssert expectCallFromPhoneNumber(String number) {
        boolean isBilled = false;
        for (TestBill bill : bills){
            isBilled = isBilled || (bill.getCustomer().getPhoneNumber().equalsIgnoreCase(number));
        }
        assertTrue(isBilled);
        return this;
    }

    public TestBillAssert getCharged() {
        return this;
    }

    private Matcher<List<TestBill>> HasRecordedNamesOfCaller(final String number) {
        return new TypeSafeMatcher<List<TestBill>>() {
            @Override
            public boolean matchesSafely(List<TestBill> testBills) {
                boolean isBilled = false;
                for (TestBill bill : bills){
                    isBilled = isBilled || (bill.getCustomer().getPhoneNumber().equalsIgnoreCase(number));
                }
                return isBilled;
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
