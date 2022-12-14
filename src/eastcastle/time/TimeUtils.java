package eastcastle.time;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * Time constants and utilities
 * </p>
 */
public class TimeUtils {
   public static final int MILLIS_PER_SECOND = 1000;
   public static final int MICROS_PER_MILLI = 1000;
   public static final int SECONDS_PER_MINUTE = 60;
   public static final int MINUTES_PER_HOUR = 60;
   public static final int HOURS_PER_DAY = 24;
   public static final int MILLIS_PER_MINUTE = SECONDS_PER_MINUTE * MILLIS_PER_SECOND;
   public static final int MILLIS_PER_HOUR = MINUTES_PER_HOUR * MILLIS_PER_MINUTE;
   public static final int MILLIS_PER_DAY = HOURS_PER_DAY * MILLIS_PER_HOUR;
   public static final String myTimeZoneName = "America/New_York";

   public static final int secondsInMillis(int seconds) {
      return seconds * MILLIS_PER_SECOND;
   }

   public static final int minutesInMillis(int minutes) {
      return secondsInMillis(minutes * SECONDS_PER_MINUTE);
   }

   public static final int hoursInMillis(int hours) {
      return minutesInMillis(hours * MINUTES_PER_HOUR);
   }

   public static String getCurrentTimeString() {
      Date curTime;
      Calendar curCal;

      curCal = Calendar.getInstance();
      curTime = curCal.getTime();
      return curTime.toString();
   }

   public static void checkTooManyMillis(long millis) {
      if (millis > Integer.MAX_VALUE) {
         throw new RuntimeException("Overflow");
      }
   }

   public static long nanos2millisLong(long nanos) {
      return nanos / StopwatchInternalConstants.nanosPerMilli;
   }

   public static double nanos2seconds(long nanos) {
      return (double) nanos / StopwatchInternalConstants.nanosPerSecond;
   }

   public static BigDecimal nanos2secondsBD(long nanos) {
      return new BigDecimal(nanos).divide(StopwatchInternalConstants.nanosPerSecondBD, MathContext.DECIMAL128);
   }
}
