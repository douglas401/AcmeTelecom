package test.acceptance;

import com.acmetelecom.BillingSystem;
import com.acmetelecom.IBillGenerator;
import com.acmetelecom.customer.Customer;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class TestBillGenerator implements IBillGenerator {
    List<TestBill> bills = new ArrayList<TestBill>();

	@Override
	public void send(Customer customer, List<BillingSystem.LineItem> calls, String totalBill) {
        bills.add(new TestBill(customer, calls, totalBill));
	}


    public TestBillGenerator expectEmptyBills() {
        return expectNumberOfCalls(0);
    }

    public TestBillGenerator expectNumberOfBills(int i) {
        int numberOfBills = 0;
        for (TestBill bill : bills){
            if(bill.calls.size() > 0){ numberOfBills++; }
        }
        assertEquals(numberOfBills, i);
        return this;
    }

    public TestBillGenerator expectNoCalls() {
        return expectNumberOfCalls(0);
    }

    public TestBillGenerator expectNumberOfCalls(int i) {
        int numberOfCalls = 0;
        for (TestBill bill : bills){
            numberOfCalls += bill.calls.size();
        }
        assertEquals(numberOfCalls, i);
        return this;
    }

    public TestBillGenerator expectNoCallsAndEmptyBills() {
        expectNoCalls();
        return expectEmptyBills();
    }

    class TestBill {
        private Customer customer;
        private List<BillingSystem.LineItem> calls;
        private String totalBill;

        public TestBill(Customer customer, List<BillingSystem.LineItem> calls, String totalBill) {
            this.customer = customer;
            this.calls = calls;
            this.totalBill = totalBill;
        }

    }
}
