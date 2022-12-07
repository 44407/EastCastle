package eastcastle.util.sort;

import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

/**
 * Base Sort implementation. Simplifies implementation of a sorting algorithm by chaining simplifying
 * methods into the fundamental method. 
 */
public abstract class BaseIndexSort extends BaseSort implements IndexSort {
   protected final SortIndexerProvider sortIndexProvider;
   
   public BaseIndexSort(String name, SortIndexerProvider sortIndexerProvider) {
      super(name);
      this.sortIndexProvider = sortIndexerProvider;
   }

   public BaseIndexSort(String name) {
      this(name, Sorts.getDefaultSortIndexerProvider());
   }
   
   //////////////////////
   // Comparable arrays
   
   @Override
   @SuppressWarnings("unchecked")
   public <T extends Comparable<? super T>> void sort(T[] a, SortParameters sortParameters) {
      if (a.length > 1) {
         SortIndexer<T> sortIndexer;
         
         sortIndexer = sortIndexProvider.getSortIndexer(a[0].getClass());
         if (sortIndexer != null) {
            sort(a, sortParameters, sortIndexer);
         } else {
            sort(a, sortParameters, Comparator.naturalOrder());
         }
      }
   }
   
   /////////////////////////////////////////
   // Generic array, explicit SortIndexer
   
   @Override
   public <T> void sort(T[] a, int i0, int i1, SortIndexer<T> sortIndexer) {
      sort(a, new SortParameters(i0, i1, Order.ASCENDING), sortIndexer);
   }
   
   @Override
   public <T> void sort(T[] a, SortIndexer<T> sortIndexer) {
      sort(a, new SortParameters(0, a.length, Order.ASCENDING), sortIndexer);
   }

   ///////////////////////////////////////////////////////////////////////
   // IndexSortable; we will find an IndexSorter from the SorterProvider
   
   @SuppressWarnings("unchecked")
   @Override
   public <T extends IndexSortable<? super T>> void sort(T[] a, SortParameters sortParameters) {
      sort(a, sortParameters, IndexSortableSortIndexer.instance);
   }
   
   @Override
   public <T extends IndexSortable<? super T>> void sort(T[] a, int i0, int i1) {
      sort(a, new SortParameters(i0, i1, Order.ASCENDING));
   }
   
   @Override
   public <T extends IndexSortable<? super T>> void sort(T[] a) {
      sort(a, new SortParameters(0, a.length, Order.ASCENDING));
   }

   ////////////////////////////////////////
   // Generic List, explicit SortIndexer
   
   public <T> void sort(List<T> l, SortParameters sortParameters, SortIndexer<T> sortIndexer) {
      @SuppressWarnings("unchecked")
      T[] a = (T[])new Comparable[l.size()];
      for (int i = 0; i < a.length; i++) {
         a[i] = l.get(i);
      }
      sort(a, sortParameters, sortIndexer);
      ListIterator<T> i = l.listIterator();
      for (int pos = 0, alen = a.length; pos < alen; pos++) {
         i.next();
         i.set(a[pos]);
      }
   }
   
   @Override
   public <T> void sort(List<T> l, int i0, int i1, SortIndexer<T> sortIndexer) {
      sort(l, new SortParameters(i0, i1, Order.ASCENDING), sortIndexer);
   }
   
   @Override
   public <T> void sort(List<T> l, SortIndexer<T> sortIndexer) {
      sort(l, new SortParameters(0, l.size(), Order.ASCENDING), sortIndexer);
   }
   
   //////////////////////////////////
   // Comparable/IndexSortable List
   
   // Due to type erasure; we must handle both Comparable and IndexSortable here
   // BaseSort has already implemented Comparable. We check and use IndexSortable if possible 
   // FUTURE: consider checking if the elements are IndexSortable
   //         and throwing an exception if we can't find a SortIndexer for that case
   
   @SuppressWarnings("unchecked")
   @Override
   public <T extends Comparable<? super T>> void sort(List<T> l, SortParameters sortParameters) {
      if (l.size() > 1) {
         SortIndexer<T> sortIndexer;
         
         sortIndexer = sortIndexProvider.getSortIndexer(l.get(sortParameters.getI0()).getClass());
         if (sortIndexer != null) {
            sort(l, sortParameters, sortIndexer);
         } else {
            super.sort(l, sortParameters);
         }
      }
   }   
}
