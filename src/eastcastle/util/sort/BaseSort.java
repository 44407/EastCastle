package eastcastle.util.sort;

import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

/**
 * Base Sort implementation. Simplifies implementation of a sorting algorithm by chaining simplifying
 * methods into the fundamental method. 
 */
public abstract class BaseSort implements Sort {
   private final String sortName;
   
   public BaseSort(String sortName) {
      this.sortName = sortName;
   }
   
   @Override
   public String getSortName() {
      return sortName;
   }
   
   /////////////////////
   // Primitive Arrays
   
   @Override
   public void sort(int[] a, int i0, int i1) {
      sort(a, new SortParameters(i0, i1, Order.ASCENDING));
   }

   @Override
   public void sort(int[] a) {
      sort(a, new SortParameters(0, a.length, Order.ASCENDING));
   }
   
   @Override
   public void sort(long[] a, int i0, int i1) {
      sort(a, new SortParameters(i0, i1, Order.ASCENDING));
   }

   @Override
   public void sort(long[] a) {
      sort(a, new SortParameters(0, a.length, Order.ASCENDING));
   }
   
   @Override
   public void sort(double[] a, int i0, int i1) {
      sort(a, new SortParameters(i0, i1, Order.ASCENDING));
   }

   @Override
   public void sort(double[] a) {
      sort(a, new SortParameters(0, a.length, Order.ASCENDING));
   }
   
   /////////////////////////////////////////
   // Generic array, explicit Comparator
   
   public <T> void sort(T[] a, int i0, int i1, Comparator<T> comparator) {
      sort(a, new SortParameters(i0, i1, Order.ASCENDING), comparator);
   }
   
   public <T> void sort(T[] a, Comparator<T> comparator) {
      sort(a, new SortParameters(0, a.length, Order.ASCENDING), comparator);
   }
   
   //////////////////////
   // Comparable arrays
   
   @Override
   public <T extends Comparable<? super T>> void sort(T[] a, SortParameters sortParameters) {
      sort(a, sortParameters, Comparator.naturalOrder());
   }
   
   @Override
   public <T extends Comparable<? super T>> void sort(T[] a, int i0, int i1) {
      sort(a, new SortParameters(i0, i1, Order.ASCENDING));
   }

   @Override
   public <T extends Comparable<? super T>> void sort(T[] a) {
      sort(a, new SortParameters(0, a.length, Order.ASCENDING));
   }
   
   ///////////////////////////////////////
   // Generic Lists, explicit Comparator
   
   @Override
   public <T> void sort(List<T> l, SortParameters sortParameters, Comparator<T> comparator) {
      @SuppressWarnings("unchecked")
      T[] a = (T[])new Comparable[l.size()];
      for (int i = 0; i < a.length; i++) {
         a[i] = l.get(i);
      }
      sort(a, sortParameters, comparator);
      ListIterator<T> i = l.listIterator();
      for (int pos = 0, alen = a.length; pos < alen; pos++) {
         i.next();
         i.set(a[pos]);
      }
   }   
   
   public <T> void sort(List<T> l, int i0, int i1, Comparator<T> comparator) {
      sort(l, new SortParameters(i0, i1, Order.ASCENDING), comparator);
   }
   
   public <T> void sort(List<T> l, Comparator<T> comparator) {
      sort(l, new SortParameters(0, l.size(), Order.ASCENDING), comparator);
   }
   
   
   /////////////////////
   // Comparable Lists
   
   @Override
   public <T extends Comparable<? super T>> void sort(List<T> l, SortParameters sortParameters) {
      sort(l, sortParameters, Comparator.naturalOrder());
   }
   
   @Override
   public <T extends Comparable<? super T>> void sort(List<T> l, int i0, int i1) {
      sort(l, new SortParameters(i0, i1, Order.ASCENDING));
   }

   @Override
   public <T extends Comparable<? super T>> void sort(List<T> l) {
      sort(l, new SortParameters(0, l.size(), Order.ASCENDING));
   }   
}
