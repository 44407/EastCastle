package eastcastle.util.sort.analysis;

import java.util.Arrays;
import java.util.Map;

import eastcastle.util.ArrayUtil;

public class SortedRangesDistribution extends ReversibleDistribution {
   private final int numRuns;
   private final int gap;
   
   private static final String   runLengthParameter = "runLength";
   private static final String   numRunsParameter = "numRuns";
   private static final String   gapParameter = "gap";
   
   public SortedRangesDistribution(TestSpecification testSpec) {
      super(testSpec);
      int   runLength;
      int   _numRuns;
      
      knownParameters.add(numRunsParameter);
      knownParameters.add(gapParameter);
      knownParameters.add(runLengthParameter);
      runLength = testSpec.getIntParameter(runLengthParameter, false, 0);
      _numRuns = testSpec.getIntParameter(numRunsParameter, false, 0);      
      if (runLength != 0 && _numRuns != 0) {
         throw new RuntimeException("Only one of runLength and numRuns can be specified");
      } else {
         if (runLength == 0) {
            numRuns = _numRuns;
         } else {
            _numRuns = testSpec.getSize() / runLength;
            if ((testSpec.getSize() % runLength) != 0) {
               ++_numRuns;
            }
            numRuns = _numRuns;
         }
      }
      gap = testSpec.getIntParameter(gapParameter, false, 0);
   }
   
   @Override
   public void fill(int[] a, int i0, int i1, Map<String, String> parameters) {
      int   aLength;
      int   runLength;
      int   j0;
      
      super.fill(a, i0, i1, parameters);
      aLength = i1 - i0;
      runLength = aLength / numRuns;
      if (runLength == 0) {
         runLength = aLength;
      }
      j0 = i0;
      while (j0 < i1) {
         int   j1;
         
         if (j0 + runLength < i1 - 1) {
            j1 = j0 + runLength;
         } else {
            j1 = i1;
         }
         Arrays.sort(a, j0, j1 - gap);
         if (reverse) {
            ArrayUtil.reverse(a, j0, j1);
         }
         j0 += runLength;
      }
   }

   @Override
   public void fill(long[] a, int i0, int i1, Map<String, String> parameters) {
      int   aLength;
      int   runLength;
      int   j0;
      
      super.fill(a, i0, i1, parameters);
      aLength = i1 - i0;
      runLength = aLength / numRuns;
      if (runLength == 0) {
         runLength = aLength;
      }
      j0 = i0;
      while (j0 < i1) {
         int   j1;
         
         if (j0 + runLength < i1 - 1) {
            j1 = j0 + runLength;
         } else {
            j1 = i1;
         }
         Arrays.sort(a, j0, j1 - gap);
         if (reverse) {
            ArrayUtil.reverse(a, j0, j1);
         }
         j0 += runLength;
      }
   }

   @Override
   public void fill(double[] a, int i0, int i1, Map<String, String> parameters) {
      int   aLength;
      int   runLength;
      int   j0;
      
      super.fill(a, i0, i1, parameters);
      aLength = i1 - i0;
      runLength = aLength / numRuns;
      if (runLength == 0) {
         runLength = aLength;
      }
      j0 = i0;
      while (j0 < i1) {
         int   j1;
         
         if (j0 + runLength < i1 - 1) {
            j1 = j0 + runLength;
         } else {
            j1 = i1;
         }
         Arrays.sort(a, j0, j1 - gap);
         if (reverse) {
            ArrayUtil.reverse(a, j0, j1);
         }
         j0 += runLength;
      }
   }
   
   @Override
   public void fill(String[] a, int i0, int i1, Map<String, String> parameters) {
      int   aLength;
      int   runLength;
      int   j0;
      
      super.fill(a, i0, i1, parameters);
      aLength = i1 - i0;
      runLength = aLength / numRuns;
      if (runLength == 0) {
         runLength = aLength;
      }
      j0 = i0;
      while (j0 < i1) {
         int   j1;
         
         if (j0 + runLength < i1 - 1) {
            j1 = j0 + runLength;
         } else {
            j1 = i1;
         }
         Arrays.sort(a, j0, j1 - gap);
         if (reverse) {
            ArrayUtil.reverse(a, j0, j1);
         }
         j0 += runLength;
      }
   }
}
