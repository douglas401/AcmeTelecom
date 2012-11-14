package test.acceptance;

import java.util.List;

import org.joda.time.DateTime;

import com.acmetelecom.BillingSystem;
import com.acmetelecom.IBillGenerator;
import com.acmetelecom.customer.Customer;

public class TelecomTestContext {
	BillingSystem billSys; 
	IBillGenerator billGen = new TestBillGenerator();
	DateTime now;
	
	public TelecomTestContext(){
		billSys = new BillingSystem(billGen);

	}
	
	public TelecomTestContext withCallStarts(String caller, String callee){
		billSys.callInitiated(caller, callee);
		return this;
	}

	public TelecomTestContext withCallEnds(String caller, String callee){
		billSys.callCompleted(caller, callee);
		return this;
	}

	public TelecomTestContext setCurrentTime(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public TelecomTestContext letFewMinutesPast(int i) {
		// TODO Auto-generated method stub
		return this;
	}
	
}
