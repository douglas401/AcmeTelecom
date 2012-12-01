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

    // TODO: Clean this method up. Very confusing + hard to read, but works properly now
    // TODO: Do the clean up AFTER PeakPeriod is changed to use jodatime only.
    private int getPeakDuration(DateTime callStart, DateTime callEnd, int numberOfDays) {
        int durationOfOneDay = (int) peakPeriod.getPeakPeriodSeconds();
<<<<<<< HEAD
        Duration duration = new Duration(callStart, callEnd);
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
        	// The second off peak interval in between two peak periods
        	Interval secondOffPeakInBetweenPeaks = new Interval(startDayPeakEnd, startDayPeakEnd.plusHours(24 - peakPeriod.getPeakPeriodHours()));
       	    DateTime prevDayPeakStart = callStart.withTimeAtStartOfDay().minusDays(1).plusHours(peakPeriod.getPeakPeriodStart());
        	DateTime prevDayPeakEnd = prevDayPeakStart.plusHours(peakPeriod.getPeakPeriodHours());
        	// The first off peak interval in between two peak periods
        	Interval firstOffPeakInBetweenPeaks = new Interval(prevDayPeakEnd, startDayPeakStart);
        	// The call interval
        	Interval call = new Interval(callStart, callEnd);
        	
        	// Call starts in off-peak, ends in off-peak
            if (peakPeriod.offPeak(callStart.toDate()) && peakPeriod.offPeak(callEnd.toDate())){
            	// Call starts before a peak period, ends after it
            	if (call.overlaps(firstDayPeak) || call.overlaps(secondDayPeak)){
            		return durationOfOneDay;
            	} 
            	// Call is within an off peak period
            	else {
            		return 0;
            	}
            } 
            // Call starts in peak, ends in peak
            else if(!peakPeriod.offPeak(callStart.toDate()) && !peakPeriod.offPeak(callEnd.toDate())) {
            	// Call starts in one peak period, ends in another peak period
            	if (call.overlaps(secondOffPeakInBetweenPeaks) || call.overlaps(firstOffPeakInBetweenPeaks)){
            		Duration firstPeakSection;
            		// The call can overlap one of two off peak periods
            		// We test which it overlaps, and set the section accordingly
            		if (call.overlaps(firstOffPeakInBetweenPeaks)){
            			firstPeakSection = new Duration(callStart, prevDayPeakEnd);
            		} else {
            			firstPeakSection = new Duration(callStart, startDayPeakEnd);
            		}
            		Duration secondPeakSection = new Duration(endDayPeakStart, callEnd);
            		Duration peakDuration = firstPeakSection.plus(secondPeakSection);
            		return (int) peakDuration.getStandardSeconds();
            	}
            	// Call starts and ends in the same peak period
            	else {
            		return (int) duration.getStandardSeconds();
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
=======
        Duration duration = new Duration(start, end);
        if (numberOfDays < 1) {
            if (peakPeriod.offPeak(start.toDate()) && peakPeriod.offPeak(end.toDate())) {
                return (start.isBefore(start.withTimeAtStartOfDay().plusHours(peakPeriod.getPeakPeriodStart()))
                        && end.isAfter(end.withTimeAtStartOfDay().plusHours(peakPeriod.getPeakPeriodEnd()))
                        && !peakPeriod.isOvernightPeakPeriod())
                        ? durationOfOneDay : 0;
            } else if (!peakPeriod.offPeak(start.toDate()) && !peakPeriod.offPeak(end.toDate())) {
                DateTime endOfPeakOnStartDay = new DateTime(start.getYear(), start.getMonthOfYear(),
                        start.getDayOfMonth(), peakPeriod.getPeakPeriodEnd(), 0, 0);
                // If call goes overnight, from first days peak period to next days peak period
                if (end.isAfter(endOfPeakOnStartDay) && !peakPeriod.isOvernightPeakPeriod()) {
                    Duration firstPeakSection = new Duration(start, endOfPeakOnStartDay);
                    DateTime startOfPeakOnEndDay = new DateTime(end.getYear(), end.getMonthOfYear(),
                            end.getDayOfMonth(), peakPeriod.getPeakPeriodStart(), 0, 0);
                    Duration secondPeakSection = new Duration(startOfPeakOnEndDay, end);
                    Duration peakDuration = firstPeakSection.plus(secondPeakSection);
                    return (int) peakDuration.getStandardSeconds();
                } else if (end.isAfter(endOfPeakOnStartDay)
                        && end.isAfter(new DateTime(end.getYear(), end.getMonthOfYear(),
                        end.getDayOfMonth(), peakPeriod.getPeakPeriodStart(), 0, 0))
                        && peakPeriod.isOvernightPeakPeriod()) {
                    Duration firstPeakSection = new Duration(start, endOfPeakOnStartDay);
                    DateTime startOfPeakOnEndDay = new DateTime(end.getYear(), end.getMonthOfYear(),
                            end.getDayOfMonth(), peakPeriod.getPeakPeriodStart(), 0, 0);
                    Duration secondPeakSection = new Duration(startOfPeakOnEndDay, end);
                    Duration peakDuration = firstPeakSection.plus(secondPeakSection);
                    return (int) peakDuration.getStandardSeconds();
                }
                // If call is within a peak period
                else {
                    return (int) duration.getStandardSeconds();
                }
            } else {
                if (peakPeriod.offPeak(start.toDate())) {
                    if (!peakPeriod.isOvernightPeakPeriod()) {
                        DateTime nextDayPeakStart = end.withTimeAtStartOfDay().plusHours(peakPeriod.getPeakPeriodStart());
                        Duration peakDuration = new Duration(nextDayPeakStart, end);
                        return (int) peakDuration.getStandardSeconds();
                    } else {
                        return getDurationSecondsWhenOverlap(peakPeriod.getPeakPeriodStart(), end);
                    }
                } else {
                    return getDurationSecondsWhenOverlap(peakPeriod.getPeakPeriodEnd(), start);
                }
>>>>>>> f618c5eb78190c7cde23157ecf0b91a80a5c1638
            }
        } else {
            return durationOfOneDay + getPeakDuration(callStart.plusDays(1), callEnd, numberOfDays - 1);
        }
    }

    private int getDurationSecondsWhenOverlap(int peakPeriod, DateTime date) {
        Duration peakDuration;
        if (peakPeriod > date.getHourOfDay()) {
            peakDuration = new Duration(date, new DateTime(date.withTimeAtStartOfDay()).plusHours(peakPeriod));
        } else {
            DateTime nextDayStartPeriod = new DateTime(date.withTimeAtStartOfDay().plusDays(1).plusHours(peakPeriod));
            peakDuration = new Duration(date, nextDayStartPeriod);
        }
        return Math.abs((int) peakDuration.getStandardSeconds());
    }
}
