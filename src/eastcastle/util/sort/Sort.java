package eastcastle.util.sort;

import java.util.Comparator;
import java.util.List;

/**
 * Interface to a sorting algorithm
 */
public interface Sort {
   // FUTURE - complete all primitive types
   String getSortName();
   
   // primitives
   
   void sort(int[] a, SortParameters sortParameters);
   void sort(int[] a, int i0, int i1);
   void sort(int[] a);
   
   void sort(long[] a, SortParameters sortParameters);
   void sort(long[] a, int i0, int i1);
   void sort(long[] a);
      
   void sort(double[] a, SortParameters sortParameters);
   void sort(double[] a, int i0, int i1);
   void sort(double[] a);
   
   // Generic array, explicit Comparator
   
   <T> void sort(T[] a, SortParameters sortParameters, Comparator<T> comparator);
   <T> void sort(T[] a, int i0, int i1, Comparator<T> comparator);
   <T> void sort(T[] a, Comparator<T> comparator);
   
   // Comparable array; the sort will pick a Comparator (e.g. Comparator.naturalOrder())
      
   <T extends Comparable<? super T>> void sort(T[] a, SortParameters sortParameters);
   <T extends Comparable<? super T>> void sort(T[] a, int i0, int i1);
   <T extends Comparable<? super T>> void sort(T[] a);
   
   // Generic List, explicit Comparator
   
   <T> void sort(List<T> l, SortParameters sortParameters, Comparator<T> comparator);
   <T> void sort(List<T> l, int i0, int i1, Comparator<T> comparator);
   <T> void sort(List<T> l, Comparator<T> comparator);
   
   // Comparable List
   
   <T extends Comparable<? super T>> void sort(List<T> l, SortParameters sortParameters);
   <T extends Comparable<? super T>> void sort(List<T> l, int i0, int i1);
   <T extends Comparable<? super T>> void sort(List<T> l);
}
