package eastcastle.util.sort.examples;

import eastcastle.util.sort.Sorts;

/**
 * Demonstrates sorting using one of the static sorting methods in Sorts 
 */
public class HelloSorting {
   public static void main(String[] args) {
      int[] a = new int[] {5, 0, -7, 1};
      
      // Sort using East Castle's static interface
      Sorts.sort(a);
      System.out.printf("%d\t%d\t%d\t%d\n", a[0], a[1], a[2], a[3]);
   }
}
