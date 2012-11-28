package com.acmetelecom.utils;

import org.joda.time.DateTime;

/**
 * Created with IntelliJ IDEA.
 * User: jt1209
 * Date: 11/28/12
 * Time: 8:59 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ITimeUtils {
    public int getPeakDurationSeconds(DateTime start, DateTime end);
}
