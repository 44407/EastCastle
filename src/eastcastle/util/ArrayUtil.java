package eastcastle.util;

public class ArrayUtil {
   private static void sanityCheckArrayIndices(int length, int i0, int i1) {
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
   
   public static void reverse(byte[] a, int i0, int i1) {
      int   j0;
      int   j1;
      
      sanityCheckArrayIndices(a.length, i0, i1);
      j0 = i0;
      j1 = i1 - 1;
      while (j0 < j1) {
         byte   tmp;
         
         tmp = a[j0];
         a[j0] = a[j1];
         a[j1] = tmp;
         ++j0;
         --j1;
      }
   }
   
   public static void reverse(short[] a, int i0, int i1) {
      int   j0;
      int   j1;
      
      sanityCheckArrayIndices(a.length, i0, i1);
      j0 = i0;
      j1 = i1 - 1;
      while (j0 < j1) {
         short   tmp;
         
         tmp = a[j0];
         a[j0] = a[j1];
         a[j1] = tmp;
         ++j0;
         --j1;
      }
   }
   
   public static void reverse(int[] a, int i0, int i1) {
      int   j0;
      int   j1;
      
      sanityCheckArrayIndices(a.length, i0, i1);
      j0 = i0;
      j1 = i1 - 1;
      while (j0 < j1) {
         int   tmp;
         
         tmp = a[j0];
         a[j0] = a[j1];
         a[j1] = tmp;
         ++j0;
         --j1;
      }
   }
   
   public static void reverse(long[] a, int i0, int i1) {
      int   j0;
      int   j1;
      
      sanityCheckArrayIndices(a.length, i0, i1);
      j0 = i0;
      j1 = i1 - 1;
      while (j0 < j1) {
         long   tmp;
         
         tmp = a[j0];
         a[j0] = a[j1];
         a[j1] = tmp;
         ++j0;
         --j1;
      }
   }
   
   public static void reverse(float[] a, int i0, int i1) {
      int   j0;
      int   j1;
      
      sanityCheckArrayIndices(a.length, i0, i1);
      j0 = i0;
      j1 = i1 - 1;
      while (j0 < j1) {
         float   tmp;
         
         tmp = a[j0];
         a[j0] = a[j1];
         a[j1] = tmp;
         ++j0;
         --j1;
      }
   }
   
   public static void reverse(double[] a, int i0, int i1) {
      int   j0;
      int   j1;
      
      sanityCheckArrayIndices(a.length, i0, i1);
      j0 = i0;
      j1 = i1 - 1;
      while (j0 < j1) {
         double   tmp;
         
         tmp = a[j0];
         a[j0] = a[j1];
         a[j1] = tmp;
         ++j0;
         --j1;
      }
   }
   
   public static <T> void reverse(T[] a, int i0, int i1) {
      int   j0;
      int   j1;
      
      sanityCheckArrayIndices(a.length, i0, i1);
      j0 = i0;
      j1 = i1 - 1;
      while (j0 < j1) {
         T   tmp;
         
         tmp = a[j0];
         a[j0] = a[j1];
         a[j1] = tmp;
         ++j0;
         --j1;
      }
   }   
   
   public static void display(double[] a) {
      for (int i = 0; i < a.length; i++) {
         System.out.printf("%d\t%f\n", i, a[i]);
      }
   }
   
   public static void display(int[] a) {
      for (int i = 0; i < a.length; i++) {
         System.out.printf("%d\t%d\n", i, a[i]);
      }
   }
}
