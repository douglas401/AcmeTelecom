package com.acmetelecom.billing;

import com.acmetelecom.customer.Customer;

import java.util.List;

public interface IBillGenerator {

    /**
     * Send the bill generated of a customer with all his/her calls made
     * @param customer the customer of the bill(caller)
     * @param calls list of calls with their prices made by customer
     * @param totalBill the total amount of bill
     */
    public abstract void send(Customer customer,
                              List<BillingSystem.LineItem> calls, String totalBill);

}