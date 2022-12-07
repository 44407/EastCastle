package eastcastle.util.sort;

import java.util.Comparator;

/**
 * Implemented by classes that can order a target class T using an integer index (not merely comparison).
 * The index need not totally order all elements according to their comparison, but the index must be monotonically increasing
 * with respect to the total order imposed by the Comparator implementation.
 */
public interface SortIndexer<T> extends Comparator<T> {
   /**
    * Indicates whether or not the indices returned by this SortIndexer should be interpreted as signed integers
    * @return whether or not the indices returned by this SortIndexer should be interpreted as signed integers
    */
   boolean indicesAreSigned();
   /**
    * Return the sort index for the given object
    * @return the sort index for the given object
    */
   int sortIndex(T t) throws NotIndexSortableException;
}
