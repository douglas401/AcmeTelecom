package test.acceptance;

import java.util.HashMap;
import java.util.List;

import com.acmetelecom.BillingSystem;
import com.acmetelecom.IBillGenerator;
import com.acmetelecom.customer.Customer;

public class TestBillGenerator implements IBillGenerator {
	HashMap<String,String> bill = new HashMap<String,String>();
	
	@Override
	public void send(Customer customer, List<BillingSystem.LineItem> calls, String totalBill) {
		bill.put(customer.getPhoneNumber(),totalBill);
	}

	public String getBillsForPhoneNumber(String phoneNumber){
	
		return bill.get(phoneNumber);
	
	}
	
	
	
}
