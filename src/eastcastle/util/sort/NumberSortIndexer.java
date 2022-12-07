package eastcastle.util.sort;

/**
 * Abstract parent class of all concrete Number SortIndexer implementations
 */
public abstract class NumberSortIndexer<N extends Number> implements SortIndexer<N> {
   protected NumberSortIndexer() {
   }
   
   @Override
   public boolean indicesAreSigned() {
      return true;
   }
   
   @Override
   public int sortIndex(N n) throws NotIndexSortableException {
      return n.intValue();
   }
}
