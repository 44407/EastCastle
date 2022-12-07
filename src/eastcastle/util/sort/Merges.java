package eastcastle.util.sort;

import java.util.Comparator;
import java.util.List;

import eastcastle.util.ThreadLocalStorage;

/**
 * Merge functionality for all sorted types.
 */
class Merges {
   public static void merge(int[] a, int s0, int e0, int s1, int e1, int t0) {
      int[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateIntStorage(totalLength);
      i0 = s0;
      i1 = s1;
      it = 0;
      while (i0 < e0 && i1 < e1) {
         if (a[i0] <= a[i1]) {
            t[it] = a[i0];
            ++i0;
         } else {
            t[it] = a[i1];
            ++i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         System.arraycopy(a, i0, t, it, totalLength - it);
      } else {
         System.arraycopy(a, i1, t, it, totalLength - it);
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }
   
   public static void merge(long[] a, int s0, int e0, int s1, int e1, int t0) {
      long[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateLongStorage(totalLength);
      i0 = s0;
      i1 = s1;
      it = 0;
      while (i0 < e0 && i1 < e1) {
         if (a[i0] <= a[i1]) {
            t[it] = a[i0];
            ++i0;
         } else {
            t[it] = a[i1];
            ++i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         System.arraycopy(a, i0, t, it, totalLength - it);
      } else {
         System.arraycopy(a, i1, t, it, totalLength - it);
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }

   public static void merge(double[] a, int s0, int e0, int s1, int e1, int t0) {
      double[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateDoubleStorage(totalLength);
      i0 = s0;
      i1 = s1;
      it = 0;
      while (i0 < e0 && i1 < e1) {
         if (a[i0] <= a[i1]) {
            t[it] = a[i0];
            ++i0;
         } else {
            t[it] = a[i1];
            ++i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         System.arraycopy(a, i0, t, it, totalLength - it);
      } else {
         System.arraycopy(a, i1, t, it, totalLength - it);
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }
   
   public static <T extends Comparable<? super T>> void merge(T[] a, int s0, int e0, int s1, int e1, int t0) {
      merge(a, s0, e0, s1, e1, t0, Comparator.naturalOrder());
   }
   
   public static <T> void merge(T[] a, int s0, int e0, int s1, int e1, int t0, Comparator<T> c) {
      T[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateComparableStorage(totalLength);
      i0 = s0;
      i1 = s1;
      it = 0;
      while (i0 < e0 && i1 < e1) {
         if (c.compare(a[i0], a[i1]) <= 0) {
            t[it] = a[i0];
            ++i0;
         } else {
            t[it] = a[i1];
            ++i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         System.arraycopy(a, i0, t, it, totalLength - it);
      } else {
         System.arraycopy(a, i1, t, it, totalLength - it);
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }
   
   /////////
   // long

   public static Run merge(long[] a, Order sortOrder, SortAnalysis sortAnalysis, int r0, int r1) {
      if (r1 < 0) {
         throw new RuntimeException();
      }
      if (r1 - r0 == 1) {                // one run; no work to do
         return sortAnalysis.getRun(r0);
      } else if (r1 - r0 == 2) {         // two runs; merge them
         Run   mergedRun;
         Run   runA;
         Run   runB;
                  
         runA = sortAnalysis.getRun(r0);
         runB = sortAnalysis.getRun(r0 + 1);         
         if (sortOrder.isAscending()) {
            Merges.mergeAscending(a, runA, runB);
         } else {
            Merges.mergeDescending(a, runA, runB);
         }
         mergedRun = new Run(sortOrder, runA.getI0(), runB.getI1());
         return mergedRun;
      } else {                           // > 2 runs; recurse
         Run   mergedRun;
         Run   runA;
         Run   runB;
         int   midpointRunIndex; // Midpoint *run* index, not element
                  
         midpointRunIndex = r0 + (r1 - r0 + 1) / 2;
         runA = merge(a, sortOrder, sortAnalysis, r0, midpointRunIndex);
         runB = merge(a, sortOrder, sortAnalysis, midpointRunIndex, r1);
         if (sortOrder.isAscending()) {
            Merges.mergeAscending(a, runA, runB);
         } else {
            Merges.mergeDescending(a, runA, runB);
         }
         mergedRun = new Run(sortOrder, runA.getI0(), runB.getI1());
         return mergedRun;
      }
   }   
   
   public static void mergeAscending(long[] a, Run r0, Run r1) {
      if (r0.getOrder().isAscending()) {
         if (r1.getOrder().isAscending()) {
            mergeAscending_AscendingAscending(a, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0());
         } else {
            mergeAscending_AscendingDescending(a, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0());
         }
      } else {
         if (r1.getOrder().isAscending()) {
            mergeAscending_AscendingDescending(a, r1.getI0(), r1.getI1(), r0.getI0(), r0.getI1(), r0.getI0());
         } else {
            mergeAscending_DescendingDescending(a, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0());
         }
      }
   }
   
   public static void mergeDescending(long[] a, Run r0, Run r1) {
      if (!r0.getOrder().isAscending()) {
         if (!r1.getOrder().isAscending()) {
            mergeDescending_DescendingDescending(a, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0());
         } else {
            mergeDescending_DescendingAscending(a, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0());
         }
      } else {
         if (!r1.getOrder().isAscending()) {
            mergeDescending_DescendingAscending(a, r1.getI0(), r1.getI1(), r0.getI0(), r0.getI1(), r0.getI0());
         } else {
            mergeDescending_AscendingAscending(a, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0());
         }
      }
   }
   
   public static void mergeAscending_AscendingAscending(long[] a, int s0, int e0, int s1, int e1, int t0) {
      long[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateLongStorage(totalLength);
      i0 = s0;
      i1 = s1;
      it = 0;
      while (i0 < e0 && i1 < e1) {
         if (a[i0] <= a[i1]) {
            t[it] = a[i0];
            ++i0;
         } else {
            t[it] = a[i1];
            ++i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i0 + i];
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i1 + i];
         }
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }

   public static void mergeAscending_AscendingDescending(long[] a, int s0, int e0, int s1, int e1, int t0) {
      long[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateLongStorage(totalLength);
      i0 = s0;
      i1 = e1 - 1;
      it = 0;
      while (i0 < e0 && i1 >= s1) {
         if (a[i0] <= a[i1]) {
            t[it] = a[i0];
            ++i0;
         } else {
            t[it] = a[i1];
            --i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i0 + i];
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i1 - i];
         }
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }
   
   public static void mergeAscending_DescendingDescending(long[] a, int s0, int e0, int s1, int e1, int t0) {
      long[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateLongStorage(totalLength);
      i0 = e0 - 1;
      i1 = e1 - 1;
      it = 0;
      while (i0 >= s0 && i1 >= s1) {
         if (a[i0] <= a[i1]) {
            t[it] = a[i0];
            --i0;
         } else {
            t[it] = a[i1];
            --i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 >= s0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i0 - i];
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i1 - i];
         }
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }

   
   public static void mergeDescending_DescendingDescending(long[] a, int s0, int e0, int s1, int e1, int t0) {
      long[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateLongStorage(totalLength);
      i0 = s0;
      i1 = s1;
      it = 0;
      while (i0 < e0 && i1 < e1) {
         if (a[i0] >= a[i1]) {
            t[it] = a[i0];
            ++i0;
         } else {
            t[it] = a[i1];
            ++i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i0 + i];
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i1 + i];
         }
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }

   public static void mergeDescending_DescendingAscending(long[] a, int s0, int e0, int s1, int e1, int t0) {
      long[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateLongStorage(totalLength);
      i0 = s0;
      i1 = e1 - 1;
      it = 0;
      while (i0 < e0 && i1 >= s1) {
         if (a[i0] >= a[i1]) {
            t[it] = a[i0];
            ++i0;
         } else {
            t[it] = a[i1];
            --i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i0 + i];
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i1 - i];
         }
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }
   
   public static void mergeDescending_AscendingAscending(long[] a, int s0, int e0, int s1, int e1, int t0) {
      long[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateLongStorage(totalLength);
      i0 = e0 - 1;
      i1 = e1 - 1;
      it = 0;
      while (i0 >= s0 && i1 >= s1) {
         if (a[i0] >= a[i1]) {
            t[it] = a[i0];
            --i0;
         } else {
            t[it] = a[i1];
            --i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 >= s0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i0 - i];
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i1 - i];
         }
      }      
      for (int i = 0; i < totalLength; i++) {
         a[t0 + i] = t[i];
      }
   }
   
   
   /////////
   // int
   
   public static Run merge(int[] a, Order sortOrder, SortAnalysis sortAnalysis, int r0, int r1) {
      if (r1 < 0) {
         throw new RuntimeException();
      }
      if (r1 - r0 == 1) {                // one run; no work to do
         return sortAnalysis.getRun(r0);
      } else if (r1 - r0 == 2) {         // two runs; merge them
         Run   mergedRun;
         Run   runA;
         Run   runB;
                  
         runA = sortAnalysis.getRun(r0);
         runB = sortAnalysis.getRun(r0 + 1);         
         if (sortOrder.isAscending()) {
            Merges.mergeAscending(a, runA, runB);
         } else {
            Merges.mergeDescending(a, runA, runB);
         }
         mergedRun = new Run(sortOrder, runA.getI0(), runB.getI1());
         return mergedRun;
      } else {                           // > 2 runs; recurse
         Run   mergedRun;
         Run   runA;
         Run   runB;
         int   midpointRunIndex; // Midpoint *run* index, not element
                  
         midpointRunIndex = r0 + (r1 - r0 + 1) / 2;
         runA = merge(a, sortOrder, sortAnalysis, r0, midpointRunIndex);
         runB = merge(a, sortOrder, sortAnalysis, midpointRunIndex, r1);
         if (sortOrder.isAscending()) {
            Merges.mergeAscending(a, runA, runB);
         } else {
            Merges.mergeDescending(a, runA, runB);
         }
         mergedRun = new Run(sortOrder, runA.getI0(), runB.getI1());
         return mergedRun;
      }
   }
   
   public static void mergeAscending(int[] a, Run r0, Run r1) {
      if (r0.getOrder().isAscending()) {
         if (r1.getOrder().isAscending()) {
            mergeAscending_AscendingAscending(a, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0());
         } else {
            mergeAscending_AscendingDescending(a, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0());
         }
      } else {
         if (r1.getOrder().isAscending()) {
            mergeAscending_AscendingDescending(a, r1.getI0(), r1.getI1(), r0.getI0(), r0.getI1(), r0.getI0());
         } else {
            mergeAscending_DescendingDescending(a, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0());
         }
      }
   }
   
   public static void mergeDescending(int[] a, Run r0, Run r1) {
      if (!r0.getOrder().isAscending()) {
         if (!r1.getOrder().isAscending()) {
            mergeDescending_DescendingDescending(a, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0());
         } else {
            mergeDescending_DescendingAscending(a, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0());
         }
      } else {
         if (!r1.getOrder().isAscending()) {
            mergeDescending_DescendingAscending(a, r1.getI0(), r1.getI1(), r0.getI0(), r0.getI1(), r0.getI0());
         } else {
            mergeDescending_AscendingAscending(a, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0());
         }
      }
   }
   
   public static void mergeAscending_AscendingAscending(int[] a, int s0, int e0, int s1, int e1, int t0) {
      int[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateIntStorage(totalLength);
      i0 = s0;
      i1 = s1;
      it = 0;
      while (i0 < e0 && i1 < e1) {
         if (a[i0] <= a[i1]) {
            t[it] = a[i0];
            ++i0;
         } else {
            t[it] = a[i1];
            ++i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i0 + i];
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i1 + i];
         }
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }

   public static void mergeAscending_AscendingDescending(int[] a, int s0, int e0, int s1, int e1, int t0) {
      int[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateIntStorage(totalLength);
      i0 = s0;
      i1 = e1 - 1;
      it = 0;
      while (i0 < e0 && i1 >= s1) {
         if (a[i0] <= a[i1]) {
            t[it] = a[i0];
            ++i0;
         } else {
            t[it] = a[i1];
            --i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i0 + i];
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i1 - i];
         }
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }
   
   public static void mergeAscending_DescendingDescending(int[] a, int s0, int e0, int s1, int e1, int t0) {
      int[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateIntStorage(totalLength);
      i0 = e0 - 1;
      i1 = e1 - 1;
      it = 0;
      while (i0 >= s0 && i1 >= s1) {
         if (a[i0] <= a[i1]) {
            t[it] = a[i0];
            --i0;
         } else {
            t[it] = a[i1];
            --i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 >= s0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i0 - i];
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i1 - i];
         }
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }
   
   public static void mergeDescending_DescendingDescending(int[] a, int s0, int e0, int s1, int e1, int t0) {
      int[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateIntStorage(totalLength);
      i0 = s0;
      i1 = s1;
      it = 0;
      while (i0 < e0 && i1 < e1) {
         if (a[i0] >= a[i1]) {
            t[it] = a[i0];
            ++i0;
         } else {
            t[it] = a[i1];
            ++i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i0 + i];
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i1 + i];
         }
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }

   public static void mergeDescending_DescendingAscending(int[] a, int s0, int e0, int s1, int e1, int t0) {
      int[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateIntStorage(totalLength);
      i0 = s0;
      i1 = e1 - 1;
      it = 0;
      while (i0 < e0 && i1 >= s1) {
         if (a[i0] >= a[i1]) {
            t[it] = a[i0];
            ++i0;
         } else {
            t[it] = a[i1];
            --i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i0 + i];
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i1 - i];
         }
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }
   
   public static void mergeDescending_AscendingAscending(int[] a, int s0, int e0, int s1, int e1, int t0) {
      int[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateIntStorage(totalLength);
      i0 = e0 - 1;
      i1 = e1 - 1;
      it = 0;
      while (i0 >= s0 && i1 >= s1) {
         if (a[i0] >= a[i1]) {
            t[it] = a[i0];
            --i0;
         } else {
            t[it] = a[i1];
            --i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 >= s0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i0 - i];
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i1 - i];
         }
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }
   
   /////////
   // double

   public static Run merge(double[] a, Order sortOrder, SortAnalysis sortAnalysis, int r0, int r1) {
      if (r1 < 0) {
         throw new RuntimeException();
      }
      if (r1 - r0 == 1) {                // one run; no work to do
         return sortAnalysis.getRun(r0);
      } else if (r1 - r0 == 2) {         // two runs; merge them
         Run   mergedRun;
         Run   runA;
         Run   runB;
                  
         runA = sortAnalysis.getRun(r0);
         runB = sortAnalysis.getRun(r0 + 1);         
         if (sortOrder.isAscending()) {
            Merges.mergeAscending(a, runA, runB);
         } else {
            Merges.mergeDescending(a, runA, runB);
         }
         mergedRun = new Run(sortOrder, runA.getI0(), runB.getI1());
         return mergedRun;
      } else {                           // > 2 runs; recurse
         Run   mergedRun;
         Run   runA;
         Run   runB;
         int   midpointRunIndex; // Midpoint *run* index, not element
                  
         midpointRunIndex = r0 + (r1 - r0 + 1) / 2;
         runA = merge(a, sortOrder, sortAnalysis, r0, midpointRunIndex);
         runB = merge(a, sortOrder, sortAnalysis, midpointRunIndex, r1);
         if (sortOrder.isAscending()) {
            Merges.mergeAscending(a, runA, runB);
         } else {
            Merges.mergeDescending(a, runA, runB);
         }
         mergedRun = new Run(sortOrder, runA.getI0(), runB.getI1());
         return mergedRun;
      }
   }

   public static void mergeAscending(double[] a, Run r0, Run r1) {
      if (r0.getOrder().isAscending()) {
         if (r1.getOrder().isAscending()) {
            mergeAscending_AscendingAscending(a, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0());
         } else {
            mergeAscending_AscendingDescending(a, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0());
         }
      } else {
         if (r1.getOrder().isAscending()) {
            mergeAscending_AscendingDescending(a, r1.getI0(), r1.getI1(), r0.getI0(), r0.getI1(), r0.getI0());
         } else {
            mergeAscending_DescendingDescending(a, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0());
         }
      }
   }
   
   public static void mergeDescending(double[] a, Run r0, Run r1) {
      if (!r0.getOrder().isAscending()) {
         if (!r1.getOrder().isAscending()) {
            mergeDescending_DescendingDescending(a, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0());
         } else {
            mergeDescending_DescendingAscending(a, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0());
         }
      } else {
         if (!r1.getOrder().isAscending()) {
            mergeDescending_DescendingAscending(a, r1.getI0(), r1.getI1(), r0.getI0(), r0.getI1(), r0.getI0());
         } else {
            mergeDescending_AscendingAscending(a, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0());
         }
      }
   }
   
   public static void mergeAscending_AscendingAscending(double[] a, int s0, int e0, int s1, int e1, int t0) {
      double[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateDoubleStorage(totalLength);
      i0 = s0;
      i1 = s1;
      it = 0;
      while (i0 < e0 && i1 < e1) {
         if (a[i0] <= a[i1]) {
            t[it] = a[i0];
            ++i0;
         } else {
            t[it] = a[i1];
            ++i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i0 + i];
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i1 + i];
         }
      } 
      System.arraycopy(t, 0, a, t0, totalLength);
   }

   public static void mergeAscending_AscendingDescending(double[] a, int s0, int e0, int s1, int e1, int t0) {
      double[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateDoubleStorage(totalLength);
      i0 = s0;
      i1 = e1 - 1;
      it = 0;
      while (i0 < e0 && i1 >= s1) {
         if (a[i0] <= a[i1]) {
            t[it] = a[i0];
            ++i0;
         } else {
            t[it] = a[i1];
            --i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i0 + i];
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i1 - i];
         }
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }
   
   public static void mergeAscending_DescendingDescending(double[] a, int s0, int e0, int s1, int e1, int t0) {
      double[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateDoubleStorage(totalLength);
      i0 = e0 - 1;
      i1 = e1 - 1;
      it = 0;
      while (i0 >= s0 && i1 >= s1) {
         if (a[i0] <= a[i1]) {
            t[it] = a[i0];
            --i0;
         } else {
            t[it] = a[i1];
            --i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 >= s0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i0 - i];
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i1 - i];
         }
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }

   
   public static void mergeDescending_DescendingDescending(double[] a, int s0, int e0, int s1, int e1, int t0) {
      double[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateDoubleStorage(totalLength);
      i0 = s0;
      i1 = s1;
      it = 0;
      while (i0 < e0 && i1 < e1) {
         if (a[i0] >= a[i1]) {
            t[it] = a[i0];
            ++i0;
         } else {
            t[it] = a[i1];
            ++i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i0 + i];
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i1 + i];
         }
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }

   public static void mergeDescending_DescendingAscending(double[] a, int s0, int e0, int s1, int e1, int t0) {
      double[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateDoubleStorage(totalLength);
      i0 = s0;
      i1 = e1 - 1;
      it = 0;
      while (i0 < e0 && i1 >= s1) {
         if (a[i0] >= a[i1]) {
            t[it] = a[i0];
            ++i0;
         } else {
            t[it] = a[i1];
            --i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i0 + i];
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i1 - i];
         }
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }
   
   public static void mergeDescending_AscendingAscending(double[] a, int s0, int e0, int s1, int e1, int t0) {
      double[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateDoubleStorage(totalLength);
      i0 = e0 - 1;
      i1 = e1 - 1;
      it = 0;
      while (i0 >= s0 && i1 >= s1) {
         if (a[i0] >= a[i1]) {
            t[it] = a[i0];
            --i0;
         } else {
            t[it] = a[i1];
            --i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 >= s0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i0 - i];
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i1 - i];
         }
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }
   
   
   /////////////////
   // Comparable[]
   
   // recursive
   public static <T> Run merge(T[] a, Order sortOrder, SortAnalysis sortAnalysis, int r0, int r1, Comparator<T> c) {
      if (r1 - r0 == 1) {                // one run; no work to do
         return sortAnalysis.getRun(r0);
      } else if (r1 - r0 == 2) {         // two runs; merge them
         Run   mergedRun;
         Run   runA;
         Run   runB;
                  
         runA = sortAnalysis.getRun(r0);
         runB = sortAnalysis.getRun(r0 + 1);         
         if (sortOrder.isAscending()) {
            Merges.mergeAscending(a, runA, runB, c);
         } else {
            Merges.mergeDescending(a, runA, runB, c);
         }
         mergedRun = new Run(sortOrder, runA.getI0(), runB.getI1());
         return mergedRun;
      } else {                           // > 2 runs; recurse
         Run   mergedRun;
         Run   runA;
         Run   runB;
         int   midpointRunIndex; // Midpoint *run* index, not element
                  
         midpointRunIndex = r0 + (r1 - r0 + 1) / 2;
         runA = merge(a, sortOrder, sortAnalysis, r0, midpointRunIndex, c);
         runB = merge(a, sortOrder, sortAnalysis, midpointRunIndex, r1, c);
         if (sortOrder.isAscending()) {
            Merges.mergeAscending(a, runA, runB, c);
         } else {
            Merges.mergeDescending(a, runA, runB, c);
         }
         mergedRun = new Run(sortOrder, runA.getI0(), runB.getI1());
         return mergedRun;
      }
   }
   
   public static <T> void mergeAscending(T[] a, Run r0, Run r1, Comparator<T> c) {
      if (r0.getOrder().isAscending()) {
         if (r1.getOrder().isAscending()) {
            mergeAscending_AscendingAscending(a, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0(), c);
         } else {
            mergeAscending_AscendingDescending(a, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0(), c);
         }
      } else {
         if (r1.getOrder().isAscending()) {
            mergeAscending_AscendingDescending(a, r1.getI0(), r1.getI1(), r0.getI0(), r0.getI1(), r0.getI0(), c);
         } else {
            mergeAscending_DescendingDescending(a, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0(), c);
         }
      }
   }
   
   public static <T> void mergeDescending(T[] a, Run r0, Run r1, Comparator<T> c) {
      if (!r0.getOrder().isAscending()) {
         if (!r1.getOrder().isAscending()) {
            mergeDescending_DescendingDescending(a, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0(), c);
         } else {
            mergeDescending_DescendingAscending(a, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0(), c);
         }
      } else {
         if (!r1.getOrder().isAscending()) {
            mergeDescending_DescendingAscending(a, r1.getI0(), r1.getI1(), r0.getI0(), r0.getI1(), r0.getI0(), c);
         } else {
            mergeDescending_AscendingAscending(a, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0(), c);
         }
      }
   }
   
   public static <T> void mergeAscending_AscendingAscending(T[] a, int s0, int e0, int s1, int e1, int t0, Comparator<T> c) {
      T[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateComparableStorage(totalLength);
      i0 = s0;
      i1 = s1;
      it = 0;
      while (i0 < e0 && i1 < e1) {
         if (c.compare(a[i0], a[i1]) <= 0) {
            t[it] = a[i0];
            ++i0;
         } else {
            t[it] = a[i1];
            ++i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i0 + i];
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i1 + i];
         }
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }

   public static <T> void mergeAscending_AscendingDescending(T[] a, int s0, int e0, int s1, int e1, int t0, Comparator<T> c) {
      T[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateComparableStorage(totalLength);
      i0 = s0;
      i1 = e1 - 1;
      it = 0;
      while (i0 < e0 && i1 >= s1) {
         if (c.compare(a[i0], a[i1]) <= 0) {
            t[it] = a[i0];
            ++i0;
         } else {
            t[it] = a[i1];
            --i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i0 + i];
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i1 - i];
         }
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }
   
   public static <T> void mergeAscending_DescendingDescending(T[] a, int s0, int e0, int s1, int e1, int t0, Comparator<T> c) {
      T[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateComparableStorage(totalLength);
      i0 = e0 - 1;
      i1 = e1 - 1;
      it = 0;
      while (i0 >= s0 && i1 >= s1) {
         if (c.compare(a[i0], a[i1]) <= 0) {
            t[it] = a[i0];
            --i0;
         } else {
            t[it] = a[i1];
            --i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 >= s0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i0 - i];
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i1 - i];
         }
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }
   
   public static <T> void mergeDescending_DescendingDescending(T[] a, int s0, int e0, int s1, int e1, int t0, Comparator<T> c) {
      T[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateComparableStorage(totalLength);
      i0 = s0;
      i1 = s1;
      it = 0;
      while (i0 < e0 && i1 < e1) {
         if (c.compare(a[i0], a[i1]) >= 0) {
            t[it] = a[i0];
            ++i0;
         } else {
            t[it] = a[i1];
            ++i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i0 + i];
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i1 + i];
         }
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }

   public static <T> void mergeDescending_DescendingAscending(T[] a, int s0, int e0, int s1, int e1, int t0, Comparator<T> c) {
      T[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateComparableStorage(totalLength);
      i0 = s0;
      i1 = e1 - 1;
      it = 0;
      while (i0 < e0 && i1 >= s1) {
         if (c.compare(a[i0], a[i1]) >= 0) {
            t[it] = a[i0];
            ++i0;
         } else {
            t[it] = a[i1];
            --i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i0 + i];
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i1 - i];
         }
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }
   
   public static <T> void mergeDescending_AscendingAscending(T[] a, int s0, int e0, int s1, int e1, int t0, Comparator<T> c) {
      T[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateComparableStorage(totalLength);
      i0 = e0 - 1;
      i1 = e1 - 1;
      it = 0;
      while (i0 >= s0 && i1 >= s1) {
         if (c.compare(a[i0], a[i1]) >= 0) {
            t[it] = a[i0];
            --i0;
         } else {
            t[it] = a[i1];
            --i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 >= s0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i0 - i];
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = a[i1 - i];
         }
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }
   
   ////////////////////
   // Comparable List
   
   // recursive
   public static <T extends Comparable<T>> Run merge(List<T> l, Order sortOrder, SortAnalysis sortAnalysis, int r0, int r1, Comparator<T> c) {
      if (r1 - r0 == 1) {                // one run; no work to do
         return sortAnalysis.getRun(r0);
      } else if (r1 - r0 == 2) {         // two runs; merge them
         Run   mergedRun;
         Run   runA;
         Run   runB;
                  
         runA = sortAnalysis.getRun(r0);
         runB = sortAnalysis.getRun(r0 + 1);         
         if (sortOrder.isAscending()) {
            Merges.mergeAscending(l, runA, runB, c);
         } else {
            Merges.mergeDescending(l, runA, runB, c);
         }
         mergedRun = new Run(sortOrder, runA.getI0(), runB.getI1());
         return mergedRun;
      } else {                           // > 2 runs; recurse
         Run   mergedRun;
         Run   runA;
         Run   runB;
         int   midpointRunIndex; // Midpoint *run* index, not element
                  
         midpointRunIndex = r0 + (r1 - r0 + 1) / 2;
         runA = merge(l, sortOrder, sortAnalysis, r0, midpointRunIndex, c);
         runB = merge(l, sortOrder, sortAnalysis, midpointRunIndex, r1, c);
         if (sortOrder.isAscending()) {
            Merges.mergeAscending(l, runA, runB, c);
         } else {
            Merges.mergeDescending(l, runA, runB, c);
         }
         mergedRun = new Run(sortOrder, runA.getI0(), runB.getI1());
         return mergedRun;
      }
   }
   
   public static <T> void mergeAscending(List<T> l, Run r0, Run r1, Comparator<T> c) {
      if (r0.getOrder().isAscending()) {
         if (r1.getOrder().isAscending()) {
            mergeAscending_AscendingAscending(l, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0(), c);
         } else {
            mergeAscending_AscendingDescending(l, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0(), c);
         }
      } else {
         if (r1.getOrder().isAscending()) {
            mergeAscending_AscendingDescending(l, r1.getI0(), r1.getI1(), r0.getI0(), r0.getI1(), r0.getI0(), c);
         } else {
            mergeAscending_DescendingDescending(l, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0(), c);
         }
      }
   }
   
   public static <T> void mergeDescending(List<T> l, Run r0, Run r1, Comparator<T> c) {
      if (!r0.getOrder().isAscending()) {
         if (!r1.getOrder().isAscending()) {
            mergeDescending_DescendingDescending(l, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0(), c);
         } else {
            mergeDescending_DescendingAscending(l, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0(), c);
         }
      } else {
         if (!r1.getOrder().isAscending()) {
            mergeDescending_DescendingAscending(l, r1.getI0(), r1.getI1(), r0.getI0(), r0.getI1(), r0.getI0(), c);
         } else {
            mergeDescending_AscendingAscending(l, r0.getI0(), r0.getI1(), r1.getI0(), r1.getI1(), r0.getI0(), c);
         }
      }
   }
   
   public static <T> void mergeAscending_AscendingAscending(List<T> l, int s0, int e0, int s1, int e1, int t0, Comparator<T> c) {
      T[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateComparableStorage(totalLength);
      i0 = s0;
      i1 = s1;
      it = 0;
      while (i0 < e0 && i1 < e1) {
         if (c.compare(l.get(i0), l.get(i1)) <= 0) {
            t[it] = l.get(i0);
            ++i0;
         } else {
            t[it] = l.get(i1);
            ++i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = l.get(i0 + i);
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = l.get(i1 + i);
         }
      }      
      for (int i = 0; i < totalLength; i++) {
         l.set(t0 + i, t[i]);
      }
   }

   public static <T> void mergeAscending_AscendingDescending(List<T> l, int s0, int e0, int s1, int e1, int t0, Comparator<T> c) {
      T[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateComparableStorage(totalLength);
      i0 = s0;
      i1 = e1 - 1;
      it = 0;
      while (i0 < e0 && i1 >= s1) {
         if (c.compare(l.get(i0), l.get(i1)) <= 0) {
            t[it] = l.get(i0);
            ++i0;
         } else {
            t[it] = l.get(i1);
            --i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = l.get(i0 + i);
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = l.get(i1 - i);
         }
      }      
      for (int i = 0; i < totalLength; i++) {
         l.set(t0 + i, t[i]);
      }
   }
   
   public static <T> void mergeAscending_DescendingDescending(List<T> l, int s0, int e0, int s1, int e1, int t0, Comparator<T> c) {
      T[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateComparableStorage(totalLength);
      i0 = e0 - 1;
      i1 = e1 - 1;
      it = 0;
      while (i0 >= s0 && i1 >= s1) {
         if (c.compare(l.get(i0), l.get(i1)) <= 0) {
            t[it] = l.get(i0);
            --i0;
         } else {
            t[it] = l.get(i1);
            --i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 >= s0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = l.get(i0 - i);
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = l.get(i1 - i);
         }
      }      
      for (int i = 0; i < totalLength; i++) {
         l.set(t0 + i, t[i]);
      }
   }

   public static <T> void mergeDescending_DescendingDescending(List<T> l, int s0, int e0, int s1, int e1, int t0, Comparator<T> c) {
      T[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateComparableStorage(totalLength);
      i0 = s0;
      i1 = s1;
      it = 0;
      while (i0 < e0 && i1 < e1) {
         if (c.compare(l.get(i0), l.get(i1)) >= 0) {
            t[it] = l.get(i0);
            ++i0;
         } else {
            t[it] = l.get(i1);
            ++i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = l.get(i0 + i);
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = l.get(i1 + i);
         }
      }      
      for (int i = 0; i < totalLength; i++) {
         l.set(t0 + i, t[i]);
      }
   }

   public static <T> void mergeDescending_DescendingAscending(List<T> l, int s0, int e0, int s1, int e1, int t0, Comparator<T> c) {
      T[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateComparableStorage(totalLength);
      i0 = s0;
      i1 = e1 - 1;
      it = 0;
      while (i0 < e0 && i1 >= s1) {
         if (c.compare(l.get(i0), l.get(i1)) >= 0) {
            t[it] = l.get(i0);
            ++i0;
         } else {
            t[it] = l.get(i1);
            --i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = l.get(i0 + i);
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = l.get(i1 - i);
         }
      }      
      for (int i = 0; i < totalLength; i++) {
         l.set(t0 + i, t[i]);
      }
   }
   
   public static <T> void mergeDescending_AscendingAscending(List<T> l, int s0, int e0, int s1, int e1, int t0, Comparator<T> c) {
      T[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateComparableStorage(totalLength);
      i0 = e0 - 1;
      i1 = e1 - 1;
      it = 0;
      while (i0 >= s0 && i1 >= s1) {
         if (c.compare(l.get(i0), l.get(i1)) >= 0) {
            t[it] = l.get(i0);
            --i0;
         } else {
            t[it] = l.get(i1);
            --i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 >= s0) {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = l.get(i0 - i);
         }
      } else {
         for (int i = 0; i < totalLength - it; i++) {
            t[it + i] = l.get(i1 - i);
         }
      }      
      for (int i = 0; i < totalLength; i++) {
         l.set(t0 + i, t[i]);
      }
   }
   
   ///////////////////////////////////////
   
   public static void mergeDescending(int[] a, int s0, int e0, int s1, int e1, int t0) {
      int[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateIntStorage(totalLength);
      i0 = s0;
      i1 = s1;
      it = 0;
      while (i0 < e0 && i1 < e1) {
         if (a[i0] >= a[i1]) {
            t[it] = a[i0];
            ++i0;
         } else {
            t[it] = a[i1];
            ++i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         System.arraycopy(a, i0, t, it, totalLength - it);
      } else {
         System.arraycopy(a, i1, t, it, totalLength - it);
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }
   
   public static void mergeDescending(long[] a, int s0, int e0, int s1, int e1, int t0) {
      long[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateLongStorage(totalLength);
      i0 = s0;
      i1 = s1;
      it = 0;
      while (i0 < e0 && i1 < e1) {
         if (a[i0] >= a[i1]) {
            t[it] = a[i0];
            ++i0;
         } else {
            t[it] = a[i1];
            ++i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         System.arraycopy(a, i0, t, it, totalLength - it);
      } else {
         System.arraycopy(a, i1, t, it, totalLength - it);
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }

   public static void mergeDescending(double[] a, int s0, int e0, int s1, int e1, int t0) {
      double[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateDoubleStorage(totalLength);
      i0 = s0;
      i1 = s1;
      it = 0;
      while (i0 < e0 && i1 < e1) {
         if (a[i0] >= a[i1]) {
            t[it] = a[i0];
            ++i0;
         } else {
            t[it] = a[i1];
            ++i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         System.arraycopy(a, i0, t, it, totalLength - it);
      } else {
         System.arraycopy(a, i1, t, it, totalLength - it);
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }
   
   public static <T> void mergeDescending(T[] a, int s0, int e0, int s1, int e1, int t0, Comparator<T> c) {
      T[] t;
      int   totalLength;
      int   i0;
      int   i1;
      int   it;
      
      totalLength = (e0 - s0) + (e1 - s1);
      t = ThreadLocalStorage.getOrCreateComparableStorage(totalLength);
      i0 = s0;
      i1 = s1;
      it = 0;
      while (i0 < e0 && i1 < e1) {
         if (c.compare(a[i0], a[i1]) >= 0) {
            t[it] = a[i0];
            ++i0;
         } else {
            t[it] = a[i1];
            ++i1;
         }
         ++it;
      }
      // FUTURE - copy straight to target below
      if (i0 < e0) {
         System.arraycopy(a, i0, t, it, totalLength - it);
      } else {
         System.arraycopy(a, i1, t, it, totalLength - it);
      }      
      System.arraycopy(t, 0, a, t0, totalLength);
   }
   
   ///////////////////////////////////////
   
   public static void display(int[] a, int i0, int i1) {
      for (int i = i0; i < i1; i++) {
         System.out.printf("%4d:%4d\n", i, a[i]);
      }
   }
   
   public static void display(int[] a, int i0) {
      display(a, i0, a.length);
   }
   
   public static void display(int[] a) {
      display(a, 0, a.length);
   }
   
   public static void display(double[] a, int i0, int i1) {
      for (int i = i0; i < i1; i++) {
         System.out.printf("%4d:%10f\n", i, a[i]);
      }
   }
   
   public static void display(double[] a, int i0) {
      display(a, i0, a.length);
   }
   
   public static void display(double[] a) {
      display(a, 0, a.length);
   }
   
   public static void main(String[] args) {
      String[] s = {"0", "2", "4", "1", "3", "5"};
      int[] a = {4, 2, 0, 5, 3, 1};
      
      for (int i = 0; i < s.length; i++) {
         System.out.println(s[i]);
      }
      System.out.println();
      merge(s, 0, 3, 3, 6, 0, StringSortIndexer.instance);
      for (int i = 0; i < s.length; i++) {
         System.out.println(s[i]);
      }
      
      System.out.println();
      for (int i = 0; i < a.length; i++) {
         System.out.println(a[i]);
      }
      System.out.println();
      mergeDescending(a, 0, 3, 3, 6, 0);
      for (int i = 0; i < a.length; i++) {
         System.out.println(a[i]);
      }
   }
}
