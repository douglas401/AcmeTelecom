package test.acceptance;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

import java.util.List;

import static org.junit.Assert.assertThat;

public class TestBillAssert {

    private List<TestBill> bills;

    public TestBillAssert(List<TestBill> bills){
        this.bills = bills;
    }

    public TestBillAssert expectNoCalls() {
        assertThat(bills, RecordNumberOfCalls(0));
        return this;
    }

    public TestBillAssert expectNumberOfCalls(int calls) {
        assertThat(bills, RecordNumberOfCalls(calls));
        return this;
    }

    public TestBillAssert expectCallFromPhoneNumber(String number) {
        return this;
    }

    public TestBillAssert getCharged() {
        return this;
    }

    private Matcher<List<TestBill>> RecordNumberOfCalls(final int expectedCalls) {
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
