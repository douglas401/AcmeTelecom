package test.acceptance;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.joda.time.DateTimeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.acmetelecom.BillingSystem;
import com.acmetelecom.customer.Tariff;

@RunWith(JMock.class)
public class TestRun{
    Mockery mock = new JUnit4Mockery();

    @Test
    public void TestRunWithNoCalls() throws Exception {
    	BillingSystem billingSystem = new BillingSystem();
    	billingSystem.createCustomerBills();
    }
 
    @Test
    public void GetTarrifRate() throws Exception {
    	Tariff std = Tariff.Standard;
    	Tariff lei = Tariff.Leisure;
    	Tariff bus = Tariff.Business;
    	std.peakRate();
    	std.offPeakRate();
    	
    }
    
    @Test
    public void TestRunWithSingleCalls() throws Exception {
        System.out.println("Running...");
        BillingSystem billingSystem = new BillingSystem();
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
