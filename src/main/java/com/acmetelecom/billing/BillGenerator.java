package com.acmetelecom.billing;

import com.acmetelecom.customer.Customer;
import com.acmetelecom.utils.HtmlPrinter;
import com.acmetelecom.utils.MoneyFormatter;
import com.acmetelecom.utils.Printer;

import java.util.List;

public class BillGenerator implements IBillGenerator {

    /* (non-Javadoc)
	 * @see com.acmetelecom.billing.IBillGenerator#send(com.acmetelecom.customer.Customer, java.util.List, java.lang.String)
	 */
    public void send(Customer customer, List<BillingSystem.LineItem> calls, String totalBill) {

        Printer printer = HtmlPrinter.getInstance();
        printer.printHeading(customer.getFullName(), customer.getPhoneNumber(), customer.getPricePlan());
        for (BillingSystem.LineItem call : calls) {
            printer.printItem(call.date(), call.callee(), call.durationMinutes(), MoneyFormatter.penceToPounds(call.cost()));
        }
        printer.printTotal(totalBill);
    }

}
