package com.acmetelecom.billing;

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
     * Check if the time is in off peak
     * @param time the time needed to check
     * @return true if the time is in off peak, false otherwise
     */
    public abstract boolean offPeak(Date time);

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


}