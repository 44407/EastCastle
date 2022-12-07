package eastcastle.util.sort;

public class IntegerSortIndexer extends NumberSortIndexer<Integer> {
   public static final IntegerSortIndexer instance = new IntegerSortIndexer();
   
   private IntegerSortIndexer() {      
   }
   
   @Override
   public int compare(Integer i0, Integer i1) {
      if (i0 < i1) {
         return -1;
      } else if (i0 > i1) {
         return 1;
      } else {
         return 0;
      }
   }
}
