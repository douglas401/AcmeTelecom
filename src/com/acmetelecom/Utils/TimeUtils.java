package com.acmetelecom.Utils;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class TimeUtils {
    public static final DaytimePeakPeriod peakPeriod = new DaytimePeakPeriod();

    public static int getPeakDurationSeconds(DateTime start, DateTime end) {
        Duration duration = new Duration(start, end);
        return getPeakDuration(start, end, duration.toStandardDays().getDays());
    }

    private static int getPeakDuration(DateTime start, DateTime end, int numberOfDays) {
        int total = (int)peakPeriod.getPeakPeriodDuration();
        if(numberOfDays < 1){
            if (peakPeriod.offPeak(start.toDate()) && peakPeriod.offPeak(end.toDate())){
                return new Duration(start, end).getStandardHours() > peakPeriod.getPeakPeriodHours()
                        ? (int)peakPeriod.getPeakPeriodDuration() : 0;
            } else if(peakPeriod.offPeak(start.toDate())){
                return (end.getHourOfDay() - peakPeriod.PeakPeriodStart) * 60 * 60
                        + (60 - end.getMinuteOfHour()) * 60 + (60 - end.getSecondOfMinute());
            } else if(peakPeriod.offPeak(end.toDate())){
                return (peakPeriod.PeakPeriodEnd - start.getHourOfDay() ) * 60 * 60
                        + (60 - start.getMinuteOfHour()) * 60 + (60 - start.getSecondOfMinute());
            } else {
                return (int) (((end.getMillis() - start.getMillis()) / 1000));
            }
        } else {
            return total + getPeakDuration(start.plusDays(1), end, numberOfDays - 1);
        }
    }
}
