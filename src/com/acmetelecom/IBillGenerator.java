package com.acmetelecom;

import java.util.List;

import com.acmetelecom.BillingSystem.LineItem;
import com.acmetelecom.customer.Customer;

public interface IBillGenerator {

	public abstract void send(Customer customer,
			List<BillingSystem.LineItem> calls, String totalBill);

}