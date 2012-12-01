package com.acmetelecom.billing;

import org.joda.time.DateTime;

public interface IDurationCalculator {

    /**
     * Gets the length of phone call that takes place during peak period.
     *
     * @param start The start time of phone call.
     * @param end   The end time of phone call.
     * @return The duration of phone call in peak period in seconds.
     */
    public abstract int getPeakDurationSeconds(DateTime start, DateTime end);

}