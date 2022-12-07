package eastcastle.util.sort.analysis;

public final class RandomDistribution extends BaseDistribution {
   public static final RandomDistribution instance = new RandomDistribution();
   
   protected RandomDistribution() {
   }
   
   protected RandomDistribution(String...knownParameters) {
      super(knownParameters);
   }
}
