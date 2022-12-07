package eastcastle.util.sort;

import java.util.List;

/**
 * Implemented by classes that, given a class, provide a Sort implementation appropriate for that class.
 */
public interface SortProvider {
   /**
    * Return a Sort for the given class.
    * The class passed in must be the class of the elements being sorted, not the containing array 
    * @param <T>
    * @param c the class of the elements being sorted (not the containing array)
    * @return
    */
   <T> Sort getSortForArrayOf(Class<T> c);
   
   /**
    * Return a Sort for the given class.
    * The class passed in must be the class of the elements being sorted, not the containing list 
    * @param <T>
    * @param c the class of the elements being sorted (not the containing array or list)
    * @return
    */
   default <T> Sort getSortForListOf(Class<T> c) {
      return getSortForArrayOf(c);
   }

   /**
    * Return a Sort for int[]
    * @param a the array
    * @return
    */
   default Sort getSortFor(int[] a) {
      return getSortForArrayOf(int.class);
   }
   
   /**
    * Return a Sort for long[]
    * @param a the array
    * @return
    */
   default Sort getSortFor(long[] a) {
      return getSortForArrayOf(long.class);
   }   
   
   /**
    * Return a Sort for double[]
    * @param a the array
    * @return
    */
   default Sort getSortFor(double[] a) {
      return getSortForArrayOf(double.class);
   }   
   
   /**
    * Return a Sort for String[]
    * @param a the array
    * @return
    */
   default Sort getSortFor(String[] a) {
      return getSortForArrayOf(String.class);
   }   
   
   /**
    * Return a Sort for List<T>
    * @param a the list
    * @return
    */
   default <T> Sort getSortFor(List<T> l) {
      if (l.size() == 0) {
         // any type will do here; alternatively, could consider throwing an exception
         return getSortForListOf(String.class); 
      } else {
         return getSortForListOf(l.get(0).getClass());
      }
   }   
}
