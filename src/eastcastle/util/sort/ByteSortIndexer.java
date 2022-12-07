package eastcastle.util.sort;

public class ByteSortIndexer extends NumberSortIndexer<Byte> {
   public static final ByteSortIndexer   instance = new ByteSortIndexer();
   
   private ByteSortIndexer() {      
   }
   
   @Override
   public int compare(Byte b0, Byte b1) {
      if (b0 < b1) {
         return -1;
      } else if (b0 > b1) {
         return 1;
      } else {
         return 0;
      }
   }
}
