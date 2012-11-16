package test.acceptance;

import com.acmetelecom.BillingSystem;
import com.acmetelecom.IBillGenerator;
import com.acmetelecom.customer.Customer;

import java.util.ArrayList;
import java.util.List;

public class TestBillGenerator implements IBillGenerator {
    private List<TestBill> bills = new ArrayList<TestBill>();

    public List<TestBill> getBills(){
        return bills;
    }

    @Override
    public void send(Customer customer, List<BillingSystem.LineItem> calls, String totalBill) {
        bills.add(new TestBill(customer, calls, totalBill));
	}
}
