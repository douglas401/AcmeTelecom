package com.acmetelecom.acceptance;

import com.acmetelecom.billing.BillingSystem;
import com.acmetelecom.billing.IBillGenerator;
import com.acmetelecom.customer.Customer;

import java.util.ArrayList;
import java.util.List;
// TODO: Add javadoc into this class

public class TestBillGenerator implements IBillGenerator {
    private List<TestBill> bills = new ArrayList<TestBill>();

    public List<TestBill> getBills(){
        return bills;
    }

    public void send(Customer customer, List<BillingSystem.LineItem> calls, String totalBill) {
        bills.add(new TestBill(customer, calls, totalBill));
	}
}
