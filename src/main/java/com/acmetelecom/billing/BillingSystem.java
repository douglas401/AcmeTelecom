package com.acmetelecom.billing;

import com.acmetelecom.calling.Call;
import com.acmetelecom.calling.CallEnd;
import com.acmetelecom.calling.CallEvent;
import com.acmetelecom.calling.CallStart;
import com.acmetelecom.customer.*;
import com.acmetelecom.utils.MoneyFormatter;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillingSystem {

    private IDurationCalculator durationCalculator;        
    // a map of caller's phone number as key and a list of call events as value
    private Map<String, List<CallEvent>> callLogMap = new HashMap<String, List<CallEvent>>();
    private IBillGenerator billGenerator;
    private CustomerDatabase customerDatabase = CentralCustomerDatabase.getInstance();
    private TariffLibrary tariffDatabase = CentralTariffDatabase.getInstance();

    /**
     * Initialises the BillingSystem with default BillGenerator
     */
    public BillingSystem(){
        billGenerator = new BillGenerator();
        durationCalculator = new DurationCalculator(new SinglePeakPeriod());
    }

    /**
     * Initialises the BillingSystem with the custom BillGenerator
     */
    public BillingSystem(IBillGenerator billGenerator) {
        this.billGenerator = billGenerator;
        this.durationCalculator = new DurationCalculator(new SinglePeakPeriod());
    }

    /**
     * Initialises a call with caller and callee
     * @param caller caller in String
     * @param callee callee in String
     */
    public void callInitiated(String caller, String callee) {
        List<CallEvent> callersEventList;
        callersEventList = getCallerEventList(caller);
        callersEventList.add(new CallStart(caller, callee));
    }

    /**
     * Completes a call with caller and callee, corresponds to callInitiated
     * @param caller caller in String
     * @param callee callee in String
     */
    public void callCompleted(String caller, String callee) {
        List<CallEvent> callersEventList;
        callersEventList = getCallerEventList(caller);
        callersEventList.add(new CallEnd(caller, callee));
    }

    /**
     * Get a list of CallEvents made by a specific caller
     * @param caller caller in String
     * @return list of CallEvents made by the caller
     */
    private List<CallEvent> getCallerEventList(String caller) {
        List<CallEvent> callersEventList;
        if ((callersEventList = callLogMap.get(caller)) == null) {
            callersEventList = new ArrayList<CallEvent>();
            callLogMap.put(caller, callersEventList);
        }
        return callersEventList;
    }

    /**
     * Creates the bills for all the customers
     */
    public void createCustomerBills() {
        List<Customer> customers = customerDatabase.getCustomers();
        for (Customer customer : customers) {
            createBillFor(customer);
        }
        callLogMap.clear();
    }

    private void createBillFor(Customer customer) {
        List<CallEvent> customerEvents = getCallerEventList(customer.getPhoneNumber());

        List<Call> calls = new ArrayList<Call>();

        CallEvent start = null;
        for (CallEvent event : customerEvents) {
            if (event instanceof CallStart) {
                start = event;
            }
            if (event instanceof CallEnd && start != null) {
                calls.add(new Call(start, event));
                start = null;
            }
        }

        BigDecimal totalBill = new BigDecimal(0);
        List<LineItem> items = new ArrayList<LineItem>();

        for (Call call : calls) {

            Tariff tariff = tariffDatabase.tarriffFor(customer);

            BigDecimal cost;

            /*
            * Utils.getPeakDuration(call.StartTime(),call.endTime())  = peakDuration
            * OffPeakDuration = call.Duration - peakDuration
            *
            *        offpeakCost = new BigDecimal(offpeakDuration.multiply(tariff.offPeakRate());
            *        peakCost    = new BigDecimal(peakDuration.multiply(tariff.peakRate());
            *
            * cost = offpeakCost + peakCost;
            * */
            int peakDurationSeconds = durationCalculator.getPeakDurationSeconds(new DateTime(call.startTime()), new DateTime(call.endTime()));
            int offPeakDurationSeconds = call.durationSeconds() - peakDurationSeconds;
            cost = new BigDecimal(peakDurationSeconds).multiply(tariff.peakRate()).add(new BigDecimal(offPeakDurationSeconds).multiply(tariff.offPeakRate()));

            cost = cost.setScale(0, RoundingMode.HALF_UP);
            BigDecimal callCost = cost;
            totalBill = totalBill.add(callCost);
            items.add(new LineItem(call, callCost));
        }

        billGenerator.send(customer, items, MoneyFormatter.penceToPounds(totalBill));
    }

    /**
     *  The class contains the call and its cost
     */
    public static class LineItem {
        private Call call;
        private BigDecimal callCost;

        public LineItem(Call call, BigDecimal callCost) {
            this.call = call;
            this.callCost = callCost;
        }

        public String date() {
            return call.date();
        }

        public String callee() {
            return call.callee();
        }

        public String durationMinutes() {
            return "" + call.durationSeconds() / 60 + ":" + String.format("%02d", call.durationSeconds() % 60);
        }

        public BigDecimal cost() {
            return callCost;
        }
    }
}
