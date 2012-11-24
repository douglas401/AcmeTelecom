package com.acmetelecom;

import com.acmetelecom.customer.CentralCustomerDatabase;
import com.acmetelecom.customer.CentralTariffDatabase;
import com.acmetelecom.customer.Customer;
import com.acmetelecom.customer.Tariff;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class BillingSystem {

    private List<CallEvent> callLog = new ArrayList<CallEvent>();
    private IBillGenerator billGenerator;

    public BillingSystem(IBillGenerator billGenerator) {
        this.billGenerator = billGenerator;
    }

    public void callInitiated(String caller, String callee) {
        callLog.add(new CallStart(caller, callee));
    }

    public void callCompleted(String caller, String callee) {
        callLog.add(new CallEnd(caller, callee));
    }

    public void createCustomerBills() {
        List<Customer> customers = CentralCustomerDatabase.getInstance().getCustomers();
        for (Customer customer : customers) {
            createBillFor(customer);
        }
        callLog.clear();
    }

    private void createBillFor(Customer customer) {
        List<CallEvent> customerEvents = new ArrayList<CallEvent>();
        for (CallEvent callEvent : callLog) {
            if (callEvent.getCaller().equals(customer.getPhoneNumber())) {
                customerEvents.add(callEvent);
            }
        }

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

            Tariff tariff = CentralTariffDatabase.getInstance().tarriffFor(customer);

            BigDecimal cost;

            DaytimePeakPeriod peakPeriod = new DaytimePeakPeriod();

            // TODO: change the implementation as the new feature describes
            if (peakPeriod.offPeak(call.startTime()) && peakPeriod.offPeak(call.endTime()) && call.durationSeconds() < 12 * 60 * 60) {
                cost = new BigDecimal(call.durationSeconds()).multiply(tariff.offPeakRate());
            } else {
                //cost = new BigDecimal(call.durationSeconds()).multiply(tariff.peakRate());
                int peakDurationSeconds = this.getPeakDurationSeconds(call.startTime(), call.endTime());
                int offPeakDurationSeconds = call.durationSeconds() - peakDurationSeconds;
                cost = new BigDecimal(peakDurationSeconds).multiply(tariff.peakRate()).add(new BigDecimal(offPeakDurationSeconds).multiply(tariff.offPeakRate()));
            }

            /*
            * Utils.getPeakDuration(call.StartTime(),call.endTime())  = peakDuration
            * OffPeakDuration = call.Duration - peakDuration
            *
            *        offpeakCost = new BigDecimal(offpeakDuration.multiply(tariff.offPeakRate());
            *        peakCost    = new BigDecimal(peakDuration.multiply(tariff.peakRate());
            *
            * cost = offpeakCost + peakCost;
            * */

            cost = cost.setScale(0, RoundingMode.HALF_UP);
            BigDecimal callCost = cost;
            totalBill = totalBill.add(callCost);
            items.add(new LineItem(call, callCost));
        }

		billGenerator.send(customer, items, MoneyFormatter.penceToPounds(totalBill));
    }

    private int getPeakDurationSeconds(Date startTime, Date endTime){
        int peakDurationSeconds = 0;
        long start = startTime.getTime() / 1000;
        long end = endTime.getTime() / 1000;
        while((end -start) > 24 * 60 * 60){
            peakDurationSeconds += 12 * 60 * 60;
            end -= 24* 60 * 60;
        }
        endTime.setTime(end * 1000);

        DaytimePeakPeriod peakPeriod = new DaytimePeakPeriod();

        //int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if(peakPeriod.offPeak(startTime) && peakPeriod.offPeak(endTime) && (end -start) > 12 * 60 * 60) {
            // phone call covered whole peak period
            peakDurationSeconds += 12;
        } else if(!peakPeriod.offPeak(startTime) && !peakPeriod.offPeak(endTime)) {
            // phone call happened within peak period
            peakDurationSeconds += (end -start);
        } else if(peakPeriod.offPeak(startTime) && !peakPeriod.offPeak(endTime)) {
            // peak time duration starts from 7am till call ends
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endTime);
            int hour = calendar.get(Calendar.HOUR_OF_DAY) - 7;
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            peakDurationSeconds += hour * 60 * 60 + minute * 60 + second;
        } else if(!peakPeriod.offPeak(startTime) && peakPeriod.offPeak(endTime)) {
            // peak time duration starts since call starts, ends at 7pm (19:00)
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startTime);
            int hour = calendar.get(Calendar.HOUR_OF_DAY) - 19;
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            peakDurationSeconds += hour * 60 * 60 + minute * 60 + second;
        }

        return peakDurationSeconds;
    }

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
