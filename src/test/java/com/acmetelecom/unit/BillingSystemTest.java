package com.acmetelecom.unit;

import com.acmetelecom.billing.BillingSystem;
import com.acmetelecom.billing.IBillGenerator;
import com.acmetelecom.billing.IDurationCalculator;
import com.acmetelecom.calling.CallEnd;
import com.acmetelecom.calling.CallEvent;
import com.acmetelecom.calling.CallStart;
import com.acmetelecom.customer.Customer;
import com.acmetelecom.customer.CustomerDatabase;
import com.acmetelecom.customer.Tariff;
import com.acmetelecom.customer.TariffLibrary;
import com.acmetelecom.utils.MoneyFormatter;
import junitx.util.PrivateAccessor;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(JMock.class)
public class BillingSystemTest {
    Mockery context = new Mockery();

    BillingSystem billingSystem = new BillingSystem();
    final IDurationCalculator durationCalculator = context.mock(IDurationCalculator.class);
    final CustomerDatabase customerDatabase = context.mock(CustomerDatabase.class);
    final IBillGenerator billGenerator = context.mock(IBillGenerator.class);
    final TariffLibrary tariffDatabase = context.mock(TariffLibrary.class);

    // set up customers
    final Customer customerA = new Customer("A","447711111111","Standard");
    final Customer customerB = new Customer("B","447722222222","Business");
    final Customer customerC = new Customer("C","447733333333","Business");
    final List<Customer> customers = new ArrayList<Customer>(){};

    // set up phone calls
    final Map<String, List<CallEvent>> callLogMap = new HashMap<String, List<CallEvent>>();

    @Before
    public void setUp(){
        //use privateAccessor to inject mock objects into BillingSystem
        try {
            PrivateAccessor.setField(billingSystem, "durationCalculator", durationCalculator);
            PrivateAccessor.setField(billingSystem, "billGenerator", billGenerator);
            PrivateAccessor.setField(billingSystem, "customerDatabase", customerDatabase);
            PrivateAccessor.setField(billingSystem, "tariffDatabase", tariffDatabase);
            PrivateAccessor.setField(billingSystem, "callLogMap", callLogMap);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        context.checking(new Expectations() {{
            allowing(customerDatabase).getCustomers();will(returnValue(customers));
        }});
    }

    /**
     * Tests the total bill generated for a Standard customer
     */
    @Test
    public void testStandardCustomerBills(){
        // set up customers
        customers.clear();
        customers.add(customerA);

        // mocking customer database and tariff database
        context.checking(new Expectations() {{
            allowing(tariffDatabase).tarriffFor(customerA);will(returnValue(Tariff.Standard));
        }});

        // set up phone calls
        // phone calls for customer A, assume peak period is 7-19 everyday
        List<CallEvent> phoneCallsA = new ArrayList<CallEvent>();
        // peak call
        final DateTime callTime1 = new DateTime(2012, 11, 1, 7, 5, 0);
        CallStart callStart1 = new CallStart(customerA.getPhoneNumber(), customerB.getPhoneNumber());
        CallEnd callEnd1 = new CallEnd(customerA.getPhoneNumber(), customerB.getPhoneNumber());
        // off peak call
        final DateTime callTime2 = new DateTime(2012, 11, 1, 19, 25, 0);
        CallStart callStart2 = new CallStart(customerA.getPhoneNumber(), customerB.getPhoneNumber());
        CallEnd callEnd2 = new CallEnd(customerA.getPhoneNumber(), customerB.getPhoneNumber());
        // peak call
        final DateTime callTime3 = new DateTime(2012, 11, 1, 12, 12, 0);
        CallStart callStart3 = new CallStart(customerA.getPhoneNumber(), customerC.getPhoneNumber());
        CallEnd callEnd3 = new CallEnd(customerA.getPhoneNumber(), customerC.getPhoneNumber());
        try {
            PrivateAccessor.setField(callStart1, "time", callTime1.getMillis());
            PrivateAccessor.setField(callEnd1, "time", callTime1.plusMinutes(1).getMillis());
            PrivateAccessor.setField(callStart2, "time", callTime2.getMillis());
            PrivateAccessor.setField(callEnd2, "time", callTime2.plusMinutes(15).getMillis());
            PrivateAccessor.setField(callStart3, "time", callTime3.getMillis());
            PrivateAccessor.setField(callEnd3, "time", callTime3.plusHours(1).getMillis());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        phoneCallsA.add(callStart1);
        phoneCallsA.add(callEnd1);
        phoneCallsA.add(callStart2);
        phoneCallsA.add(callEnd2);
        phoneCallsA.add(callStart3);
        phoneCallsA.add(callEnd3);
        callLogMap.clear();
        callLogMap.put(customerA.getPhoneNumber(), phoneCallsA);

        // mocking duration calculations
        context.checking(new Expectations() {{
            allowing(durationCalculator).getPeakDurationSeconds(with(isTimeMatches(callTime1)),with(isTimeMatches(callTime1.plusMinutes(1))));
            will(returnValue(60));
            allowing(durationCalculator).getPeakDurationSeconds(with(isTimeMatches(callTime2)),with(isTimeMatches(callTime2.plusMinutes(15))));
            will(returnValue(0));
            allowing(durationCalculator).getPeakDurationSeconds(with(isTimeMatches(callTime3)),with(isTimeMatches(callTime3.plusHours(1))));
            will(returnValue(60*60));
        }});

        // set up bills
        // bill for customerA
        final BigDecimal totalBillA = (
                new BigDecimal(60).multiply(tariffDatabase.tarriffFor(customerA).peakRate()).
                        add(new BigDecimal(60*15).multiply(tariffDatabase.tarriffFor(customerA).offPeakRate())).
                        add(new BigDecimal(60*60).multiply(tariffDatabase.tarriffFor(customerA).peakRate()))
        ).setScale(0, RoundingMode.HALF_UP);
        final String totalBillForA =  MoneyFormatter.penceToPounds(totalBillA);

        // set up test conditions for method send()
        context.checking(new Expectations() {{
            oneOf(billGenerator).send(with(isCustomerMatches(customerA)), with(any(List.class)), with(equal(totalBillForA)));
        }});

        billingSystem.createCustomerBills();
    }

    /**
     * Tests the total bill generated for a Business customer
     */
    @Test
    public void testBusinessCustomerBills(){
        // set up customers
        customers.clear();
        customers.add(customerB);

        // mocking customer database and tariff database
        context.checking(new Expectations() {{
            allowing(tariffDatabase).tarriffFor(customerB);will(returnValue(Tariff.Business));
        }});

        // set up phone calls
        // phone calls for customer B, assume peak period is 7-19 everyday
        List<CallEvent> phoneCallsB = new ArrayList<CallEvent>();
        final DateTime callTime4 = new DateTime(2012, 11, 2, 8, 5, 0);
        CallStart callStart4 = new CallStart(customerB.getPhoneNumber(), customerC.getPhoneNumber());
        CallEnd callEnd4 = new CallEnd(customerB.getPhoneNumber(), customerC.getPhoneNumber());
        try {
            PrivateAccessor.setField(callStart4, "time", callTime4.getMillis());
            PrivateAccessor.setField(callEnd4, "time", callTime4.plusMinutes(3).getMillis());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        phoneCallsB.add(callStart4);
        phoneCallsB.add(callEnd4);
        callLogMap.clear();
        callLogMap.put(customerB.getPhoneNumber(), phoneCallsB);

        // mocking duration calculations
        context.checking(new Expectations() {{
            allowing(durationCalculator).getPeakDurationSeconds(with(isTimeMatches(callTime4)),with(isTimeMatches(callTime4.plusMinutes(3))));
            will(returnValue(3*60));
        }});

        // set up bills
        // bills for customer B
        final BigDecimal totalBillB = (
                new BigDecimal(3*60).multiply(tariffDatabase.tarriffFor(customerB).peakRate())).setScale(0, RoundingMode.HALF_UP);
        final String totalBillForB =  MoneyFormatter.penceToPounds(totalBillB);

        // set up test conditions for method send()
        context.checking(new Expectations() {{
            oneOf(billGenerator).send(with(isCustomerMatches(customerB)), with(any(List.class)), with(equal(totalBillForB)));
        }});

        billingSystem.createCustomerBills();
    }

    /**
     * Tests a customer with no phone call, a bill of zero pounds generated
     */
    @Test
    public void testZeroCustomerBills(){
        // set up customers
        customers.clear();
        customers.add(customerC);

        // mocking customer database and tariff database
        context.checking(new Expectations() {{
            allowing(tariffDatabase).tarriffFor(customerC);will(returnValue(Tariff.Standard));
        }});

        // set up phone calls
        // phone calls for customer C
        List<CallEvent> phoneCallsC = new ArrayList<CallEvent>();
        callLogMap.clear();
        callLogMap.put(customerC.getPhoneNumber(), phoneCallsC);

        // set up bills
        // bills for customer C
        final String totalBillForC =  MoneyFormatter.penceToPounds(new BigDecimal(0));

        // set up test conditions for method send()
        context.checking(new Expectations() {{
            oneOf(billGenerator).send(with(isCustomerMatches(customerC)), with(any(List.class)), with(equal(totalBillForC)));
        }});

        billingSystem.createCustomerBills();
    }

    private Matcher<Customer> isCustomerMatches(final Customer customer) {
        return new TypeSafeMatcher<Customer>() {
            @Override
            public boolean matchesSafely(Customer item) {
                return item.getPhoneNumber().equals(customer.getPhoneNumber());
            }

            public void describeTo(Description description) { return; }
        };
    }

    private Matcher<DateTime> isTimeMatches(final DateTime time) {
        return new TypeSafeMatcher<DateTime>() {
            @Override
            public boolean matchesSafely(DateTime item) {
                return item.getMillis() == time.getMillis();
            }

            public void describeTo(Description description) { return; }
        };
    }
}

