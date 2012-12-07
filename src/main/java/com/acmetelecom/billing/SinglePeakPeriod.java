package com.acmetelecom.billing;

import java.util.ArrayList;

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

    public ArrayList<PeakPeriod> getAllPeriods() {
        ArrayList<PeakPeriod> result = new ArrayList<PeakPeriod>();
        if(start <= end){
            result.add(this);
        }else{
            result.add(new SinglePeakPeriod(0,end));
            result.add(new SinglePeakPeriod(start,24));
        }
        return result;
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


}
