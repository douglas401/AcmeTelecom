package com.acmetelecom.Utils;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class TimeUtils {
    /**
     * The <cref>DaytimePeakPeriod</cref> used to get information of peak and off peak period.
     */
    public static final DaytimePeakPeriod peakPeriod = new DaytimePeakPeriod();

    /**
     * Gets the length of phone call lasted in peak period.
     * @param start The start time of phone call.
     * @param end The end time of phone call.
     * @return The duration of phone call in peak period in seconds.
     */
    public static int getPeakDurationSeconds(DateTime start, DateTime end) {
        Duration duration = new Duration(start, end);
        return getPeakDuration(start, end, duration.toStandardDays().getDays());
    }

    private static int getPeakDuration(DateTime start, DateTime end, int numberOfDays) {
        int durationOfOneDay = (int)peakPeriod.getPeakPeriodSeconds();
        Duration duration = new Duration(start, end);
        if(numberOfDays < 1){
            if (peakPeriod.offPeak(start.toDate()) && peakPeriod.offPeak(end.toDate())){
                return duration.getStandardHours() > peakPeriod.getPeakPeriodHours()
                        ? durationOfOneDay : 0;
            } else if(!peakPeriod.offPeak(start.toDate()) && !peakPeriod.offPeak(end.toDate())) {
                return duration.getStandardHours() > peakPeriod.getPeakPeriodHours()
                        ? (int) (duration.getStandardSeconds() - peakPeriod.getPeakPeriodSeconds())
                        : (int) duration.getStandardSeconds();
            } else {
                return peakPeriod.offPeak(start.toDate())
                        ? getDurationSecondsWhenOverlap(peakPeriod.PeakPeriodStart, end)
                        : getDurationSecondsWhenOverlap(peakPeriod.PeakPeriodEnd, start);
            }
        } else {
            return durationOfOneDay + getPeakDuration(start.plusDays(1), end, numberOfDays - 1);
        }
    }

    private static int getDurationSecondsWhenOverlap(int peakPeriod, DateTime date) {
        Duration peakDuration = new Duration(date, new DateTime(date.withTimeAtStartOfDay()).plusHours(peakPeriod));
        return Math.abs((int) peakDuration.getStandardSeconds());
    }
}
