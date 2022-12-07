package eastcastle.util.sort;

public class ShortSortIndexer extends NumberSortIndexer<Short> {
   public static final ShortSortIndexer   instance = new ShortSortIndexer();
   
   private ShortSortIndexer() {      
   }
   
   @Override
   public int compare(Short s0, Short s1) {
      if (s0 < s1) {
         return -1;
      } else if (s0 > s1) {
         return 1;
      } else {
         return 0;
      }
   }
}
