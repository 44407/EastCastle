package eastcastle.util.sort;

/**
 * SortIndexer implementation that orders classes implementing IndexSortable.
 * This class simplifies implementation by allowing us to use SortIndexer functionality
 * to sort IndexSortable classes. That is, we avoid specific methods dealing with
 * IndexSortable classes.
 *
 * @param <T>
 */
public class IndexSortableSortIndexer<T extends IndexSortable<T>> implements SortIndexer<T> {
   @SuppressWarnings("rawtypes")
   public static final IndexSortableSortIndexer   instance = new IndexSortableSortIndexer();
   
   private IndexSortableSortIndexer() {
   }
   
   @Override
   public int compare(T o0, T o1) {
      return o0.compareTo(o1);
   }

   @Override
   public boolean indicesAreSigned() {
      return true;
   }
   
   @Override
   public int sortIndex(T is) throws NotIndexSortableException {
      return ((IndexSortable<T>)is).sortIndex();
   }
}
