package eastcastle.util.sort.analysis;

public abstract class ReversibleDistribution extends BaseDistribution {
   protected final boolean   reverse;
   
   private static final String   reverseParameter = "reverse";
   
   public ReversibleDistribution(TestSpecification testSpec) {
      super(reverseParameter);
      this.reverse = testSpec.getBooleanParameter(reverseParameter);
   }
}
