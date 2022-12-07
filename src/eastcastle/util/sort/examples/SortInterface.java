package eastcastle.util.sort.examples;

import java.util.concurrent.ThreadLocalRandom;

import eastcastle.util.sort.MutableSortProvider;
import eastcastle.util.sort.RadJSort;
import eastcastle.util.sort.Sort;
import eastcastle.util.sort.Sorts;
import eastcastle.util.sort.SystemSort;

/**
 * Demonstrates sorting using the Sort interface 
 */
public class SortInterface {
   public static void display(int[] a) {
      for (int i = 0; i < a.length; i++) {
         System.out.printf("%4d\t%12d\n", i, a[i]);
      }
   }
   
   public static int[] getRandomIntArray(int arraySize) {
      int[] a;
      
      // Generate some random data (using simple api)
      a = new int[arraySize];
      for (int i = 0; i < a.length; i++) {
         a[i] = ThreadLocalRandom.current().nextInt();
      }
      return a;
   }
   
   public static void testSimpleSortWithSort(int arraySize, Sort sort) {
      int[] a;

      System.out.printf("Using sort %s\n", sort.getSortName());
      a = getRandomIntArray(arraySize);
      System.out.printf("Before sorting:\n");
      display(a);
      
      sort.sort(a);
      
      System.out.printf("After sorting:\n");
      display(a);
   }
   
   public static void testSimpleSort(int arraySize) {
      MutableSortProvider  sortProvider;

      // First test with the default sort
      testSimpleSortWithSort(arraySize, Sorts.getSortForArrayOf(int.class));
      
      // Now user a sort provider; this will use the provider's default sort, set to RadJ below
      sortProvider = new MutableSortProvider(RadJSort.defaultInstance);
      testSimpleSortWithSort(arraySize, sortProvider.getSortForArrayOf(int.class));
      
      // Again use a sort provider; this will use the explicit mapping stored below
      sortProvider.put(int.class, SystemSort.instance);
      testSimpleSortWithSort(arraySize, sortProvider.getSortForArrayOf(int.class));
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
