package eastcastle.util.sort.examples;

import java.util.concurrent.ThreadLocalRandom;

import eastcastle.util.sort.Order;
import eastcastle.util.sort.SortParameters;
import eastcastle.util.sort.Sorts;

/**
 * Demonstrates sorting using the static sorting methods in Sorts,
 * and exercising various sorting features. 
 */
public class StaticSortingInterfaceFeatures {
   public static void display(int[] a) {
      for (int i = 0; i < a.length; i++) {
         System.out.printf("%4d\t%12d\n", i, a[i]);
      }
   }
   
   private static int[] getRandomIntArray(int arraySize) {
      int[] a;
      
      a = new int[arraySize];
      for (int i = 0; i < a.length; i++) {
         a[i] = ThreadLocalRandom.current().nextInt();
      }
      return a;
   }
   
   public static void testSortFeatures(int arraySize) {
      int[] a;
      
      // Generate some random data (using simple api)
      a = getRandomIntArray(arraySize);
      System.out.printf("Before sorting:\n");
      display(a);
      
      // Sort using East Castle's static interface
      Sorts.sort(a);
      System.out.printf("After sorting:\n");
      display(a);
      
      // Sort in reverse
      Sorts.sort(a, SortParameters.DEFAULT.order(Order.DESCENDING));
      
      System.out.printf("After sorting in reverse:\n");
      display(a);
      
      // Sort the middle of the array in ascending order
      Sorts.sort(a, arraySize / 4, arraySize * 3 / 4);
      
      System.out.printf("After sorting the middle of the array in ascending order:\n");
      display(a);
      
      // Sort the middle of the array in descending order
      Sorts.sort(a, SortParameters.DEFAULT.order(Order.DESCENDING).i0(arraySize / 4).i1(arraySize * 3 / 4));
      
      System.out.printf("After sorting the middle of the array in descending order:\n");
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
         testSortFeatures(arraySize);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
