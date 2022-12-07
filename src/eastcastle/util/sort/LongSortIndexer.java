package eastcastle.util.sort;

public class LongSortIndexer extends NumberSortIndexer<Long> {
   public static final LongSortIndexer   instance = new LongSortIndexer();
   
   private LongSortIndexer() {      
   }
   
   @Override
   public int compare(Long l0, Long l1) {
      if (l0 < l1) {
         return -1;
      } else if (l0 > l1) {
         return 1;
      } else {
         return 0;
      }
   }
}
