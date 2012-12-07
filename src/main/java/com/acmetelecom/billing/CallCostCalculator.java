package com.acmetelecom.billing;

import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.acmetelecom.customer.CentralTariffDatabase;
import com.acmetelecom.customer.Customer;
import com.acmetelecom.customer.Tariff;
import com.acmetelecom.customer.TariffLibrary;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CallCostCalculator implements ICallCostCalculator {
    /**
     * The <cref>PeakPeriod</cref> used to get information of peak and off peak period.
     */
    private ArrayList<PeakPeriod> peakPeriods = new ArrayList<PeakPeriod>();
    
    private TariffLibrary tariffDatabase = CentralTariffDatabase.getInstance();

    /**
     * Initializes the CallCostCalculator with the defined PeakPeriod
     */
    public CallCostCalculator(PeakPeriod ... peakPeriods) {
        for (PeakPeriod p : peakPeriods){
            ArrayList<PeakPeriod> thisPeakPeriods = p.getAllPeriods();
            for (PeakPeriod thisP : thisPeakPeriods){
                this.peakPeriods.add(thisP);
            }
        }
    }
    
    /**
     * Gets the cost of a phone call.
     * @param start  the time of phone call
     * @param end the end time of phone call
     * @return the total cost of this phone call
     */
    public BigDecimal getCallCost(DateTime start, DateTime end,
			Customer customer) {
		Tariff tariff = tariffDatabase.tarriffFor(customer);

        BigDecimal cost;
        Duration call = new Duration(start, end);
        
        int peakDurationSeconds = getPeakDurationSeconds(start, end);
        int offPeakDurationSeconds = (int) (call.getStandardSeconds() - peakDurationSeconds);
        cost = new BigDecimal(peakDurationSeconds).multiply(tariff.peakRate()).add(new BigDecimal(offPeakDurationSeconds).multiply(tariff.offPeakRate()));

		
		return cost;
	}

    /**
     * Gets the length of phone call that takes place during peak period.
     * @param start The start time of phone call.
     * @param end   The end time of phone call.
     * @return The duration of phone call in peak period in seconds.
     */
    private int getPeakDurationSeconds(DateTime start, DateTime end) {

        int durationForFirstDay;
        if(DateUtils.isSameDay(start.toDate(), end.toDate())){
            durationForFirstDay = getPeakDurationWithinSingleDay(start,end);
            return durationForFirstDay;
        }else{
            DateTime startOfNextDay = start.withTimeAtStartOfDay().plusDays(1);
            durationForFirstDay = getPeakDurationWithinSingleDay(start,startOfNextDay);
            return durationForFirstDay + getPeakDurationSeconds(startOfNextDay, end);
        }
    }

    /**
     * Gets the length of phone call that takes place during a single day period.
     * @param start The start time of phone call.
     * @param end   The end time of phone call.
     * @return The duration of phone call in peak period in seconds.
     */
    private int getPeakDurationWithinSingleDay(DateTime start, DateTime end) {
        int totalDuration = 0;
        for(PeakPeriod each : peakPeriods){
             totalDuration += getPeakDurationWithinSinglePeakBand(start,end,each);
        }
        return totalDuration;
    }

    /**
     * Gets the length of phone call that takes place during a single day period as well as a single peak band.
     * @param start The start time of phone call.
     * @param end   The end time of phone call.
     * @return The duration of phone call in peak period in seconds.
     */
    private int getPeakDurationWithinSinglePeakBand(DateTime start, DateTime end, PeakPeriod peakBand) {
        DateTime peakStart = start.withTimeAtStartOfDay().plusHours(peakBand.getPeakPeriodStart());
        DateTime peakEnd   = start.withTimeAtStartOfDay().plusHours(peakBand.getPeakPeriodEnd());

        if(start.isAfter(peakEnd) || end.isBefore(peakStart)){
           return 0;
        }

        // at this stage, start must before the peakEnd and end must after peakStart, so must be within peak
        Duration nonPeakDuration = new Duration(start, peakStart);
        Duration nonPeakDuration2 = new Duration(peakEnd, end);

        int nonPeak1 = Math.max((int)nonPeakDuration.getStandardSeconds(), 0);
        int nonPeak2 = Math.max((int)nonPeakDuration2.getStandardSeconds(), 0);

        Duration totalDuration = new Duration(start, end);
        return (int)totalDuration.getStandardSeconds() - nonPeak1 - nonPeak2;
    }
}
