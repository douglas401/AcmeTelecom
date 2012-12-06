package com.acmetelecom.unit;

import com.acmetelecom.billing.*;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DurationCalculatorTest {

	// A default duration calculator, with single peak period from 7am to 7pm, used in most of the tests here
    IDurationCalculator defaultDC = new DurationCalculator(new SinglePeakPeriod(7, 19));

    @Test
    // Test peak duration across 2 days
    public void testTwoDaysDuration() {
        DateTime startTime = new DateTime(2012, 11, 1, 18, 0, 0);
        DateTime endTime = startTime.plusDays(2);
        int peakDuration = defaultDC.getPeakDurationSeconds(startTime, endTime);
        assertEquals(2*12*60*60, peakDuration);
    }

    @Test
    // Test off peak durations during a day
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
    // Test peak duration cross over off peak
    public void testOffPeakOverPeakPeriod() {
        DateTime startTime = new DateTime(2012, 11, 1, 6, 30, 0);
        DateTime endTime = startTime.plusHours(14);
        int peakDuration = defaultDC.getPeakDurationSeconds(startTime, endTime);
        assertEquals(12*60*60,peakDuration);
    }

    @Test
    // Test simple 10mins peak period
    public void testPeakPeriod() {
        DateTime startTime = new DateTime(2012, 11, 1, 8, 30, 0);
        DateTime endTime = startTime.plusMinutes(10);
        int peakDuration = defaultDC.getPeakDurationSeconds(startTime, endTime);
        assertEquals(10*60,peakDuration);
    }

    @Test
    // Test off peak period over night, crossing peak period on next day
    public void testPeakPeriodOverNight() {
        DateTime startTime = new DateTime(2012, 11, 1, 18, 0, 0);
        DateTime endTime = startTime.plusHours(14);
        int peakDuration = defaultDC.getPeakDurationSeconds(startTime, endTime);
        assertEquals(2*60*60,peakDuration);
    }

    @Test
    // Test peak duration start from an off peak time
    public void testOffPeakToPeak() {
        DateTime startTime = new DateTime(2012, 11, 1, 22, 0, 0);
        DateTime endTime = startTime.plusHours(14);
        int peakDuration = defaultDC.getPeakDurationSeconds(startTime, endTime);
        assertEquals(5*60*60,peakDuration);
    }

    @Test
    // Test peak duration end with an off peak time
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
    // Test peak duration with a new set of long peak period from 2am - 11pm
    public void testOvernightAndLongPeak() {
        IDurationCalculator dC = new DurationCalculator(new SinglePeakPeriod(2,23));
        DateTime startTime = new DateTime(2012, 11, 1, 22, 0, 0);
        DateTime endTime = new DateTime(2012, 11, 2, 8, 0, 0);
        int peakDuration = dC.getPeakDurationSeconds(startTime, endTime);
        assertEquals(7*60*60, peakDuration);
    }
    
    @Test
    // Test peak duration with a new setting of long peak period from 2am - 11pm
    public void testOffPeakToOffPeakThroughPeak() {
    	PeakPeriod peakPeriod = new SinglePeakPeriod(10,16);
        IDurationCalculator dC = new DurationCalculator(peakPeriod);
        DateTime startTime = new DateTime(2012, 11, 1, 9, 58, 0);
        DateTime endTime = new DateTime(2012, 11, 1, 16, 2, 0);
        int peakDuration = dC.getPeakDurationSeconds(startTime, endTime);
        assertEquals(peakPeriod.getPeakPeriodSeconds(), peakDuration);
    }
    
    @Test
    // Test peak duration overnight with a new setting of short peak period from 10am - 4pm
    public void testPeakToPeakOvernightWithShortPeak() {
        IDurationCalculator dC = new DurationCalculator(new SinglePeakPeriod(10,16));
        DateTime startTime = new DateTime(2012, 11, 1, 15, 0, 0);
        DateTime endTime = new DateTime(2012, 11, 2, 11, 0, 0);
        int peakDuration = dC.getPeakDurationSeconds(startTime, endTime);
        assertEquals(2*60*60, peakDuration);
    }
    
    @Test
    // Test peak duration overnight with new setting of peak period from 8pm - 5am
    public void testPeakPeriodGoesThroughMidnight() {
    	IDurationCalculator dC = new DurationCalculator(new SinglePeakPeriod(20,5));
    	DateTime startTime = new DateTime(2012, 11, 1, 21, 0, 0);
    	DateTime endTime = new DateTime(2012, 11, 2, 4, 0, 0);
    	int peakDuration = dC.getPeakDurationSeconds(startTime, endTime);
    	assertEquals(7*60*60, peakDuration);
    }
    
    @Test
    // Test peak duration overnight end with off peak with new setting of peak period from 8pm - 5am
    public void testPeakPeriodGoesThroughMidnightCallFromPeakToOff() {
    	IDurationCalculator dC = new DurationCalculator(new SinglePeakPeriod(20,5));
    	DateTime startTime = new DateTime(2012, 11, 1, 21, 0, 0);
    	DateTime endTime = new DateTime(2012, 11, 2, 6, 0, 0);
    	int peakDuration = dC.getPeakDurationSeconds(startTime, endTime);
    	assertEquals(8*60*60, peakDuration);
    }
    
    @Test
    // Test off peak duration overnight with new setting of peak period from 8pm - 5am
    public void testPeakPeriodGoesThroughMidnightCallFromOffToOff() {
    	IDurationCalculator dC = new DurationCalculator(new SinglePeakPeriod(20,5));
    	DateTime startTime = new DateTime(2012, 11, 1, 6, 0, 0);
    	DateTime endTime = new DateTime(2012, 11, 1, 19, 0, 0);
    	int peakDuration = dC.getPeakDurationSeconds(startTime, endTime);
    	assertEquals(0, peakDuration);
    }
    
    @Test
    // Test off peak duration with a very short peak period from 10pm - 11pm
    public void testLongOffPeakCallWithShortPeakPeriod() {
    	IDurationCalculator dC = new DurationCalculator(new SinglePeakPeriod(22,23));
    	DateTime startTime = new DateTime(2012, 11, 1, 3, 0, 0);
    	DateTime endTime = new DateTime(2012, 11, 1, 20, 0, 0);
    	int peakDuration = dC.getPeakDurationSeconds(startTime, endTime);
    	assertEquals(0, peakDuration);
    }
    
    @Test
    // Test peak duration through a day with new setting of peak period from 9pm - 6am
    public void testOvernightPeakCallOnOneDay() {
    	IDurationCalculator dC = new DurationCalculator(new SinglePeakPeriod(21,6));
    	DateTime startTime = new DateTime(2012, 11, 1, 3, 0, 0);
    	DateTime endTime = new DateTime(2012, 11, 1, 22, 0, 0);
    	int peakDuration = dC.getPeakDurationSeconds(startTime, endTime);
    	assertEquals(4*60*60, peakDuration);
    }

    @Test
    // Test peak duration through a day with new setting of peak period from 9pm - 6am
    public void testMultiplePeakCall() {
        IDurationCalculator dC = new DurationCalculator(new SinglePeakPeriod(9,12), new SinglePeakPeriod(16, 21));
        DateTime startTime = new DateTime(2012, 11, 1, 6, 0, 0);
        DateTime endTime = new DateTime(2012, 11, 1, 22, 0, 0);
        int peakDuration = dC.getPeakDurationSeconds(startTime, endTime);
        assertEquals(8*60*60, peakDuration);
    }
     
}
