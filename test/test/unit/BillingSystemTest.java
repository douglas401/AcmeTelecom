package test.unit;

import com.acmetelecom.BillingSystem;
import com.acmetelecom.IBillGenerator;
import com.acmetelecom.MoneyFormatter;
import com.acmetelecom.Utils.ITimeUtils;
import com.acmetelecom.customer.Customer;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(JMock.class)
public class BillingSystemTest {
    Mockery context = new Mockery();

    @Test
    public void testCreateCustomerBills(){
        // phone calls for customerA
        //Call call1 = new Call(new CallStart("customerA", "customerB"), new CallEnd("customerA", "customerB"));
        //BigDecimal callCost1 = new BigDecimal(1);

        final List<BillingSystem.LineItem> itemsForA = new ArrayList<BillingSystem.LineItem>();
        //itemsForA.add(new BillingSystem.LineItem(call1, callCost1));
        final BigDecimal totalBillA = new BigDecimal(0);
        final String totalBillForA =  MoneyFormatter.penceToPounds(totalBillA);

        // customers
        final Customer customerA = new Customer("","7447111111","");
        final List<Customer> customers = new ArrayList<Customer>(){};
        customers.add(customerA);

        //final CentralCustomerDatabase database = context.mock(CentralCustomerDatabase.class);

        /*context.checking(new Expectations() {{
            allowing(database).getCustomers();
            will(returnValue(customers));
        }}); */

        final IBillGenerator billGenerator = context.mock(IBillGenerator.class);
        context.checking(new Expectations() {{
            allowing(billGenerator).send(with(any(Customer.class)), with(any(List.class)), with(equal(totalBillForA)));//customerA, itemsForA, MoneyFormatter.penceToPounds(totalBillForA));
        }});

        final ITimeUtils timeUtils = context.mock(ITimeUtils.class);
        context.checking(new Expectations() {{
            allowing(timeUtils).getPeakDurationSeconds(with(any(DateTime.class)),with(any(DateTime.class)));
            will(returnValue(0));
        }});

        BillingSystem billingSystem = new BillingSystem(billGenerator, timeUtils);

        billingSystem.createCustomerBills();

        context.assertIsSatisfied();
    }
}
