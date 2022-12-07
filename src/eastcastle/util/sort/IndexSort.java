package eastcastle.util.sort;

import java.util.List;

/**
 * Interface to an index sorting algorithm. An index sorting algorithm is capable ordering 
 * elements using an integer index (not merely comparison).
 * The index need not totally order all elements according to their comparison, 
 * but the index must be monotonically increasing with respect to the total order. 
 * As a result, IndexSort implementations must be able to order elements that have identical
 * indices, but are not identical in the order.
 */
public interface IndexSort extends Sort {
   // FUTURE - complete all primitive types
   
   // Generic array, explicit SortIndexer
   
   <T> void sort(T[] a, SortParameters sortParameters, SortIndexer<T> sortIndexer);
   <T> void sort(T[] a, int i0, int i1, SortIndexer<T> sortIndexer);
   <T> void sort(T[] a, SortIndexer<T> sortIndexer);
   
   // IndexSortable array; the sort will pick a SortIndexer (typically from the SortIndexerProvider)
   
   <T extends IndexSortable<? super T>> void sort(T[] a, SortParameters sortParameters);
   <T extends IndexSortable<? super T>> void sort(T[] a, int i0, int i1);
   <T extends IndexSortable<? super T>> void sort(T[] a);
   
   // Generic List, explicit SortIndexer
   
   <T> void sort(List<T> l, SortParameters sortParameters, SortIndexer<T> sortIndexer);
   <T> void sort(List<T> l, int i0, int i1, SortIndexer<T> sortIndexer);
   <T> void sort(List<T> l, SortIndexer<T> sortIndexer);
   
   // IndexSortable List; the sort will pick a SortIndexer (typically from the SortIndexerProvider)
   
   // Type erasure prevents declaration of this type, we must override
   //<T extends IndexSortable<? super T>> void sort(List<T> l, SortParameters sortParameters);
   //<T extends IndexSortable<? super T>> void sort(List<T> l, int i0, int i1);
   //<T extends IndexSortable<? super T>> void sort(List<T> l);
}
