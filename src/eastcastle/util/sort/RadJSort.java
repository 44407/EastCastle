package eastcastle.util.sort;

import java.util.Arrays;
import java.util.Comparator;

import eastcastle.util.ArrayUtil;
import eastcastle.util.NumConversion;
import eastcastle.util.ThreadLocalStorage;

/**
 * Extended radix sort implementation capable of sorting classes that are not naturally radix-sortable. 
 * Uses a hybrid mix of MSB radix sorting, LSB radix prefix sorting, merges, and the underlying system sort.
 */
public class RadJSort extends BaseIndexSort {
   /*
    * This class contains two distinct radix sorting implementations:
    * 1) MSB radix sorting.
    * 2) LSB radix prefix sorting. This implementation sorts elements based on a prefix, and then uses system sort 
    *    to finish off the sort. The idea here is that radix sort can quickly move elements close to where they need be
    *    (using the prefix), and then the system sort - which has good locality - can finish the sort.
    * 
    * Pure LSB radix sorting was previously implemented, but was observed to be inferior to MSB radix sorting - hence it was removed.
    */

   /*
    * FUTURE - Complete primitive types after everything is stable for the currently implemented types
    */
   
   private static final String   name = "RadJ";
   
   // FUTURE - TypeParameters configurable
   private static final TypeParameters intParameters = new TypeParameters(128, 10);
   private static final TypeParameters longParameters = new TypeParameters(128, 10); 
   private static final TypeParameters doubleParameters = new TypeParameters(128, 12);
   private static final TypeParameters comparableParameters = new TypeParameters(48, 2); 

   private static final int   COUNT_SIZE = 256;
   private static final int   LONG_BUCKETS = NumConversion.BYTES_PER_LONG;
   private static final int   INT_BUCKETS = NumConversion.BYTES_PER_INT;
   private static final int   BYTE_MIN_VALUE_UNSIGNED = 128;   
   private static final int   MSB_MIN_SIZE = 128;

   private static final int   negativeSearchLinearThreshold = 8;   
   
   /**
    * RadJSort default singleton
    */
   public static final RadJSort   defaultInstance = new RadJSort();
   
   private static final boolean  debug = false;
   
   private enum Signedness {
      Signed, 
      Unsigned;
      
      boolean isSigned() {
         return this == Signed;
      }
   };
   
   private static class TypeParameters {
      /** for array/list length < this threshold, use system sort */
      final int  systemSortLengthThreshold;
      /** for num runs <= this threshold, merge runs */
      final int  mergeRunThreshold;
      
      TypeParameters(int systemSortLengthThreshold, int mergeRunThreshold) {
         this.systemSortLengthThreshold = systemSortLengthThreshold;
         this.mergeRunThreshold = mergeRunThreshold;
      }
   }
   
   /////////////////
   // Constructors
   
   /**
    * Construct an instance with the given SortIndexerProvider
    */
   public RadJSort(SortIndexerProvider sortIndexerProvider) {
      super(name, sortIndexerProvider);
   }

   /**
    * Construct an instance with the DefaultSortIndexerProvider.
    * For internal use only. External users should use the singleton for this case.
    */
   private RadJSort() {
      this(DefaultSortIndexerProvider.instance);
   }

   ///////////////////////////
   // Prefix sorting support 
   
   /*
    * Prefix sorting stores an index and value in a long as follows:
    *  +-----------+
    *  |index|value|
    *  +-----------+
    *    32    32
    *  
    * The index is the array or List index to the element to be sorted.
    * The value is the prefix of the value to be sorted.
    */

   /**
    * Given a long, extract the index
    */
   private static int indexFromLong(long l) {
      return (int)(l >>> 32);
   }

   /**
    * Given a long, extract the value
    */
   private static int valueFromLong(long l) {
      return (int)l;
   }

   /**
    * Compute prefix sorting longs - in bulk - for an array (or array portion) of objects to be sorted.
    * @param <T>
    * @param a
    * @param i0
    * @param i1
    * @param sortIndexer
    * @return
    */
   private static <T> long[] objectArrayToLongArray(T[] a, int i0, int i1, SortIndexer<T> sortIndexer) {
      long[] _a;

      _a = new long[i1 - i0];
      for (int i = 0; i < _a.length; i++) {
         _a[i] = (sortIndexer.sortIndex(a[i0 + i]) & 0xffffffffL) | ((long)i << 32);
      }
      return _a;
   }
   
   /**
    * Compute to prefix sorting longs in bulk for an array (or array portion) of doubles to be sorted.
    * @param <T>
    * @param a
    * @param i0
    * @param i1
    * @param sorter
    * @return
    */
   private static long[] doubleArrayToLongArrayPrefix_IEEE754(double[] a, int i0, int i1) {
      long[] _a;

      _a = new long[i1 - i0];
      for (int i = 0; i < _a.length; i++) {
         _a[i] = (Double.doubleToRawLongBits(a[i0 + i]) >>> 32) | ((long)i << 32);
      }
      return _a;
   }
   
   /**
    * Convert an array of double[] to an array of long[].
    * The returned array will be of length i1 - i0
    * @param a
    * @param i0
    * @param i1
    * @return
    */
   private static long[] doubleArrayToLongArray_IEEE754(double[] a, int i0, int i1) {
      long[] _a;

      _a = new long[i1 - i0];
      for (int i = 0; i < _a.length; i++) {
         _a[i] = Double.doubleToRawLongBits(a[i0 + i]);
      }
      return _a;
   }
   
   /**
    * Prefix sorting permutation.
    * Permutes a into b using the directives in s
    * @param <T>
    * @param a
    * @param b
    * @param s
    */
   private <T> void permute(T[] a, T[] b, long[] s) {
      int prev;
      int i0;
      int i1;

      prev = -1;
      i0 = -1;
      i1 = -1;
      for (int i = 0; i < s.length; i++) {
         int index;
         int value;

         index = indexFromLong(s[i]);
         value = valueFromLong(s[i]);
         if (debug) {
            System.out.printf("%d => %d\t%x\t\t%s\n", i, index, s[i], a[i]);
         }
         b[i] = a[index];

         if (i0 < 0) {
            if (prev == value) {
               i0 = i - 1;
            }
         } else {
            if (prev != value) {
               i1 = i;
               Arrays.sort(b, i0, i1);
               i0 = -1;
               i1 = -1;
            }
         }
         
         prev = value;
      }
      if (i0 >= 0) {
         Arrays.sort(b, i0, s.length);
      }
   }

   /**
    * Prefix sorting permutation.
    * Permutes a into b using the directives in s
    * @param <T>
    * @param a
    * @param b
    * @param s
    */
   private <T> void permute(double[] a, double[] b, long[] s) {
      int prev;
      int i0;
      int i1;

      prev = -1;
      i0 = -1;
      i1 = -1;
      for (int i = 0; i < s.length; i++) {
         int index;
         int value;

         index = indexFromLong(s[i]);
         value = valueFromLong(s[i]);
         if (debug) {
            System.out.printf("%d => %d\t%x\t\t%s\n", i, index, s[i], a[i]);
         }
         b[i] = a[index];

         if (i0 < 0) {
            if (prev == value) {
               i0 = i - 1;
            }
         } else {
            if (prev != value) {
               i1 = i;
               Arrays.sort(b, i0, i1);
               if (b[i0] < 0.0) {
                  ArrayUtil.reverse(b, i0, i1);
               }
               i0 = -1;
               i1 = -1;
            }
         }

         prev = value;
      }
      if (i0 >= 0) {
         Arrays.sort(b, i0, s.length);
         if (b[i0] < 0.0) {
            ArrayUtil.reverse(b, i0, s.length);
         }
      }
   }

   private boolean prefixIsNegative(long l) {
      return (l & 0x80000000L) != 0;
   }   
   
   /**
    * Finds first negative for a long array that is converted from IEEE754 double and stores double prefixes in the low order bytes *only*
    * and sorted using an *unsigned* sort.
    * In this array, negatives come *after* positives
    * @param a
    * @param i0
    * @param i1
    * @return
    */
   private int findFirstNegative_SortedDoublePrefix(long[] a, int i0, int i1) {
      int   size;
      
      size = i1 - i0;
      if (size < negativeSearchLinearThreshold) {
         for (int i = 0; i < size; i++) {
            if (prefixIsNegative(a[i0 + i])) {
               return i0 + i;
            }
         }
         return -1;
      } else {
         int   i_mid;
         
         i_mid = (i0 + i1) / 2;
         if (prefixIsNegative(a[i_mid])) {
            return findFirstNegative_SortedDoublePrefix(a, i0, i_mid + 1);
         } else {
            return findFirstNegative_SortedDoublePrefix(a, i_mid + 1, i1);
         }
      }
   }
   
   
   /**
    * Finds first negative for a long array that is converted from IEEE754 double.
    * In this array, negatives come *after* positives
    * @param a
    * @param i0
    * @param i1
    * @return
    */
   private int findFirstNegative_SortedDouble(long[] a, int i0, int i1) {
      int   size;
      
      size = i1 - i0;
      if (size < negativeSearchLinearThreshold) {
         for (int i = 0; i < size; i++) {
            if (a[i0 + i] < 0) {
               return i0 + i;
            }
         }
         return -1;
      } else {
         int   i_mid;
         
         i_mid = (i0 + i1) / 2;
         if (a[i_mid] < 0) {
            return findFirstNegative_SortedDouble(a, i0, i_mid + 1);
         } else {
            return findFirstNegative_SortedDouble(a, i_mid + 1, i1);
         }
      }
   }
   
   public int computeBytesToSortMask(long bitDelta, int numBuckets) {
      int   bytesToSortMask;
      
      bytesToSortMask = 0;
      for (long i = 0; i < numBuckets; i++) {
         long  mask;
         
         mask = 0xffL << (i * (long)8);
         bytesToSortMask |= ((bitDelta & mask) != 0 ? 1 : 0) << i;
      }
      return bytesToSortMask;
   }
   
   //////////////////////
   // radix sort keying

   private static int keyForIndex(int i, int byteIndex) {
      return (int) ((i >>> (byteIndex * 8)) & 0xff);
   }
   
   private static int keyForIndex(long i, int byteIndex) {
      return (int) ((i >>> (byteIndex * 8)) & 0xff);
   }
   
   ////////////////
   // radix sorts
      
   public boolean radixSort(int[] a, SortParameters sortParameters) {
      int   i0;
      int   i1;
      boolean orderHandled;

      orderHandled = false;
      i0 = sortParameters.getI0();
      i1 = sortParameters.getI1();
      if (i1 - i0 < intParameters.systemSortLengthThreshold) {
         Arrays.sort(a, i0, i1);
      } else {
         SortAnalysis   sortAnalysis;
         
         // Before using radix sort, analyze for non-radix cases
         sortAnalysis = SortAnalyzer.analyze(a, sortParameters.getI0(), sortParameters.getI1());
         if (!sortAnalysis.needsSort()) {
            if (sortAnalysis.getInitialOrder() != sortParameters.getOrder()) {
               ArrayUtil.reverse(a, i0, i1);
            }
            orderHandled = true;
         } else {
            if (sortAnalysis.allValuesInRuns() && sortAnalysis.getNumRuns() <= intParameters.mergeRunThreshold) {
               Merges.merge(a, sortParameters.getOrder(), sortAnalysis, 0, sortAnalysis.getNumRuns());
               orderHandled = true;
            } else {
               countingSort_msb(a, i0, i1, Signedness.Signed, computeBytesToSortMask(sortAnalysis.getBitDelta(), INT_BUCKETS), INT_BUCKETS - 1, 0);
            }
         }
      }
      return orderHandled;
   }   
   
   public boolean radixSort(long[] a, SortParameters sortParameters) {
      int   i0;
      int   i1;
      boolean orderHandled;

      orderHandled = false;
      i0 = sortParameters.getI0();
      i1 = sortParameters.getI1();
      if (i1 - i0 < longParameters.systemSortLengthThreshold) {
         Arrays.sort(a, i0, i1);
      } else {
         SortAnalysis   sortAnalysis;
         
         // Before using radix sort, analyze for non-radix cases
         sortAnalysis = SortAnalyzer.analyze(a, sortParameters.getI0(), sortParameters.getI1());
         if (!sortAnalysis.needsSort()) {
            if (sortAnalysis.getInitialOrder() != sortParameters.getOrder()) {
               ArrayUtil.reverse(a, i0, i1);
            }
            orderHandled = true;
         } else {
            if (sortAnalysis.allValuesInRuns() && sortAnalysis.getNumRuns() <= longParameters.mergeRunThreshold) {
               Merges.merge(a, sortParameters.getOrder(), sortAnalysis, 0, sortAnalysis.getNumRuns());
               orderHandled = true;
            } else {
               countingSort_msb(a, i0, i1, Signedness.Signed, computeBytesToSortMask(sortAnalysis.getBitDelta(), LONG_BUCKETS), LONG_BUCKETS - 1, 0);
            }
         }
      }
      return orderHandled;
   }   
   
   // not for direct use
   // Can't use this presently as the sort invoked calls Arrays.sort for small arrays
   // and Arrays.sort doesn't know about [index/value]
   private void radixSortIntPrefix_msb(long[] a, Signedness signedness, long bytesToSortMask) {
      countingSort_msb(a, 0, a.length, signedness, bytesToSortMask, INT_BUCKETS - 1, 0);    
   }
   
   private void radixSortIntPrefix(long[] a, Signedness signedness) {
      radixSortIntPrefix(a, signedness, 0x0f); // 0x0f because int prefix sorting only looks at first four bits (an int)      
   }
   
   // not for direct use
   private void radixSortIntPrefix(long[] a, Signedness signedness, long bytesToSortMask) {
      long[] b;

      b = ThreadLocalStorage.getOrCreateLongStorage(a.length);      
      countingSort_OnePass_IntBuckets(a, b, a.length, bytesToSortMask, signedness);      
   }
   
   // prefix version using SortAnalyzer 
   private boolean radixSortByPrefix(double[] a, SortParameters sortParameters) {
      int i0;
      int i1;
      boolean orderHandled;
      
      orderHandled = false;
      i0 = sortParameters.getI0();
      i1 = sortParameters.getI1();
      if (i1 - i0 < doubleParameters.systemSortLengthThreshold) {
         Arrays.sort(a, i0, i1);
      } else {
         SortAnalysis   sortAnalysis;
         
         // Before using radix sort, analyze for non-radix cases
         sortAnalysis = SortAnalyzer.analyze(a, sortParameters.getI0(), sortParameters.getI1());
         
         if (!sortAnalysis.needsSort()) {
            if (sortAnalysis.getInitialOrder() != sortParameters.getOrder()) {
               ArrayUtil.reverse(a, i0, i1);
            }
            orderHandled = true;
         } else {
            if (sortAnalysis.allValuesInRuns() && sortAnalysis.getNumRuns() <= doubleParameters.mergeRunThreshold) {
               Merges.merge(a, sortParameters.getOrder(), sortAnalysis, 0, sortAnalysis.getNumRuns());
               orderHandled = true;
            } else {
               double[] b;
               long[]   _a;
               
               b = ThreadLocalStorage.getOrCreateDoubleStorage(a.length);
               _a = doubleArrayToLongArrayPrefix_IEEE754(a, i0, i1);
               
               radixSortIntPrefix(_a, Signedness.Unsigned);
               
               permute(a, b, _a);
               
               int   firstNegative;
               int   numNegatives;
               int   j;
               
               firstNegative = findFirstNegative_SortedDoublePrefix(_a, 0, _a.length);
               if (firstNegative >= 0) {
                  numNegatives = _a.length - firstNegative;
               } else {
                  numNegatives = 0;
               }
               
               System.arraycopy(b, 0, a, i0 + numNegatives, firstNegative);
               j = 0;            
               for (int i = _a.length - 1; i >= firstNegative; --i) {
                  a[i0 + j] = b[i];
                  ++j;
               }            
            }
         }
      }
      return orderHandled;
   }
   
   // full msb version
   public boolean radixSort(double[] a, SortParameters sortParameters) {
      int   i0;
      int   i1;
      boolean orderHandled;

      orderHandled = false;
      i0 = sortParameters.getI0();
      i1 = sortParameters.getI1();
      if (i1 - i0 < doubleParameters.systemSortLengthThreshold) {
         Arrays.sort(a, i0, i1);
      } else {
         SortAnalysis   sortAnalysis;
         
         // Before using radix sort, analyze for non-radix cases
         sortAnalysis = SortAnalyzer.analyze(a, sortParameters.getI0(), sortParameters.getI1());
         if (!sortAnalysis.needsSort()) {
            if (sortAnalysis.getInitialOrder() != sortParameters.getOrder()) {
               ArrayUtil.reverse(a, i0, i1);
            }
            orderHandled = true;
         } else {
            if (sortAnalysis.allValuesInRuns() && sortAnalysis.getNumRuns() <= doubleParameters.mergeRunThreshold) {
               Merges.merge(a, sortParameters.getOrder(), sortAnalysis, 0, sortAnalysis.getNumRuns());
               orderHandled = true;
            } else {
               long[]   la;
               int   firstNegative;
               int   numNegatives;
               
               la = doubleArrayToLongArray_IEEE754(a, i0, i1);
               countingSort_msb(la, i0, i1, Signedness.Unsigned, 0xff, LONG_BUCKETS - 1, 0);
               
               firstNegative = findFirstNegative_SortedDouble(la, i0, i1);
               if (firstNegative >= 0) {
                  numNegatives = la.length - firstNegative;
               } else {
                  numNegatives = 0;
               }

               for (int i = 0; i < numNegatives; i++) {
                  a[i] = Double.longBitsToDouble(la[la.length - i - 1]);
               }
               for (int i = numNegatives; i < a.length; i++) {
                  a[i] = Double.longBitsToDouble(la[i - numNegatives]);
               }
            }
         }
      }
      return orderHandled;
   }      
   
   private <T> boolean radixSort(T[] a, SortParameters sortParameters, SortIndexer<T> sortIndexer) {
      int i0;
      int i1;
      boolean orderHandled;

      orderHandled = false;
      i0 = sortParameters.getI0();
      i1 = sortParameters.getI1();
      if (i1 - i0 < comparableParameters.systemSortLengthThreshold) {
         Arrays.sort(a, i0, i1);
      } else {
         SortAnalysis   sortAnalysis;
         
         // Before using radix sort, analyze for non-radix cases
         sortAnalysis = SortAnalyzer.analyze(a, sortParameters.getI0(), sortParameters.getI1(), sortIndexer);
         if (!sortAnalysis.needsSort()) {
            if (sortAnalysis.getInitialOrder() != sortParameters.getOrder()) {
               ArrayUtil.reverse(a, i0, i1);
            }
            orderHandled = true;
         } else {
            if (sortAnalysis.allValuesInRuns() && sortAnalysis.getNumRuns() <= comparableParameters.mergeRunThreshold) {
               Merges.merge(a, sortParameters.getOrder(), sortAnalysis, 0, sortAnalysis.getNumRuns(), sortIndexer);
               orderHandled = true;
            } else {
               T[] b;
               long[] _a;
               int   bytesToSortMask;
               // FUTURE - consider msb sort; presently not showing benefit so using prefix
      
               b = ThreadLocalStorage.getOrCreateComparableStorage(a.length);
               bytesToSortMask = computeBytesToSortMask(sortAnalysis.getBitDelta(), INT_BUCKETS);
               _a = objectArrayToLongArray(a, i0, i1, sortIndexer);
               radixSortIntPrefix(_a, sortIndexer.indicesAreSigned() ? Signedness.Signed : Signedness.Unsigned, bytesToSortMask);
               // Can't use msb sort presently as we don't have an efficient
               // sort for [index/value] when array length is small
               //radixSortIntPrefix_msb(_a, Signedness.Unsigned, bytesToSortMask);
               permute(a, b, _a);
               System.arraycopy(b, 0, a, i0, _a.length);
            }
         }
      }
      return orderHandled;
   }      
   
   ///////////////////
   // Counting sorts

   private static void countingSort_OnePass_IntBuckets(long[] a, long[] b, int i1, long bytesToSortMask, Signedness signedness) {
      int[] count;
      int   sortBuckets;

      // The fundamental byte-skipping logic can skip arbitrary bytes
      // This logic is somewhat expensive, however.      
      // If we have a sequence of high order bytes that can be skipped, we
      // optimize this by changing the sort buckets to match the number of
      // bytes that can be skipped.
      // We set the bytesToSortMask to 0xff which disables the expensive byte-skipping logic
      // (and byte skipping is de facto handled by altering the sortBuckets).
      
      // First, count bytes all in one pass
      count = new int[COUNT_SIZE * INT_BUCKETS];
      
      switch ((int)bytesToSortMask) {
      case 0x00:
      case 0x01:
         bytesToSortMask = 0xf;
         sortBuckets = 1;
         signedness = Signedness.Unsigned;
         break;
      case 0x03:
         bytesToSortMask = 0xf;
         sortBuckets = 2;
         signedness = Signedness.Unsigned;
         break;
      case 0x07:
         bytesToSortMask = 0xf;
         sortBuckets = 3;
         signedness = Signedness.Unsigned;
         break;
      case 0x0f:
         sortBuckets = INT_BUCKETS;
         // leave signedness unchanged
         break;
      default:
         sortBuckets = INT_BUCKETS;
         signedness = (bytesToSortMask & 0x4) == 1 ? signedness : Signedness.Unsigned; 
         break;
      }
      
      countBytesOnePass_IntBuckets(a, bytesToSortMask, count);      
      makeCountsCumulative(count, sortBuckets, bytesToSortMask, signedness);
      sortUsingCounts(a, b, sortBuckets, bytesToSortMask, count);
   }
   
   private static boolean hasBytesToSkip(long bytesToSortMask, int numBuckets) {
      if (debug) {
         System.out.printf("%x %d %x %s\n", bytesToSortMask, numBuckets, ~(0xffffffffffffffffL << numBuckets), (~bytesToSortMask & ~(0xffffffffffffffffL << numBuckets)) != 0);
      }
      return (~bytesToSortMask & ~(0xffffffffffffffffL << numBuckets)) != 0;
   }
   
   // i0 = 0 is guaranteed by caller
   private static void countBytesOnePass_IntBuckets(final long[] a, long bytesToSortMask, int[] count) {
      if (hasBytesToSkip(bytesToSortMask, INT_BUCKETS)) {
         // only use expensive byte-skipping logic when it's useful
         for (int i = 0; i < a.length; i++) {
            long  longValue;
            
            longValue = a[i];
            for (int byteIndex = 0; byteIndex < INT_BUCKETS; byteIndex++) {
               if (((1 << byteIndex) & bytesToSortMask) != 0) {
                  int   countIndexBase;
                  int   j;
         
                  countIndexBase = byteIndex * COUNT_SIZE;
                  j = keyForIndex(longValue, byteIndex);
                  count[countIndexBase + j]++;
               }
            }
         }
      } else {
         for (int i = 0; i < a.length; i++) {
            long  longValue;
            
            longValue = a[i];
            for (int byteIndex = 0; byteIndex < INT_BUCKETS; byteIndex++) {
               int   countIndexBase;
               int   j;
      
               countIndexBase = byteIndex * COUNT_SIZE;
               j = keyForIndex(longValue, byteIndex);
               count[countIndexBase + j]++;
            }
         }
      }
   }
   
   private static void makeCountsCumulative(int[] count, int numBuckets, long bytesToSortMask, Signedness signedness) {
      if (signedness.isSigned()) {
         if (hasBytesToSkip(bytesToSortMask, numBuckets)) {
            // only use expensive byte-skipping logic when it's useful
            for (int byteIndex = 0; byteIndex < numBuckets; byteIndex++) {
               if (((1 << byteIndex) & bytesToSortMask) != 0) {
                  int   countIndexBase;
                  
                  countIndexBase = byteIndex * COUNT_SIZE;
                  if (byteIndex == numBuckets - 1 && signedness.isSigned()) {
                     int   prev;
                     
                     prev = BYTE_MIN_VALUE_UNSIGNED;
                     for (int i = BYTE_MIN_VALUE_UNSIGNED + 1; i < BYTE_MIN_VALUE_UNSIGNED + COUNT_SIZE; i++) {
                        int   _i;
                        
                        _i = i & 0xff;
                        count[countIndexBase + _i] += count[countIndexBase + prev];
                        prev = _i;
                     }
                  } else {
                     for (int i = 1; i < COUNT_SIZE; i++) {
                        count[countIndexBase + i] += count[countIndexBase + i - 1];
                     }
                  }
               }
            }
         } else {
            for (int byteIndex = 0; byteIndex < numBuckets; byteIndex++) {
               int   countIndexBase;
               
               countIndexBase = byteIndex * COUNT_SIZE;
               if (byteIndex == numBuckets - 1 && signedness.isSigned()) {
                  int   prev;
                  
                  prev = BYTE_MIN_VALUE_UNSIGNED;
                  for (int i = BYTE_MIN_VALUE_UNSIGNED + 1; i < BYTE_MIN_VALUE_UNSIGNED + COUNT_SIZE; i++) {
                     int   _i;
                     
                     _i = i & 0xff;
                     count[countIndexBase + _i] += count[countIndexBase + prev];
                     prev = _i;
                  }
               } else {
                  for (int i = 1; i < COUNT_SIZE; i++) {
                     count[countIndexBase + i] += count[countIndexBase + i - 1];
                  }
               }
            }
         }
      } else {
         if (hasBytesToSkip(bytesToSortMask, numBuckets)) {
            // only use expensive byte-skipping logic when it's useful
            for (int byteIndex = 0; byteIndex < numBuckets; byteIndex++) {
               if (((1 << byteIndex) & bytesToSortMask) != 0) {
                  int   countIndexBase;
                  
                  countIndexBase = byteIndex * COUNT_SIZE;
                  for (int i = 1; i < COUNT_SIZE; i++) {
                     count[countIndexBase + i] += count[countIndexBase + i - 1];
                  }
               }
            }
         } else {
            for (int byteIndex = 0; byteIndex < numBuckets; byteIndex++) {
               int   countIndexBase;
               
               countIndexBase = byteIndex * COUNT_SIZE;
               for (int i = 1; i < COUNT_SIZE; i++) {
                  count[countIndexBase + i] += count[countIndexBase + i - 1];
               }
            }
         }
      }
   }
   
   private static void makeCountsCumulative_OneBucket(int[] count, int numBuckets, int byteIndex, Signedness signedness) {
      if (signedness.isSigned()) {
         if (byteIndex == numBuckets - 1 && signedness.isSigned()) {
            int   prev;
            
            prev = BYTE_MIN_VALUE_UNSIGNED;
            for (int i = BYTE_MIN_VALUE_UNSIGNED + 1; i < BYTE_MIN_VALUE_UNSIGNED + COUNT_SIZE; i++) {
               int   _i;
               
               _i = i & 0xff;
               count[_i] += count[prev];
               prev = _i;
            }
         } else {
            for (int i = 1; i < COUNT_SIZE; i++) {
               count[i] += count[i - 1];
            }
         }
      } else {
         for (int i = 1; i < COUNT_SIZE; i++) {
            count[i] += count[i - 1];
         }
      }
   }
   
   // i0 = 0 here
   private static void sortUsingCounts(long[] a, long [] b, int numBuckets, long bytesToSortMask, int[] count) {
      int      aLength;
      
      aLength = a.length;      
      if (hasBytesToSkip(bytesToSortMask, numBuckets)) {
         long[]   source;
         long[]   dest;
         int      source0;
         int      dest0;
         int      numSwaps;
         
         numSwaps = 0;
         source = a;
         dest = b;
         source0 = 0;
         dest0 = 0;
         
         for (int byteIndex = 0; byteIndex < numBuckets; byteIndex++) {
            long[]   tmpArray;
            int      tmp0;
            
            if (((1 << byteIndex) & bytesToSortMask) != 0) {
               int   countIndexBase;

               ++numSwaps;
               countIndexBase = byteIndex * COUNT_SIZE;
               for (int i = aLength - 1; i >= 0; i--) {
                  int j;
         
                  j = keyForIndex(source[0 + i], byteIndex);
                  dest[dest0 + --count[countIndexBase + j]] = source[source0 + i];
               }
               tmpArray = source;
               source = dest;
               dest = tmpArray;
               tmp0 = source0;
               source0 = dest0;
               dest0 = tmp0;
            }
         }
         if (numSwaps % 2 == 1) { // if an odd number of swaps, then results are left in b; copy them to a
            System.arraycopy(b, 0, a, 0, aLength);
         }     
      } else {
         for (int byteIndex = 0; byteIndex < numBuckets; byteIndex++) {
            int   countIndexBase;
            long[]   tmp;
            int      numSwaps;
            
            numSwaps = 0;
            countIndexBase = byteIndex * COUNT_SIZE;
            for (int i = aLength - 1; i >= 0; i--) {
               int j;
      
               j = keyForIndex(a[i], byteIndex);
               b[--count[countIndexBase + j]] = a[i];               
            }
            ++numSwaps;
            tmp = a;
            a = b;
            b = tmp;
            if (numSwaps % 2 == 1) { 
               // if an odd number of swaps, then results are left in "a"; but a and b are swapped, so "a" is the original b
               // copy from current "a" to current "b"; i.e. from original b to original a where it belongs
               System.arraycopy(a, 0, b, 0, aLength);
            }
         }
      }
   }
   
   ///////////////////////////
   
   private static void countingSort_msb(long[] a, int i0, int i1, Signedness signedness, long bytesToSortMask, int byteIndex, int depth) {
      int      aLength;
      int[]    count;

      if (i1 - i0 <  MSB_MIN_SIZE) {
         Arrays.sort(a, i0, i1);
         return;
      }

      if (hasBytesToSkip(bytesToSortMask, LONG_BUCKETS)) {
         // if no bytes to sort at this index; try the next index
         while (((1 << byteIndex) & bytesToSortMask) == 0) {
            if (byteIndex == 0) {
               // no more indices left; return
               return;
            } else {
               --byteIndex;
            }
         }
      }
      
      aLength = i1 - i0;
      
      count = new int[COUNT_SIZE];
      for (int i = 0; i < aLength; i++) {
         int   j;
         long  longValue;
         
         longValue = a[i0 + i];
         j = keyForIndex(longValue, byteIndex);
         count[j]++;
      }
      
      makeCountsCumulative_OneBucket(count, LONG_BUCKETS, byteIndex, signedness);
      int   numBuckets = LONG_BUCKETS;
      
      int[]    savedCount;
      savedCount = new int[COUNT_SIZE];
      System.arraycopy(count, 0, savedCount, 0, count.length);
      
      // similar to previous doSort(a, b, i0, aLength, LONG_BUCKETS, bytesToSortMask, count);
      long[]   b;
      
      b = new long[aLength];
      
      for (int i = aLength - 1; i >= 0; i--) {
         int j;

         j = keyForIndex(a[i0 + i], byteIndex);
         b[--count[j]] = a[i0 + i];
      }
      System.arraycopy(b, 0, a, i0, aLength);

      if (byteIndex > 0) {
         // recurse
         if (byteIndex == numBuckets - 1 && signedness.isSigned()) {
            int   prev;
            
            prev = 0;
            for (int i = BYTE_MIN_VALUE_UNSIGNED; i < BYTE_MIN_VALUE_UNSIGNED + COUNT_SIZE; i++) {
               int   _i;
               
               _i = i & 0xff;
               
               int   next;               
               next = savedCount[_i];
               if (next - prev > 1) {
                  countingSort_msb(a, i0 + prev, i0 + next, signedness, bytesToSortMask, byteIndex - 1, depth + 1);
               }
               prev = next;
            }
         } else {
            int   prev;
            
            prev = 0;
            for (int i = 0; i < count.length; i++) {
               int   next;
               
               next = savedCount[i];
               if (next - prev > 1) {
                  countingSort_msb(a, i0 + prev, i0 + next, signedness, bytesToSortMask, byteIndex - 1, depth + 1);
               }
               prev = next;
            }
         }
      }
   }   

   private static void countingSort_msb(int[] a, int i0, int i1, Signedness signedness, int bytesToSortMask, int byteIndex, int depth) {
      int      aLength;
      int[]    count;
      
      if (i1 - i0 <  MSB_MIN_SIZE) {
         Arrays.sort(a, i0, i1);
         return;
      }

      if (hasBytesToSkip(bytesToSortMask, INT_BUCKETS)) {
         // if no bytes to sort at this index; try the next index
         while (((1 << byteIndex) & bytesToSortMask) == 0) {
            if (byteIndex == 0) {
               // no more indices left; return
               return;
            } else {
               --byteIndex;
            }
         }
      }
      
      aLength = i1 - i0;
      
      count = new int[COUNT_SIZE];
      for (int i = 0; i < aLength; i++) {
         int   j;
         int   intValue;
         
         intValue = a[i0 + i];
         j = keyForIndex(intValue, byteIndex);
         count[j]++;
      }
      
      // below does all bytes, we only want to do one...
      makeCountsCumulative_OneBucket(count, INT_BUCKETS, byteIndex, signedness);
      int   numBuckets = INT_BUCKETS;
      
      int[]    savedCount;
      savedCount = new int[COUNT_SIZE];
      System.arraycopy(count, 0, savedCount, 0, count.length);
      
      // similar to previous doSort(a, b, i0, aLength, INT_BUCKETS, bytesToSortMask, count);
      int[]   b;
      
      b = new int[aLength];
      
      for (int i = aLength - 1; i >= 0; i--) {
         int j;

         j = keyForIndex(a[i0 + i], byteIndex);
         b[--count[j]] = a[i0 + i];
      }
      System.arraycopy(b, 0, a, i0, aLength);

      if (byteIndex > 0) {
         // recurse
         if (byteIndex == numBuckets - 1 && signedness.isSigned()) {
            int   prev;
            
            prev = 0;
            for (int i = BYTE_MIN_VALUE_UNSIGNED; i < BYTE_MIN_VALUE_UNSIGNED + COUNT_SIZE; i++) {
               int   _i;
               
               _i = i & 0xff;
               
               int   next;               
               next = savedCount[_i];
               if (next - prev > 1) {
                  countingSort_msb(a, i0 + prev, i0 + next, signedness, bytesToSortMask, byteIndex - 1, depth + 1);
               }
               prev = next;
            }
         } else {
            int   prev;
            
            prev = 0;
            for (int i = 0; i < count.length; i++) {
               int   next;
               
               next = savedCount[i];
               if (next - prev > 1) {
                  countingSort_msb(a, i0 + prev, i0 + next, signedness, bytesToSortMask, byteIndex - 1, depth + 1);
               }
               prev = next;
            }
         }
      }
   }
   
   //////////////////////////////////
   // Sort interface implementation
   
   // Primitives

   @Override
   public void sort(int[] a, SortParameters sortParameters) {
      sortParameters = sortParameters.getDefaultsFor(a);
      if (sortParameters.getNumElements() > 1) {
         boolean  orderHandled;
         
         orderHandled = radixSort(a, sortParameters);
         if (!orderHandled && sortParameters.getOrder() == Order.DESCENDING) {
            ArrayUtil.reverse(a, sortParameters.getI0(), sortParameters.getI1());
         }
      }
   }

   @Override
   public void sort(long[] a, SortParameters sortParameters) {
      sortParameters = sortParameters.getDefaultsFor(a);
      if (sortParameters.getNumElements() > 1) {
         boolean  orderHandled;
         
         orderHandled = radixSort(a, sortParameters);
         if (!orderHandled && sortParameters.getOrder() == Order.DESCENDING) {
            ArrayUtil.reverse(a, sortParameters.getI0(), sortParameters.getI1());
         }
      }
   }

   @Override
   public void sort(double[] a, SortParameters sortParameters) {
      sortParameters = sortParameters.getDefaultsFor(a);
      if (sortParameters.getNumElements() > 1) {
         boolean  orderHandled;
   
         // Prefix presently faster
         orderHandled = radixSortByPrefix(a, sortParameters);
         //orderHandled = radixSort(a, sortParameters);
         if (!orderHandled && sortParameters.getOrder() == Order.DESCENDING) {
            ArrayUtil.reverse(a, sortParameters.getI0(), sortParameters.getI1());
         }
      }
   }
   
   ///////////////////////////////////////
   // Generic array, explicit Comparator

   @Override
   public <T> void sort(T[] a, SortParameters sortParameters, Comparator<T> comparator) {
      SystemSort.instance.sort(a, sortParameters, comparator);
   }   
   
   ////////////////////////////////////////
   // Generic array, explicit SortIndexer
   
   @Override
   public <T> void sort(T[] a, SortParameters sortParameters, SortIndexer<T> sortIndexer) {
      sortParameters = sortParameters.getDefaultsFor(a);
      if (sortParameters.getNumElements() > 1) {
         boolean  orderHandled;
         
         orderHandled = radixSort(a, sortParameters, sortIndexer);
         if (!orderHandled && sortParameters.getOrder() == Order.DESCENDING) {
            ArrayUtil.reverse(a, sortParameters.getI0(), sortParameters.getI1());
         }
      }
   }
}
