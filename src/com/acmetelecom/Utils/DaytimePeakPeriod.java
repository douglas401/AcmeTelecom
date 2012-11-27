package com.acmetelecom.Utils;

import java.util.Calendar;
import java.util.Date;

public class DaytimePeakPeriod {

    /**
     * The start time of peak period.
     */
	public final int PeakPeriodStart = 7;

    /**
     * The end time of peak period.
     */
	public final int PeakPeriodEnd = 19;

    /**
     * Gets the length of peak period in hours.
     * @return The length of peak period.
     */
    public int getPeakPeriodHours(){
        return this.PeakPeriodEnd - this.PeakPeriodStart;
    }

    /**
     * Gets the length of peak period in seconds.
     * @return The length of peak period.
     */
	public long getPeakPeriodSeconds(){
		return ( this.PeakPeriodEnd - this.PeakPeriodStart ) * 60 * 60;
	}

    /**
     * Determines if a specific time is in off peak period.
     * @param time The time to be tested.
     * @return True if the time is in off peak period. Otherwise false.
     */
    public boolean offPeak(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return hour < PeakPeriodStart || hour >= PeakPeriodEnd;
    }
}
