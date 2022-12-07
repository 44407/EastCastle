package eastcastle.util.sort;

/**
 * Implemented by classes capable of providing an SortIndexer given a class. 
 */
public interface SortIndexerProvider {
   /**
    * 
    * @param <T>
    * @param c the class of the elements being indexed (not the containing array or list)
    * @return
    */
   <T extends Comparable<? super T>> SortIndexer<T> getSortIndexer(Class<T> c); 
}
