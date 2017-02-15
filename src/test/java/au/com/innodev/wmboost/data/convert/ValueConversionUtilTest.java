package au.com.innodev.wmboost.data.convert;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;
import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;

import au.com.innodev.wmboost.data.convert.ValueConversionUtil;

public class ValueConversionUtilTest {

	
	@Test
	public void testDateToIsoStringMillis() {
		String input = "2017-02-13T12:34:56.789Z";		
		Date instant = DateTimeUtils.toDate(Instant.parse(input));
		
		assertEquals("2017-02-13T12:34:56.789Z", ValueConversionUtil.dateToIsoString(instant));
	}
	
	@Test
	public void testDateNoMillisToIsoString() {
		String input = "2017-02-13T12:34:56Z";		
		Date instant = DateTimeUtils.toDate(Instant.parse(input));
		
		assertEquals("2017-02-13T12:34:56.000Z", ValueConversionUtil.dateToIsoString(instant));
	}	
	
	@Test
	public void testIsoStringToDateMillis() {
		String input = "2017-02-13T12:34:56.789Z";		
		Date expected = DateTimeUtils.toDate(Instant.parse("2017-02-13T12:34:56.789Z"));
		
		assertEquals(expected, ValueConversionUtil.isoStringToDate(input));
	}
	
	@Test
	public void testIsoStringToDateNoMillis() {
		String input = "2017-02-13T12:34:56Z";		
		Date expected = DateTimeUtils.toDate(Instant.parse("2017-02-13T12:34:56Z"));
		
		assertEquals(expected, ValueConversionUtil.isoStringToDate(input));
	}
	
	@Test
	public void testIsoStringToDateWithTimeDiffMillis() {
		String input = "2017-02-13T12:34:56.789+10:00";
		
		Date expected = DateTimeUtils.toDate(OffsetDateTime.parse("2017-02-13T12:34:56.789+10:00").toInstant());
		
		assertEquals(expected, ValueConversionUtil.isoStringToDate(input));
	}
	
	@Test
	public void testIsoStringToDateWithTimeDiffNoMillis() {
		String input = "2017-02-13T12:34:56+10:00";
		
		Date expected = DateTimeUtils.toDate(OffsetDateTime.parse("2017-02-13T12:34:56+10:00").toInstant());
		
		assertEquals(expected, ValueConversionUtil.isoStringToDate(input));
	}
	
	@Test
	public void testIsoStringToDateNoColons() {
		String input = "20170213T123456Z";		
		Date expected = DateTimeUtils.toDate(Instant.parse("2017-02-13T12:34:56Z"));
		
		assertEquals(expected, ValueConversionUtil.isoStringToDate(input));
	}
	
	@Test(expected=ValueConversionUtil.InvalidValueConversionException.class)
	public void testIsoStringToDateNoTimezone() {
		String input = "2017-02-13T12:34:56";		
		
		ValueConversionUtil.isoStringToDate(input);
	}
	
	@Test(expected=ValueConversionUtil.InvalidValueConversionException.class)
	public void testInvalidDayMonthStringToDate() {
		String input = "2017-02-31T12:34:56Z";		
		
		ValueConversionUtil.isoStringToDate(input);
	}
	
	@Test(expected=ValueConversionUtil.InvalidValueConversionException.class)
	public void testTimeWithoutseparatorStringToDate() {
		String input = "2017-02-3112:34:56Z";		
		
		ValueConversionUtil.isoStringToDate(input);
	}

}
