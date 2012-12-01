package com.acmetelecom.billing;

import org.joda.time.DateTime;
import org.joda.time.Duration;

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
    private int getPeakDuration(DateTime start, DateTime end, int numberOfDays) {
        int durationOfOneDay = (int) peakPeriod.getPeakPeriodSeconds();
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
            }
        } else {
            return durationOfOneDay + getPeakDuration(start.plusDays(1), end, numberOfDays - 1);
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
