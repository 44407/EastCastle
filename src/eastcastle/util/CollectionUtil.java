package eastcastle.util;

import java.util.List;

public class CollectionUtil {
   private static void sanityCheckListIndices(int length, int i0, int i1) {
      if (i0 < 0) {
         throw new IndexOutOfBoundsException("i0 < 0");
      }
      if (i1 < i0) {
         throw new IndexOutOfBoundsException("i1 < i0");
      }
      if (i1 > length) {
         throw new IndexOutOfBoundsException("i1 > length");
      }
   }
   
   public static <T> void reverse(List<T> l, int i0, int i1) {
      int   j0;
      int   j1;
      
      sanityCheckListIndices(l.size(), i0, i1);
      j0 = i0;
      j1 = i1 - 1;
      while (j0 < j1) {
         T   tmp;
         
         tmp = l.get(j0);
         l.set(j0, l.get(j1));
         l.set(j1, tmp);
         ++j0;
         --j1;
      }
   }   
}
