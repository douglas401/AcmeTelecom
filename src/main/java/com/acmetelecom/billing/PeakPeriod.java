package com.acmetelecom.billing;

import java.util.ArrayList;
import java.util.Date;

/**
 * PeakPeriod is the interface represents the peak period
 */
public interface PeakPeriod {

    /**
     * Get the peak period in hours
     * @return peak period of hours in int
     */
    public abstract int getPeakPeriodHours();

    /**
     * Get the peak period in seconds
     * @return peak period of seconds in long
     */
    public abstract long getPeakPeriodSeconds();

    /**
     * Get the start time of peak period
     * @return start time of the peak period in int
     */
    public abstract int getPeakPeriodStart();

    /**
     * Get the end time of peak period
     * @return end time of the peak period in int
     */
    public abstract int getPeakPeriodEnd();

    /**
     * Get the end time of peak period
     * @return end time of the peak period in int
     */
    ArrayList<PeakPeriod> getAllPeriods();
}