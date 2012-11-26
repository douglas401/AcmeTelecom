package com.acmetelecom.Utils;

import java.util.Calendar;
import java.util.Date;

public class DaytimePeakPeriod {

	public final int PeakPeriodStart;
	public final int PeakPeriodEnd;
	
	/**
	 * for example DaytimePeakPeriod(7,19) means
	 * peak periods start at 7am and ends at 7pm
	 * @param peakStart
	 * @param peakEnds
	 */
	public DaytimePeakPeriod(int peakStart,int peakEnds){
		PeakPeriodStart = peakStart;
		PeakPeriodEnd = peakEnds;
	}
	
	public int getPeakPeriodDuration(){
		return this.PeakPeriodEnd - this.PeakPeriodStart;
	}
	
    public boolean offPeak(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return hour < PeakPeriodStart || hour >= PeakPeriodEnd;
    }
}
