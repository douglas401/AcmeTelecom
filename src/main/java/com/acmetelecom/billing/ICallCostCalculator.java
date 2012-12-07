package com.acmetelecom.billing;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import com.acmetelecom.customer.Customer;

public interface ICallCostCalculator {

    /**
     * Gets the length of phone call that takes place during peak period.
     *
     * @param start The start time of phone call.
     * @param end   The end time of phone call.
     * @return The duration of phone call in peak period in seconds.
     */    
    public abstract BigDecimal getCallCost(DateTime start, DateTime end, Customer customer);

}