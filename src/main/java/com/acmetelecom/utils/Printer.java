package com.acmetelecom.utils;

public interface Printer {

    void printHeading(String name, String phoneNumber, String pricePlan);

    void printItem(String time, String callee, String duration, String cost);

    void printTotal(String total);
}
