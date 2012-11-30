package com.acmetelecom.billing;

import java.util.Date;

public interface PeakPeriod {

	public abstract int getPeakPeriodHours();

	public abstract long getPeakPeriodSeconds();

	public abstract boolean offPeak(Date time);
	
	public abstract int getPeakPeriodStart();
	
	public abstract int getPeakPeriodEnd();
	
	public abstract boolean isOvernightPeakPeriod();

}