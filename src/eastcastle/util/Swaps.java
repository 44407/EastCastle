package eastcastle.util;

public class Swaps {
   public static void swap(byte[] a, int i0, int i1) {
      byte   tmp;
      
      tmp = a[i0];
      a[i0] = a[i1];
      a[i1] = tmp;
   }

   public static void swap(short[] a, int i0, int i1) {
      short   tmp;
      
      tmp = a[i0];
      a[i0] = a[i1];
      a[i1] = tmp;
   }

   public static void swap(int[] a, int i0, int i1) {
      int   tmp;
      
      tmp = a[i0];
      a[i0] = a[i1];
      a[i1] = tmp;
   }

   public static void swap(long[] a, int i0, int i1) {
      long   tmp;
      
      tmp = a[i0];
      a[i0] = a[i1];
      a[i1] = tmp;
   }

   public static void swap(float[] a, int i0, int i1) {
      float   tmp;
      
      tmp = a[i0];
      a[i0] = a[i1];
      a[i1] = tmp;
   }

   public static void swap(double[] a, int i0, int i1) {
      double   tmp;
      
      tmp = a[i0];
      a[i0] = a[i1];
      a[i1] = tmp;
   }

   public static <T> void swap(T[] a, int i0, int i1) {
      T   tmp;
      
      tmp = a[i0];
      a[i0] = a[i1];
      a[i1] = tmp;
   }
}
