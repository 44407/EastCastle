package eastcastle.util.sort.analysis;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import eastcastle.util.ArrayUtil;

public class NSwappedDistribution extends ReversibleDistribution {
   private final int numSwapped;
   
   private static final String   numSwappedParameter = "numSwapped";
   
   public NSwappedDistribution(TestSpecification testSpec) {
      super(testSpec);
      knownParameters.add(numSwappedParameter);
      numSwapped = testSpec.getIntParameter(numSwappedParameter);
   }
   
   @Override
   public void fill(int[] a, int i0, int i1, Map<String, String> parameters) {
      super.fill(a, i0, i1, parameters);
      Arrays.sort(a, i0, i1);
      if (reverse) {
         ArrayUtil.reverse(a, i0, i1);
      }
      for (int i = 0; i < numSwapped; i++) {
         int   tmp;
         int   j0;
         int   j1;
         
         j0 = ThreadLocalRandom.current().nextInt(i0, i1);
         j1 = ThreadLocalRandom.current().nextInt(i0, i1);
         tmp = a[j0];
         a[j0] = a[j1];
         a[j1] = tmp;
      }
   }

   @Override
   public void fill(long[] a, int i0, int i1, Map<String, String> parameters) {
      super.fill(a, i0, i1, parameters);
      Arrays.sort(a, i0, i1);
      if (reverse) {
         ArrayUtil.reverse(a, i0, i1);
      }
      for (int i = 0; i < numSwapped; i++) {
         long   tmp;
         int   j0;
         int   j1;
         
         j0 = ThreadLocalRandom.current().nextInt(i0, i1);
         j1 = ThreadLocalRandom.current().nextInt(i0, i1);
         tmp = a[j0];
         a[j0] = a[j1];
         a[j1] = tmp;
      }
   }

   @Override
   public void fill(double[] a, int i0, int i1, Map<String, String> parameters) {
      super.fill(a, i0, i1, parameters);
      Arrays.sort(a, i0, i1);
      if (reverse) {
         ArrayUtil.reverse(a, i0, i1);
      }
      for (int i = 0; i < numSwapped; i++) {
         double   tmp;
         int   j0;
         int   j1;
         
         j0 = ThreadLocalRandom.current().nextInt(i0, i1);
         j1 = ThreadLocalRandom.current().nextInt(i0, i1);
         tmp = a[j0];
         a[j0] = a[j1];
         a[j1] = tmp;
      }
   }
   
   @Override
   public void fill(String[] a, int i0, int i1, Map<String, String> parameters) {
      super.fill(a, i0, i1, parameters);
      Arrays.sort(a, i0, i1);
      if (reverse) {
         ArrayUtil.reverse(a, i0, i1);
      }
      for (int i = 0; i < numSwapped; i++) {
         String   tmp;
         int   j0;
         int   j1;
         
         j0 = ThreadLocalRandom.current().nextInt(i0, i1);
         j1 = ThreadLocalRandom.current().nextInt(i0, i1);
         tmp = a[j0];
         a[j0] = a[j1];
         a[j1] = tmp;
      }
   }
}
