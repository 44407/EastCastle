package eastcastle.util.sort.analysis;

import java.util.List;

public class ArrayDisplay {
   public static void display(int[] a) {
      for (int i = 0; i < a.length; i++) {
         System.out.printf("%d\t%d\n", i, a[i]);
      }
   }
   
   public static void display(long[] a) {
      for (int i = 0; i < a.length; i++) {
         System.out.printf("%d\t%d\n", i, a[i]);
      }
   }
   
   public static void display(float[] a) {
      for (int i = 0; i < a.length; i++) {
         System.out.printf("%d\t%f\n", i, a[i]);
      }
   }
   
   public static void display(double[] a) {
      for (int i = 0; i < a.length; i++) {
         System.out.printf("%d\t%f\n", i, a[i]);
      }
   }
   
   public static <T> void display(T[] a) {
      for (int i = 0; i < a.length; i++) {
         System.out.printf("%d\t%s\n", i, a[i]);
      }
   }
   
   public static <T> void display(List<T> l) {
      for (int i = 0; i < l.size(); i++) {
         System.out.printf("%d\t%s\n", i, l.get(i));
      }
   }      
}
