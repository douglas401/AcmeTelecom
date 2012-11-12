package test.acceptance;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.joda.time.DateTimeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.acmetelecom.BillingSystem;

@RunWith(JMock.class)
public class TestRun{
    Mockery mock = new JUnit4Mockery();

    @Test
    public void TestRunWithNoCalls() throws Exception {
    	BillingSystem billingSystem = new BillingSystem();
    	billingSystem.createCustomerBills();
    }
    
    @Test
    public void TestRunWithSingleCalls() throws Exception {
        System.out.println("Running...");
        BillingSystem billingSystem = new BillingSystem();
        //set current time to 12/Nov/2012 15:00 
        DateTimeUtils.setCurrentMillisFixed(1352732400L);
        billingSystem.callInitiated("447722113434", "447766511332");
        
        //set current time to 12/Nov/2012 15:20 
        DateTimeUtils.setCurrentMillisFixed(1352733600L);
        billingSystem.callCompleted("447722113434", "447766511332");

        billingSystem.createCustomerBills();
    }
    
}
