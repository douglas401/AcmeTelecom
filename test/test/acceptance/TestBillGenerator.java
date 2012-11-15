package test.acceptance;

import com.acmetelecom.BillingSystem;
import com.acmetelecom.IBillGenerator;
import com.acmetelecom.customer.Customer;

import java.util.ArrayList;
import java.util.List;

public class TestBillGenerator implements IBillGenerator {
    List<TestBill> bills = new ArrayList<TestBill>();

	@Override
	public void send(Customer customer, List<BillingSystem.LineItem> calls, String totalBill) {
        bills.add(new TestBill(customer.getPhoneNumber(), calls, totalBill));
	}

    class TestBill {
        private String phoneNumber;
        private List<BillingSystem.LineItem> calls;
        private String totalBill;

        public TestBill(String phoneNumber, List<BillingSystem.LineItem> calls, String totalBill) {
            this.phoneNumber = phoneNumber;
            this.calls = calls;
            this.totalBill = totalBill;
        }

    }
}
