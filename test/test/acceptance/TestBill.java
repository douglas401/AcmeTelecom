package test.acceptance;

import com.acmetelecom.BillingSystem;
import com.acmetelecom.customer.Customer;

import java.util.List;

public class TestBill {
    private final Customer customer;
    private final List<BillingSystem.LineItem> calls;
    private final String totalBill;

    public TestBill(Customer customer, List<BillingSystem.LineItem> calls, String totalBill) {
        this.customer = customer;
        this.calls = calls;
        this.totalBill = totalBill;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<BillingSystem.LineItem> getCalls() {
        return calls;
    }

    public String getTotalBill() {
        return totalBill;
    }
}
