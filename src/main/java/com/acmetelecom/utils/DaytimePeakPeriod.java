package com.acmetelecom.utils;

import java.util.Calendar;
import java.util.Date;

public class DaytimePeakPeriod {

	public final int PeakPeriodStart = 7;
	public final int PeakPeriodEnd = 19;

    public int getPeakPeriodHours(){
        return this.PeakPeriodEnd - this.PeakPeriodStart;
    }

	public long getPeakPeriodSeconds(){
		return ( this.PeakPeriodEnd - this.PeakPeriodStart ) * 60 * 60;
	}
	
    public boolean offPeak(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return hour < PeakPeriodStart || hour >= PeakPeriodEnd;
    }
}
