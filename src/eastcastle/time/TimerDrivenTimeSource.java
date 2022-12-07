package eastcastle.time;

import java.util.TimerTask;

import eastcastle.util.SafeTimer;
import eastcastle.util.SafeTimerTask;

/**
 * A RelNanosAbsMillisTimeSource that utilizes a Timer class to provide very
 * low-overhead time reads. This timer is faster than SystemTimeSource, but is
 * less accurate and more granular.
 */
public final class TimerDrivenTimeSource extends TimerTask implements RelNanosAbsMillisTimeSource {
   private final SafeTimer timer;
   private volatile long absTimeMillis;
   private volatile long relTimeNanos;

   static final long defaultPeriodMillis = 5;
   private static final String defaultTimerNameBase = "TimerDriveTimeSource_";

   private static String defaultTimerName() {
      return defaultTimerNameBase + System.currentTimeMillis();
   }

   public TimerDrivenTimeSource(SafeTimer timer, long periodMillis) {
      updateTimes();
      this.timer = timer;
      timer.scheduleAtFixedRate(new SafeTimerTask(this), 0, periodMillis);
   }

   public TimerDrivenTimeSource(SafeTimer timer) {
      this(timer, defaultPeriodMillis);
   }

   public TimerDrivenTimeSource(long periodMillis) {
      this(new SafeTimer(defaultTimerName(), true), periodMillis);
   }

   public TimerDrivenTimeSource() {
      this(defaultPeriodMillis);
   }

   public void stop() {
      timer.cancel();
   }

   @Override
   public long relTimeNanos() {
      return relTimeNanos;
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
      updateTimes();
   }

   private void updateTimes() {
      relTimeNanos = System.nanoTime();
      absTimeMillis = System.currentTimeMillis();
   }
}
