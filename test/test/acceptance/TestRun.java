package test.acceptance;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.joda.time.DateTimeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.acmetelecom.BillGenerator;
import com.acmetelecom.BillingSystem;
import com.acmetelecom.Call;
import com.acmetelecom.IBillGenerator;
import com.acmetelecom.customer.Customer;
import com.acmetelecom.customer.Tariff;

@RunWith(JMock.class)
public class TestRun{
    Mockery context = new JUnit4Mockery();

    @Test
    public void TestRunNoCalls() throws Exception {
    	TelecomTestContext context = new TelecomTestContext();
//    	
//    	context.callFromPhoneNumber("21312312").ToPhoneNumber("647867468").CallDurationInMinutes(minute)
//    		   .ExpectedDurationFallInPeakTime().ExpectedTotalCost()
//    		   .WhenBillSystemRuns();
//    	
    	//		.caller(number3).calls(number4).for(minute)
    	
    }
    
    
    /**Just a small utility for us to quickly retrieve the tariff rate
     * 
     */
    public void GetTarrifRate(){
    	Tariff std = Tariff.Standard;
    	Tariff lei = Tariff.Leisure;
    	Tariff bus = Tariff.Business;
    	std.peakRate();
    	std.offPeakRate();
    }
    
    /** Haha, World
     * @throws Exception
     */
    @Test
    public void TestRunWithSingleCalls() throws Exception {
        System.out.println("Running...");
    	IBillGenerator billGen = context.mock(BillGenerator.class);
    	BillingSystem billingSystem = new BillingSystem(billGen);
        //set current time to 12/Nov/2012 15:00 
        SimpleDateFormat formater = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Date date = formater.parse("Fri Jun 27 21:28:16 EDT 2008"); 
        DateTimeUtils.setCurrentMillisFixed(date.getTime());
        billingSystem.callInitiated("447722113434", "447766511332");
        
        //set current time to 12/Nov/2012 15:20 
        Date date2 = formater.parse("Fri Jun 27 23:28:16 EDT 2008"); 
        DateTimeUtils.setCurrentMillisFixed(date2.getTime());
        billingSystem.callCompleted("447722113434", "447766511332");

        billingSystem.createCustomerBills();
    }
    
}
