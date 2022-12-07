package eastcastle.time;

/**
 * RelNanosAbsMillisTimeSource implemented with standard System class calls.
 */
public final class SystemTimeSource implements RelNanosAbsMillisTimeSource, AbsNanosTimeSource {
   private final TimerDrivenAbsNanosTimeSource absNanosTimeSource;

   // This instance is only intended for use by other time package utilities.
   // Making this generally available could lead to instances where different
   // nanosecond origins are used.
   static final SystemTimeSource instance = new SystemTimeSource();

   private SystemTimeSource(long nanoOriginInMillis) {
      absNanosTimeSource = new TimerDrivenAbsNanosTimeSource(nanoOriginInMillis);
   }

   /**
    * Create a SystemTimeSource with a nanos origin of now. <b>Be aware that this
    * origin will change every time a new instance is created.</b>
    */
   public SystemTimeSource() {
      this(System.currentTimeMillis());
   }

   public static SystemTimeSource createWithMillisOrigin(long nanoOriginInMillis) {
      return new SystemTimeSource(nanoOriginInMillis);
   }

   @Override
   public long relTimeNanos() {
      return System.nanoTime();
   }

   @Override
   public long absTimeMillis() {
      return System.currentTimeMillis();
   }

   @Override
   public int relMillisRemaining(long absDeadlineMillis) {
      return TimeSourceUtil.relTimeRemainingAsInt(absDeadlineMillis, absTimeMillis());
   }

   @Override
   public long absTimeNanos() {
      return absNanosTimeSource.absTimeNanos();
   }

   @Override
   public long getNanosOriginTime() {
      return absNanosTimeSource.getNanosOriginTime();
   }

   @Override
   public long relNanosRemaining(long absDeadlineNanos) {
      return absNanosTimeSource.relNanosRemaining(absDeadlineNanos);
   }

   @Override
   public int hashCode() {
      return absNanosTimeSource.hashCode();
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }

      if (this.getClass() != o.getClass()) {
         return false;
      }

      SystemTimeSource other = (SystemTimeSource) o;

      return absNanosTimeSource.equals(other.absNanosTimeSource);
   }
}
