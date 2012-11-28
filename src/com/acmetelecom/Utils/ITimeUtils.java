package com.acmetelecom.Utils;

import org.joda.time.DateTime;

/**
 * Created with IntelliJ IDEA.
 * User: jt1209
 * Date: 11/27/12
 * Time: 9:40 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ITimeUtils {
    public int getPeakDurationSeconds(DateTime start, DateTime end);
}
