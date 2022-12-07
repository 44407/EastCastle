package eastcastle.util.sort;

/**
 * Implemented by classes that can be sorted using an integer index (not merely comparison).
 * The index need not be unique, but it must impose an order that is monotonically increasing
 * with respect to the Comparable sort order. 
 */
public interface IndexSortable<T> extends Comparable<T> {
   /**
    * Return the sort index for this object.
    * The index need not be unique, but it must impose an order that is monotonically increasing
    * with respect to the Comparable sort order.
    * @return the sort index for this object
    */
   int sortIndex();
}
