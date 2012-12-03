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
        	// On the day the call starts, the start of the peak period
        	DateTime startDayPeakStart = callStart.withTimeAtStartOfDay().plusHours(peakPeriod.getPeakPeriodStart());
        	// The end of the peak period that starts on the day of the call
        	DateTime startDayPeakEnd = startDayPeakStart.plusHours(peakPeriod.getPeakPeriodHours());
        	// On the day the call ends, the start of the peak period
        	DateTime endDayPeakStart = callEnd.withTimeAtStartOfDay().plusHours(peakPeriod.getPeakPeriodStart());
        	// The end of the peak period that starts on the day the call ends
        	DateTime endDayPeakEnd = endDayPeakStart.plusHours(peakPeriod.getPeakPeriodHours());
        	// First day peak interval
        	Interval firstDayPeak = new Interval(startDayPeakStart, startDayPeakEnd);
        	// Second day peak interval
        	Interval secondDayPeak = new Interval(endDayPeakStart, endDayPeakEnd);

        	// The call interval
        	Interval call = new Interval(callStart, callEnd);
        	
        	// Call starts in off-peak, ends in off-peak
            if (peakPeriod.offPeak(callStart.toDate()) && peakPeriod.offPeak(callEnd.toDate())){
            	// Call starts before a peak period, ends after it
            	if (call.overlaps(firstDayPeak) || call.overlaps(secondDayPeak)){
            		return durationOfAPeakPeriod;
            	} 
            	// Call is within an off peak period
            	else {
            		return 0;
            	}
            } 
            // Call starts in peak, ends in peak
            else if(!peakPeriod.offPeak(callStart.toDate()) && !peakPeriod.offPeak(callEnd.toDate())) {

                if(!firstDayPeak.contains(callStart)){
                    startDayPeakEnd = startDayPeakEnd.minusDays(1);
                }
                Interval offPeakBetweenPeaks = !firstDayPeak.equals(secondDayPeak)
                        ? new Interval(startDayPeakEnd, endDayPeakStart)
                        : new Interval(startDayPeakEnd, endDayPeakStart.plusDays(1));

                // Call starts and ends at different peaks
            	if (call.overlaps(offPeakBetweenPeaks)){
                    Duration offPeakDuration = new Duration(startDayPeakEnd, endDayPeakStart);
                    return (int) (callDuration.getStandardSeconds() - offPeakDuration.getStandardSeconds());
            	}
            	// Call starts and ends in the same peak period
            	else {
            		return (int) callDuration.getStandardSeconds();
            	}
            } 
            // Call starts in peak, ends in off-peak OR call starts in off-peak, ends in peak
            else {
            	// Call starts in off-peak
            	if (!firstDayPeak.contains(callStart) && !secondDayPeak.contains(callStart)){
            		Duration peakDuration;
            		// Call ends in first days peak period
            		if (firstDayPeak.contains(callEnd)){
            			peakDuration = new Duration(startDayPeakStart, callEnd);
            		} 
            		// Call ends in second days peak period
            		else {
            			peakDuration = new Duration(endDayPeakStart, callEnd);
            		}
            		return (int) peakDuration.getStandardSeconds();
            	}
            	// Call starts in peak
            	else {
            		Duration peakDuration;
            		// Call starts in first days peak period
            		if (firstDayPeak.contains(callStart)){
            			peakDuration = new Duration(callStart, startDayPeakEnd);
            		} 
            		// Call starts in second days peak period
            		else {
            			peakDuration = new Duration(callStart, endDayPeakEnd);
            		}
            		return (int) peakDuration.getStandardSeconds();
            	}
            }
        } else {
            return durationOfAPeakPeriod + getPeakDuration(callStart.plusDays(1), callEnd, numberOfDays - 1);
        }
    }
}
