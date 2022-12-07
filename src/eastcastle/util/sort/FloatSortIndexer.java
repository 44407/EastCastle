package eastcastle.util.sort;

public class FloatSortIndexer extends NumberSortIndexer<Float> {
   public static final FloatSortIndexer instance = new FloatSortIndexer();

   private FloatSortIndexer() {      
   }
   
   @Override
   public int compare(Float f0, Float f1) {
      if (f0 < f1) {
         return -1;
      } else if (f0 > f1) {
         return 1;
      } else {
         return 0;
      }
   }
}
