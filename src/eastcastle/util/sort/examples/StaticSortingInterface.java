package eastcastle.util.sort.examples;

import java.util.concurrent.ThreadLocalRandom;

import eastcastle.util.sort.Sorts;

/**
 * Demonstrates sorting using one of the static sorting methods in Sorts 
 */
public class StaticSortingInterface {
   public static void display(int[] a) {
      for (int i = 0; i < a.length; i++) {
         System.out.printf("%4d\t%12d\n", i, a[i]);
      }
   }
   
   public static void testSimpleSort(int arraySize) {
      int[] a;
      
      // Generate some random data (using simple api)
      a = new int[arraySize];
      for (int i = 0; i < a.length; i++) {
         a[i] = ThreadLocalRandom.current().nextInt();
      }
      System.out.printf("Before sorting:\n");
      display(a);
      
      // Sort using East Castle's static interface
      Sorts.sort(a);
      
      System.out.printf("After sorting:\n");
      display(a);
   }
   
   public static void main(String[] args) {
      try {
         int   arraySize;
         
         if (args.length > 0) {
            arraySize = Integer.parseInt(args[0]);
         } else {
            arraySize = 16;
         }
         testSimpleSort(arraySize);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
