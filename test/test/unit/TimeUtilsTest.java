package test.unit;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;

import com.acmetelecom.Utils.DaytimePeakPeriod;
import com.acmetelecom.Utils.TimeUtils;

public class TimeUtilsTest {

	@Test
	public void testThreeSecondDuration() {
		DateTime startTime = DateTime.now();
		DateTime endTime = startTime.plusSeconds(3);
		int peakDuration = TimeUtils.getPeakDurationSeconds(startTime, endTime, new DaytimePeakPeriod(7,19));
		assertEquals(peakDuration,3);
	}

	@Test
	public void testThreeDaysDuration() {
		DateTime startTime = DateTime.now();
		DateTime endTime = startTime.plusDays(1);
		int peakDuration = TimeUtils.getPeakDurationSeconds(startTime, endTime, new DaytimePeakPeriod(7,19));
		assertEquals(peakDuration,24*60*60);
	}
	
}
