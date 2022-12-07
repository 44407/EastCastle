package eastcastle.util.sort;

public class DoubleSortIndexer extends NumberSortIndexer<Double> {
   public static final DoubleSortIndexer instance = new DoubleSortIndexer();

   private DoubleSortIndexer() {      
   }
   
   @Override
   public int compare(Double d0, Double d1) {
      if (d0 < d1) {
         return -1;
      } else if (d0 > d1) {
         return 1;
      } else {
         return 0;
      }
   }
}
