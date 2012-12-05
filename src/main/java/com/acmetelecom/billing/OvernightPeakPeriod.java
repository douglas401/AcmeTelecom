package com.acmetelecom.billing;

public class OvernightPeakPeriod {

    private int start;
    private int end;

    public OvernightPeakPeriod(int morningPeakEnd, int eveningPeakStart) {
        if(morningPeakEnd < eveningPeakStart){
            start = morningPeakEnd;
            end = eveningPeakStart;
        }else {
            start = 0;
            end = 24;
        }
    }

    public PeakPeriod getMorningPeakBand(){
        return new SinglePeakPeriod(0,start);
    }

    public PeakPeriod getEveningPeakBand(){
        return new SinglePeakPeriod(end,24);
    }

}
