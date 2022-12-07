package eastcastle.util.sort;

import java.util.List;

class SortAnalyzer {
   private static final int   maxRuns =  12;
   
   public static SortAnalysis analyze(double[] a, int i0, int i1) {
      SortAnalysis   sortAnalysis;
      double   prev;
      int      runIndex;
      boolean  ascending;
      boolean  initiallyAscending;
      int      valuesInRuns;
      
      int   numRuns = 0;
      Order[]  runOrder = new Order[maxRuns];
      int[]   runI0 = new int[maxRuns];
      int[]   runI1 = new int[maxRuns];
      
      valuesInRuns = 0;
      runIndex = i0;
      prev = a[i0];
      ascending = prev <= a[i0 + 1];
      initiallyAscending = ascending;
      
      int i;
      for (i = i0 + 1; i < i1; i++) {
         double  cur;
         
         cur = a[i];
         if (ascending) {
            if (prev > cur) {
               runOrder[numRuns] = Order.ASCENDING;
               runI0[numRuns] = runIndex;
               runI1[numRuns] = i;
               valuesInRuns += i - runIndex;
               runIndex = i;
               ascending = (i == i1 - 1) || (a[i] < a[i + 1]);
               if (++numRuns >= maxRuns - 1) {
                  runIndex = i1;
                  break;
               } 
            } else {
            }
         } else {
            if (prev < cur) {
               runOrder[numRuns] = Order.DESCENDING;
               runI0[numRuns] = runIndex;
               runI1[numRuns] = i;
               valuesInRuns += i - runIndex;
               runIndex = i;
               ascending = (i == i1 - 1) || (a[i] < a[i + 1]);
               if (++numRuns >= maxRuns - 1) {
                  runIndex = i1;
                  break;
               } 
            } else {
            }
         }
         prev = cur;            
      }
      if (runIndex < i1) {
         runOrder[numRuns] = ascending ? Order.ASCENDING : Order.DESCENDING;
         runI0[numRuns] = runIndex;
         runI1[numRuns] = i1;
         ++numRuns;
         valuesInRuns += i1 - runIndex;
      }
      
      for (; i < i1; i++) {
         double  cur;
         
         cur = a[i];
         prev = cur;            
      }
      
      sortAnalysis = new SortAnalysis(initiallyAscending ? Order.ASCENDING : Order.DESCENDING, i1 - i0, numRuns, runOrder, runI0, runI1);
      
      sortAnalysis.setValuesInRuns(valuesInRuns);
      return sortAnalysis;
   }
   
   public static SortAnalysis analyze(int[] a, int i0, int i1) {
      SortAnalysis   sortAnalysis;
      int     prev;
      int      runIndex;
      int     bitMin;
      int     bitMax;
      int     bitDelta;
      boolean  ascending;
      boolean  initiallyAscending;
      int      valuesInRuns;
      
      int   numRuns = 0;
      Order[]  runOrder = new Order[maxRuns];
      int[]   runI0 = new int[maxRuns];
      int[]   runI1 = new int[maxRuns];
      
      valuesInRuns = 0;
      runIndex = i0;
      prev = a[i0];
      bitMax = prev;
      bitMin = 0xffffffff & prev;
      ascending = prev <= a[i0 + 1];
      initiallyAscending = ascending;
      
      int i;
      for (i = i0 + 1; i < i1; i++) {
         int  cur;
         
         cur = a[i];
         bitMax |= cur;
         bitMin &= cur;
         if (ascending) {
            if (prev > cur) {
               runOrder[numRuns] = Order.ASCENDING;
               runI0[numRuns] = runIndex;
               runI1[numRuns] = i;
               valuesInRuns += i - runIndex;
               runIndex = i;
               ascending = (i == i1 - 1) || (a[i] < a[i + 1]);
               if (++numRuns >= maxRuns - 1) {
                  runIndex = i1;
                  break;
               } 
            } else {
            }
         } else {
            if (prev < cur) {
               runOrder[numRuns] = Order.DESCENDING;
               runI0[numRuns] = runIndex;
               runI1[numRuns] = i;
               valuesInRuns += i - runIndex;
               runIndex = i;
               ascending = (i == i1 - 1) || (a[i] < a[i + 1]);
               if (++numRuns >= maxRuns - 1) {
                  runIndex = i1;
                  break;
               } 
            } else {
            }
         }
         prev = cur;            
      }
      if (runIndex < i1 - 1) {
         runOrder[numRuns] = ascending ? Order.ASCENDING : Order.DESCENDING;
         runI0[numRuns] = runIndex;
         runI1[numRuns] = i1;
         ++numRuns;
         valuesInRuns += i1 - runIndex;
      }
      
      for (; i < i1; i++) {
         int  cur;
         
         cur = a[i];
         bitMax |= cur;
         bitMin &= cur;
         prev = cur;            
      }
      sortAnalysis = new SortAnalysis(initiallyAscending ? Order.ASCENDING : Order.DESCENDING, i1 - i0, numRuns, runOrder, runI0, runI1);
      
      bitDelta = bitMin ^ bitMax;      
      sortAnalysis.setBitDelta(bitDelta);
      sortAnalysis.setValuesInRuns(valuesInRuns);
      return sortAnalysis;
   }
    
   public static SortAnalysis analyze(long[] a, int i0, int i1) {
      SortAnalysis   sortAnalysis;
      long     prev;
      int      runIndex;
      long     bitMin;
      long     bitMax;
      long     bitDelta;
      boolean  ascending;
      boolean  initiallyAscending;
      int      valuesInRuns;
      
      int   numRuns = 0;
      Order[]  runOrder = new Order[maxRuns];
      int[]   runI0 = new int[maxRuns];
      int[]   runI1 = new int[maxRuns];
      
      valuesInRuns = 0;
      runIndex = i0;
      prev = a[i0];
      bitMax = prev;
      bitMin = 0xffffffffffffffffL & prev;
      ascending = prev <= a[i0 + 1];
      initiallyAscending = ascending;
      
      int i;
      for (i = i0 + 1; i < i1; i++) {
         long  cur;
         
         cur = a[i];
         bitMax |= cur;
         bitMin &= cur;
         if (ascending) {
            if (prev > cur) {
               runOrder[numRuns] = Order.ASCENDING;
               runI0[numRuns] = runIndex;
               runI1[numRuns] = i;
               valuesInRuns += i - runIndex;
               runIndex = i;
               ascending = (i == i1 - 1) || (a[i] < a[i + 1]);
               if (++numRuns >= maxRuns - 1) {
                  runIndex = i1;
                  break;
               } 
            } else {
            }
         } else {
            if (prev < cur) {
               runOrder[numRuns] = Order.DESCENDING;
               runI0[numRuns] = runIndex;
               runI1[numRuns] = i;
               valuesInRuns += i - runIndex;
               runIndex = i;
               ascending = (i == i1 - 1) || (a[i] < a[i + 1]);
               if (++numRuns >= maxRuns - 1) {
                  runIndex = i1;
                  break;
               } 
            } else {
            }
         }
         prev = cur;            
      }
      if (runIndex < i1) {
         runOrder[numRuns] = ascending ? Order.ASCENDING : Order.DESCENDING;
         runI0[numRuns] = runIndex;
         runI1[numRuns] = i1;
         ++numRuns;
         valuesInRuns += i1 - runIndex;
      }
      
      for (; i < i1; i++) {
         long  cur;
         
         cur = a[i];
         bitMax |= cur;
         bitMin &= cur;
         prev = cur;            
      }

      sortAnalysis = new SortAnalysis(initiallyAscending ? Order.ASCENDING : Order.DESCENDING, i1 - i0, numRuns, runOrder, runI0, runI1);
      
      bitDelta = bitMin ^ bitMax;      
      sortAnalysis.setBitDelta(bitDelta);
      sortAnalysis.setValuesInRuns(valuesInRuns);
      return sortAnalysis;
   }
   
  public static <T> SortAnalysis analyze(T[] a, int i0, int i1, SortIndexer<T> sortIndexer) {
      SortAnalysis   sortAnalysis;
      T        prev;
      int      runIndex;
      int     bitMin;
      int     bitMax;
      int     bitDelta;
      boolean  ascending;
      boolean  initiallyAscending;
      int      valuesInRuns;
      int      prevIndex;
      
      int   numRuns = 0;
      Order[]  runOrder = new Order[maxRuns];
      int[]   runI0 = new int[maxRuns];
      int[]   runI1 = new int[maxRuns];
      
      valuesInRuns = 0;
      runIndex = i0;
      prev = a[i0];
      prevIndex = sortIndexer.sortIndex(prev);
      bitMax = prevIndex;
      bitMin = 0xffffffff & prevIndex;
      ascending = sortIndexer.compare(prev, a[i0 + 1]) <= 0;
      initiallyAscending = ascending;
      
      int i;
      for (i = i0 + 1; i < i1; i++) {
         T  cur;
         int   curIndex;
         
         cur = a[i];
         curIndex = sortIndexer.sortIndex(cur);
         bitMax |= curIndex;
         bitMin &= curIndex;
         if (ascending) {
            if (sortIndexer.compare(prev, cur) > 0) {
               runOrder[numRuns] = Order.ASCENDING;
               runI0[numRuns] = runIndex;
               runI1[numRuns] = i;
               valuesInRuns += i - runIndex;
               runIndex = i;
               ascending = (i == i1 - 1) || (sortIndexer.compare(a[i], a[i + 1]) < 0);
               if (++numRuns >= maxRuns - 1) {
                  runIndex = i1;
                  break;
               } 
            } else {
            }
         } else {
            if (sortIndexer.compare(prev, cur) < 0) {
               runOrder[numRuns] = Order.DESCENDING;
               runI0[numRuns] = runIndex;
               runI1[numRuns] = i;
               valuesInRuns += i - runIndex;
               runIndex = i;
               ascending = (i == i1 - 1) || (sortIndexer.compare(a[i], a[i + 1]) < 0);
               if (++numRuns >= maxRuns - 1) {
                  runIndex = i1;
                  break;
               } 
            } else {
            }
         }
         prev = cur;            
      }
      if (runIndex < i1 - 1) {
         runOrder[numRuns] = ascending ? Order.ASCENDING : Order.DESCENDING;
         runI0[numRuns] = runIndex;
         runI1[numRuns] = i1;
         ++numRuns;
         valuesInRuns += i1 - runIndex;
      }
      
      for (; i < i1; i++) {
         T  cur;
         int   curIndex;
         
         cur = a[i];
         curIndex = sortIndexer.sortIndex(cur);
         bitMax |= curIndex;
         bitMin &= curIndex;
         prev = cur;            
      }
      
      sortAnalysis = new SortAnalysis(initiallyAscending ? Order.ASCENDING : Order.DESCENDING, i1 - i0, numRuns, runOrder, runI0, runI1);
      
      bitDelta = bitMin ^ bitMax;      
      sortAnalysis.setBitDelta(bitDelta);
      sortAnalysis.setValuesInRuns(valuesInRuns);
      return sortAnalysis;
   }
   
   public static <T extends Comparable<T>> SortAnalysis analyze(List<T> l, int i0, int i1) {
      SortAnalysis   sortAnalysis;
      T        prev;
      int      runIndex;
      boolean  ascending;
      boolean  initiallyAscending;
      int      valuesInRuns;
      
      int   numRuns = 0;
      Order[]  runOrder = new Order[maxRuns];
      int[]   runI0 = new int[maxRuns];
      int[]   runI1 = new int[maxRuns];
      
      valuesInRuns = 0;
      runIndex = i0;
      prev = l.get(i0);
      ascending = prev.compareTo(l.get(i0 + 1)) <= 0;
      initiallyAscending = ascending;
      
      for (int i = i0 + 1; i < i1; i++) {
         T  cur;
         
         cur = l.get(i);
         if (ascending) {
            if (prev.compareTo(cur) > 0) {
               runOrder[numRuns] = Order.ASCENDING;
               runI0[numRuns] = runIndex;
               runI1[numRuns] = i;
               valuesInRuns += i - runIndex;
               runIndex = i;
               ascending = (i == i1 - 1) || (l.get(i).compareTo(l.get(i + 1)) < 0);
               if (++numRuns >= maxRuns - 1) {
                  runIndex = i1;
                  break;
               } 
            } else {
            }
         } else {
            if (prev.compareTo(cur) < 0) {
               runOrder[numRuns] = Order.DESCENDING;
               runI0[numRuns] = runIndex;
               runI1[numRuns] = i;
               valuesInRuns += i - runIndex;
               runIndex = i;
               ascending = (i == i1 - 1) || (l.get(i).compareTo(l.get(i + 1)) < 0);
               if (++numRuns >= maxRuns - 1) {
                  runIndex = i1;
                  break;
               } 
            } else {
            }
         }
         prev = cur;            
      }
      if (runIndex < i1 - 1) {
         runOrder[numRuns] = ascending ? Order.ASCENDING : Order.DESCENDING;
         runI0[numRuns] = runIndex;
         runI1[numRuns] = i1;
         ++numRuns;
         valuesInRuns += i1 - runIndex;
      }
      
      sortAnalysis = new SortAnalysis(initiallyAscending ? Order.ASCENDING : Order.DESCENDING, i1 - i0, numRuns, runOrder, runI0, runI1);
      
      sortAnalysis.setValuesInRuns(valuesInRuns);
      return sortAnalysis;
   }   
}
