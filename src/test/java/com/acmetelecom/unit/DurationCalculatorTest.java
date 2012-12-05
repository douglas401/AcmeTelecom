package com.acmetelecom.unit;

import com.acmetelecom.billing.SinglePeakPeriod;
import com.acmetelecom.billing.DurationCalculator;
import com.acmetelecom.billing.IDurationCalculator;
import com.acmetelecom.billing.PeakPeriod;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DurationCalculatorTest {

	// A default duration calculator, with single peak period from 7am to 7pm, used in most of the tests here
    IDurationCalculator defaultDC = new DurationCalculator(new SinglePeakPeriod(7, 19));

    @Test
    public void testTwoDaysDuration() {
        DateTime startTime = new DateTime(2012, 11, 1, 18, 0, 0);
        DateTime endTime = startTime.plusDays(2);
        int peakDuration = defaultDC.getPeakDurationSeconds(startTime, endTime);
        assertEquals(2*12*60*60, peakDuration);
    }

    @Test
    public void testOffPeak() {
        DateTime startTime = new DateTime(2012, 11, 1, 19, 30, 0);
        DateTime endTime = startTime.plusMinutes(5);
        int peakDuration = defaultDC.getPeakDurationSeconds(startTime, endTime);
        assertEquals(0,peakDuration);

        // over night
        DateTime startTime2 = new DateTime(2012, 11, 1, 23, 30, 0);
        DateTime endTime2 = startTime.plusHours(4);
        int peakDuration2 = defaultDC.getPeakDurationSeconds(startTime2, endTime2);
        assertEquals(0,peakDuration2);
    }

    @Test
    public void testOffPeakOverPeakPeriod() {
        DateTime startTime = new DateTime(2012, 11, 1, 6, 30, 0);
        DateTime endTime = startTime.plusHours(14);
        int peakDuration = defaultDC.getPeakDurationSeconds(startTime, endTime);
        assertEquals(12*60*60,peakDuration);
    }

    @Test
    public void testPeakPeriod() {
        DateTime startTime = new DateTime(2012, 11, 1, 8, 30, 0);
        DateTime endTime = startTime.plusMinutes(10);
        int peakDuration = defaultDC.getPeakDurationSeconds(startTime, endTime);
        assertEquals(10*60,peakDuration);
    }

    @Test
    public void testPeakPeriodOverNight() {
        DateTime startTime = new DateTime(2012, 11, 1, 18, 0, 0);
        DateTime endTime = startTime.plusHours(14);
        int peakDuration = defaultDC.getPeakDurationSeconds(startTime, endTime);
        assertEquals(2*60*60,peakDuration);
    }

    @Test
    public void testOffPeakToPeak() {
        DateTime startTime = new DateTime(2012, 11, 1, 22, 0, 0);
        DateTime endTime = startTime.plusHours(14);
        int peakDuration = defaultDC.getPeakDurationSeconds(startTime, endTime);
        assertEquals(5*60*60,peakDuration);
    }

    @Test
    public void testPeakToOffPeak() {
        DateTime startTime = new DateTime(2012, 11, 1, 16, 0, 0);
        DateTime endTime = startTime.plusHours(5);
        int peakDuration = defaultDC.getPeakDurationSeconds(startTime, endTime);
        assertEquals(3*60*60,peakDuration);

        // over night
        DateTime startTime2 = new DateTime(2012, 11, 1, 18, 30, 0);
        DateTime endTime2 = startTime.plusHours(10);
        int peakDuration2 = defaultDC.getPeakDurationSeconds(startTime2, endTime2);
        assertEquals(30*60,peakDuration2);
    }
    
    @Test
    public void testOvernightAndLongPeak() {
        IDurationCalculator dC = new DurationCalculator(new SinglePeakPeriod(2,23));
        DateTime startTime = new DateTime(2012, 11, 1, 22, 0, 0);
        DateTime endTime = new DateTime(2012, 11, 2, 8, 0, 0);
        int peakDuration = dC.getPeakDurationSeconds(startTime, endTime);
        assertEquals(7*60*60, peakDuration);
    }
    
    @Test
    public void testOffPeakToOffPeakThroughPeak() {
    	PeakPeriod peakPeriod = new SinglePeakPeriod(10,16);
        IDurationCalculator dC = new DurationCalculator(peakPeriod);
        DateTime startTime = new DateTime(2012, 11, 1, 9, 58, 0);
        DateTime endTime = new DateTime(2012, 11, 1, 16, 2, 0);
        int peakDuration = dC.getPeakDurationSeconds(startTime, endTime);
        assertEquals(peakPeriod.getPeakPeriodSeconds(), peakDuration);
    }
    
    @Test
    public void testPeakToPeakOvernightWithShortPeak() {
        IDurationCalculator dC = new DurationCalculator(new SinglePeakPeriod(10,16));
        DateTime startTime = new DateTime(2012, 11, 1, 15, 0, 0);
        DateTime endTime = new DateTime(2012, 11, 2, 11, 0, 0);
        int peakDuration = dC.getPeakDurationSeconds(startTime, endTime);
        assertEquals(2*60*60, peakDuration);
    }
    
    @Test
    public void testPeakPeriodGoesThroughMidnight() {
    	IDurationCalculator dC = new DurationCalculator(new SinglePeakPeriod(20,5));
    	DateTime startTime = new DateTime(2012, 11, 1, 21, 0, 0);
    	DateTime endTime = new DateTime(2012, 11, 2, 4, 0, 0);
    	int peakDuration = dC.getPeakDurationSeconds(startTime, endTime);
    	assertEquals(7*60*60, peakDuration);
    }
    
    @Test
    public void testPeakPeriodGoesThroughMidnightCallFromPeakToOff() {
    	IDurationCalculator dC = new DurationCalculator(new SinglePeakPeriod(20,5));
    	DateTime startTime = new DateTime(2012, 11, 1, 21, 0, 0);
    	DateTime endTime = new DateTime(2012, 11, 2, 6, 0, 0);
    	int peakDuration = dC.getPeakDurationSeconds(startTime, endTime);
    	assertEquals(8*60*60, peakDuration);
    }
    
    @Test
    public void testPeakPeriodGoesThroughMidnightCallFromOffToOff() {
    	IDurationCalculator dC = new DurationCalculator(new SinglePeakPeriod(20,5));
    	DateTime startTime = new DateTime(2012, 11, 1, 6, 0, 0);
    	DateTime endTime = new DateTime(2012, 11, 1, 19, 0, 0);
    	int peakDuration = dC.getPeakDurationSeconds(startTime, endTime);
    	assertEquals(0, peakDuration);
    }
    
    @Test
    public void testLongOffPeakCallWithShortPeakPeriod() {
    	IDurationCalculator dC = new DurationCalculator(new SinglePeakPeriod(22,23));
    	DateTime startTime = new DateTime(2012, 11, 1, 3, 0, 0);
    	DateTime endTime = new DateTime(2012, 11, 1, 20, 0, 0);
    	int peakDuration = dC.getPeakDurationSeconds(startTime, endTime);
    	assertEquals(0, peakDuration);
    }
    
    @Test
    public void testOvernightPeakCallOnOneDay() {
    	IDurationCalculator dC = new DurationCalculator(new SinglePeakPeriod(21,6));
    	DateTime startTime = new DateTime(2012, 11, 1, 3, 0, 0);
    	DateTime endTime = new DateTime(2012, 11, 1, 22, 0, 0);
    	int peakDuration = dC.getPeakDurationSeconds(startTime, endTime);
    	assertEquals(4*60*60, peakDuration);
    }
     
}
