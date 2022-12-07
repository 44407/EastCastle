package eastcastle.util.sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import eastcastle.util.ArrayUtil;
import eastcastle.util.CollectionUtil;

/**
 * Sort using built-in system parallel sorting routines. If no parallel sort is available, 
 * default to the serial system sort.
 */
public class ParallelSystemSort extends BaseSort {
   private static final String   name = "ParallelSystemSort";
   public static final ParallelSystemSort   instance = new ParallelSystemSort();
   
   public ParallelSystemSort() {
      super(name);
   }
   
   @Override
   public void sort(int[] a, SortParameters sortParameters) {
      sortParameters = sortParameters.getDefaultsFor(a);
      Arrays.parallelSort(a, sortParameters.getI0(), sortParameters.getI1());
      if (sortParameters.getOrder() == Order.DESCENDING) {
         ArrayUtil.reverse(a, sortParameters.getI0(), sortParameters.getI1());
      }
   }
   
   @Override
   public void sort(long[] a, SortParameters sortParameters) {
      sortParameters = sortParameters.getDefaultsFor(a);
      Arrays.parallelSort(a, sortParameters.getI0(), sortParameters.getI1());
      if (sortParameters.getOrder() == Order.DESCENDING) {
         ArrayUtil.reverse(a, sortParameters.getI0(), sortParameters.getI1());
      }
   }

   @Override
   public void sort(double[] a, SortParameters sortParameters) {
      sortParameters = sortParameters.getDefaultsFor(a);
      Arrays.parallelSort(a, sortParameters.getI0(), sortParameters.getI1());
      if (sortParameters.getOrder() == Order.DESCENDING) {
         ArrayUtil.reverse(a, sortParameters.getI0(), sortParameters.getI1());
      }
   }
   
   //////////////////
   // Generic Array
   
   @Override
   public <T> void sort(T[] a, SortParameters sortParameters, Comparator<T> comparator) {
      sortParameters = sortParameters.getDefaultsFor(a);
      Arrays.parallelSort(a, sortParameters.getI0(), sortParameters.getI1(), comparator);
      if (sortParameters.getOrder() == Order.DESCENDING) {
         ArrayUtil.reverse(a, sortParameters.getI0(), sortParameters.getI1());
      }
   }
   
   /////////
   // List
   
   @Override
   public <T> void sort(List<T> l, SortParameters sortParameters, Comparator<T> comparator) {
      sortParameters = sortParameters.getDefaultsFor(l);
      // No existing Collections.parallelSort(); use serial
      if (sortParameters.getI0() == 0 && sortParameters.getI1() == l.size()) {
         Collections.sort(l, comparator);
      } else {
         Collections.sort(l.subList(sortParameters.getI0(), sortParameters.getI1()), comparator);
      }
      if (sortParameters.getOrder() == Order.DESCENDING) {
         CollectionUtil.reverse(l, sortParameters.getI0(), sortParameters.getI1());
      }
   }
}
