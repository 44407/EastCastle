package eastcastle.time;

/**
 * Provides relative time in nanoseconds.
 */
public interface RelNanosTimeSource {
   /**
    * @return a relative time in nanoseconds
    */
   public long relTimeNanos();
}
