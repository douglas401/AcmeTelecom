package com.acmetelecom.billing;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;

public class DurationCalculator implements IDurationCalculator {
    /**
     * The <cref>PeakPeriod</cref> used to get information of peak and off peak period.
     */
    public PeakPeriod peakPeriod;

    /**
     * Initializes the DurationCalculator with the defined PeakPeriod
     */
    public DurationCalculator(PeakPeriod peakPeriod) {
        this.peakPeriod = peakPeriod;
    }

    /**
     * Gets the length of phone call that takes place during peak period.
     * @param start The start time of phone call.
     * @param end   The end time of phone call.
     * @return The duration of phone call in peak period in seconds.
     */
    public int getPeakDurationSeconds(DateTime start, DateTime end) {
        Duration duration = new Duration(start, end);
        return getPeakDuration(start, end, duration.toStandardDays().getDays());
    }

    private int getPeakDuration(DateTime callStart, DateTime callEnd, int numberOfDays) {
        int durationOfAPeakPeriod = (int) peakPeriod.getPeakPeriodSeconds();
        Duration callDuration = new Duration(callStart, callEnd);
        if (numberOfDays < 1){

        	// peak interval that contains callStart
            Interval peakIntervalBeforeCallStart = LastPeakIntervalBeforeCallAction(callStart);
            // peak interval that contains callEnd
            Interval peakIntervalBeforeCallEnd = LastPeakIntervalBeforeCallAction(callEnd);
        	
        	// Call starts in off-peak, ends in off-peak
            if (peakPeriod.offPeak(callStart.toDate()) && peakPeriod.offPeak(callEnd.toDate())){
            	// Call never comes across the peak period
                // Otherwise, call starts before a peak period, through it and ends after it
            	return peakIntervalBeforeCallStart.equals(peakIntervalBeforeCallEnd) ? 0 : durationOfAPeakPeriod;
            } 
            // Call starts in peak, ends in peak
            else if(!peakPeriod.offPeak(callStart.toDate()) && !peakPeriod.offPeak(callEnd.toDate())) {

                // If the peak intervals are the same, which means call starts and ends at the same peak interval
                // Otherwise, call starts and ends at different intervals, need to find the off-peak interval in between
                if(peakIntervalBeforeCallStart.equals(peakIntervalBeforeCallEnd)){
                    return (int) callDuration.getStandardSeconds();
                } else {
                    Duration offPeakDuration = new Duration(peakIntervalBeforeCallStart.getEnd(), peakIntervalBeforeCallEnd.getStart());
                    return (int) (callDuration.getStandardSeconds() - offPeakDuration.getStandardSeconds());
            	}
            } else {
                // Intervals equal to each other means the call starts in off-peak, ends in peak.
                // Otherwise starts in peak, and ends in off-peak
                Duration peakDuration = peakIntervalBeforeCallStart.equals(peakIntervalBeforeCallEnd)
                        ? new Duration(callStart, peakIntervalBeforeCallStart.getEnd())
                        :new Duration(peakIntervalBeforeCallEnd.getStart(), callEnd);
                return (int) peakDuration.getStandardSeconds();
            }
        } else {
            return durationOfAPeakPeriod + getPeakDuration(callStart.plusDays(1), callEnd, numberOfDays - 1);
        }
    }

    /*
    * Return there is an peak interval that contains the date.
    * Otherwise, return the last peak interval, just before the date.
    * */
    private Interval LastPeakIntervalBeforeCallAction(DateTime date) {
        DateTime start = date.getHourOfDay() < peakPeriod.getPeakPeriodStart()
                ? date.withTimeAtStartOfDay().minusDays(1).plusHours(peakPeriod.getPeakPeriodStart())
                : date.withTimeAtStartOfDay().plusHours(peakPeriod.getPeakPeriodStart());
        DateTime end = start.plusHours(peakPeriod.getPeakPeriodHours());
        return new Interval(start, end);
    }
}
