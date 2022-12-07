package eastcastle.util.sort.examples;

import java.util.concurrent.ThreadLocalRandom;

import eastcastle.util.sort.ParallelSystemSort;
import eastcastle.util.sort.RadJSort;
import eastcastle.util.sort.Sort;
import eastcastle.util.sort.SystemSort;

/**
 * Demonstrates sorting using various sorting implementations passed in 
 * to a common method that is unaware of the implementation 
 */
public class SortImplementationsExample {
   public static void display(int[] a) {
      for (int i = 0; i < a.length; i++) {
         System.out.printf("%4d\t%12d\n", i, a[i]);
      }
   }
   
   public static void testSortImplementations(int arraySize) {
      sortWithImplementation(RadJSort.defaultInstance, arraySize);
      sortWithImplementation(SystemSort.instance, arraySize);
      sortWithImplementation(ParallelSystemSort.instance, arraySize);
   }
   
   public static void sortWithImplementation(Sort sort, int arraySize) {
      int[] a;
      
      System.out.printf("Sorting using %s\n", sort);
      
      // Generate some random data (using simple api)
      a = new int[arraySize];
      for (int i = 0; i < a.length; i++) {
         a[i] = ThreadLocalRandom.current().nextInt();
      }
      System.out.printf("Before sorting:\n");
      display(a);
      
      // Sort using the implementation passed in
      sort.sort(a);
      
      System.out.printf("After sorting:\n");
      display(a);
      System.out.println();
   }
   
   public static void main(String[] args) {
      try {
         int   arraySize;
         
         if (args.length > 0) {
            arraySize = Integer.parseInt(args[0]);
         } else {
            arraySize = 16;
         }
         testSortImplementations(arraySize);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
