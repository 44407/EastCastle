package eastcastle.time;

import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

import eastcastle.util.SafeTimer;
import eastcastle.util.SafeTimerTask;

public class TimerDrivenAbsNanosTimeSource extends TimerTask implements AbsNanosTimeSource, AbsMillisTimeSource {
   private final SafeTimer timer;
   private volatile long absTimeMillis;
   private volatile long relTimeNanos;
   private final long nanoOriginTimeMillis;
   private final AtomicLong lastTimeNanos;

   private static final long nanosPerMilli = 1000000;
   private static final long maxNanosDelta = 900000;

   static final long defaultPeriodMillis = 5;
   private static final String defaultTimerNameBase = "TimerDrivenAbsNanosTimeSource_";

   private static String defaultTimerName() {
      return defaultTimerNameBase + System.currentTimeMillis();
   }

   public TimerDrivenAbsNanosTimeSource(SafeTimer timer, long periodMillis, long nanoOriginTimeMillis) {
      this.timer = timer;
      lastTimeNanos = new AtomicLong();
      timer.scheduleAtFixedRate(new SafeTimerTask(this), 0, periodMillis);
      this.nanoOriginTimeMillis = nanoOriginTimeMillis;
   }

   public TimerDrivenAbsNanosTimeSource(SafeTimer timer, long nanoOriginTimeMillis) {
      this(timer, defaultPeriodMillis, nanoOriginTimeMillis);
   }

   public TimerDrivenAbsNanosTimeSource(long periodMillis, long nanoOriginTimeMillis) {
      this(new SafeTimer(defaultTimerName(), true), periodMillis, nanoOriginTimeMillis);
   }

   public TimerDrivenAbsNanosTimeSource(long nanoOriginTimeMillis) {
      this(defaultPeriodMillis, nanoOriginTimeMillis);
   }

   public void stop() {
      timer.cancel();
   }

   @Override
   public long absTimeMillis() {
      return absTimeMillis;
   }

   @Override
   public int relMillisRemaining(long absDeadlineMillis) {
      return TimeSourceUtil.relTimeRemainingAsInt(absDeadlineMillis, absTimeMillis());
   }

   @Override
   public void run() {
      relTimeNanos = System.nanoTime();
      absTimeMillis = System.currentTimeMillis();
   }

   @Override
   public long getNanosOriginTime() {
      return 0;
   }

   @Override
   public long absTimeNanos() {
      long prev;
      long nanosDelta;
      long absTimeNanos;

      nanosDelta = System.nanoTime() - relTimeNanos; // hint
      if (nanosDelta > maxNanosDelta || nanosDelta < 0) {
         nanosDelta = 0;
      }
      absTimeNanos = (absTimeMillis - nanoOriginTimeMillis) * nanosPerMilli + nanosDelta;
      prev = lastTimeNanos.getAndUpdate(x -> x < absTimeNanos ? absTimeNanos : x + 1);
      return absTimeNanos > prev ? absTimeNanos : prev + 1;
   }

   @Override
   public long relNanosRemaining(long absDeadlineNanos) {
      return TimeSourceUtil.relTimeRemainingAsInt(absDeadlineNanos, absTimeNanos());
   }
}
