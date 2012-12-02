package com.acmetelecom.billing;

import java.util.Calendar;
import java.util.Date;

// TODO: CHANGE THIS CLASS TO ONLY USE JODADATETIME... SHOULD NOT BE LIMITED TO ONLY HOURS..

/**
 * @see PeakPeriod
 *
 */
public class SinglePeakPeriod implements PeakPeriod {

	private int start = 0;
	private int end = 0;
	
	public int getPeakPeriodStart() {
		return start;
	}

	public int getPeakPeriodEnd() {
		return end;
	}

	public SinglePeakPeriod(){
        start = 7;
        end = 19;
	}
	
    public SinglePeakPeriod(int peakPeriodStart, int peakPeriodEnd) {
		start = peakPeriodStart;
		end = peakPeriodEnd;
	}

	public int getPeakPeriodHours(){
        return this.end > this.start ? this.end - this.start : (24-this.start) + this.end;
    }

	public long getPeakPeriodSeconds(){
		return getPeakPeriodHours() * 60 * 60;
	}
	
    public boolean offPeak(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (start < end) { 
        	return hour < start || hour >= end;
        } else {
        	return hour < start && hour >= end;
        }
    }

}
