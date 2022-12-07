package eastcastle.util.sort.analysis;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import eastcastle.util.ArrayUtil;

public class PartiallySortedDistribution extends ReversibleDistribution {
   private final int sortedPercentage;
   
   private static final String   sortedPercentageParameter = "sortedPercentage";
   
   public PartiallySortedDistribution(TestSpecification testSpec) {
      super(testSpec);
      knownParameters.add(sortedPercentageParameter);
      sortedPercentage = testSpec.getIntParameter(sortedPercentageParameter);
   }
   
   @Override
   public void fill(int[] a, int i0, int i1, Map<String, String> parameters) {
      int   aLength;
      int   sortedLength;
      int   sortedIndex;
      
      super.fill(a, i0, i1, parameters);
      aLength = i1 - i0;
      sortedLength = aLength * sortedPercentage / 100;
      if (sortedPercentage < 100) {
         sortedIndex = ThreadLocalRandom.current().nextInt(0, aLength - sortedLength);
      } else {
         sortedIndex = 0;
      }
      Arrays.sort(a, sortedIndex, sortedIndex + sortedLength);
      if (reverse) {
         ArrayUtil.reverse(a, sortedIndex, sortedIndex + sortedLength);
      }
   }

   @Override
   public void fill(long[] a, int i0, int i1, Map<String, String> parameters) {
      int   aLength;
      int   sortedLength;
      int   sortedIndex;
      
      super.fill(a, i0, i1, parameters);
      aLength = i1 - i0;
      sortedLength = aLength * sortedPercentage / 100;
      if (sortedPercentage < 100) {
         sortedIndex = ThreadLocalRandom.current().nextInt(0, aLength - sortedLength);
      } else {
         sortedIndex = 0;
      }
      Arrays.sort(a, sortedIndex, sortedIndex + sortedLength);
      if (reverse) {
         ArrayUtil.reverse(a, sortedIndex, sortedIndex + sortedLength);
      }
   }

   @Override
   public void fill(double[] a, int i0, int i1, Map<String, String> parameters) {
      int   aLength;
      int   sortedLength;
      int   sortedIndex;
      
      super.fill(a, i0, i1, parameters);
      aLength = i1 - i0;
      sortedLength = aLength * sortedPercentage / 100;
      if (sortedPercentage < 100) {
         sortedIndex = ThreadLocalRandom.current().nextInt(0, aLength - sortedLength);
      } else {
         sortedIndex = 0;
      }
      Arrays.sort(a, sortedIndex, sortedIndex + sortedLength);
      if (reverse) {
         ArrayUtil.reverse(a, sortedIndex, sortedIndex + sortedLength);
      }
   }
   
   @Override
   public void fill(String[] a, int i0, int i1, Map<String, String> parameters) {
      int   aLength;
      int   sortedLength;
      int   sortedIndex;
      
      super.fill(a, i0, i1, parameters);
      aLength = i1 - i0;
      sortedLength = aLength * sortedPercentage / 100;
      if (sortedPercentage < 100) {
         sortedIndex = ThreadLocalRandom.current().nextInt(0, aLength - sortedLength);
      } else {
         sortedIndex = 0;
      }
      Arrays.sort(a, sortedIndex, sortedIndex + sortedLength);
      if (reverse) {
         ArrayUtil.reverse(a, sortedIndex, sortedIndex + sortedLength);
      }
   }
}
