package eastcastle.util.sort;

import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * Provides convenient static access to sorting functionality.
 * </p>
 * 
 * <p>
 * Also, provides a central location for SortProvider functionality.
 * </p>
 *
 */
public class Sorts {
   private static final SortProvider defaultSortProvider;
   private static final SortIndexerProvider defaultSortIndexerProvider;
   
   private static final Sort  intArraySort;
   private static final Sort  longArraySort;
   private static final Sort  doubleArraySort;
   
   static {
      // FUTURE - make defaultSortProvider configurable by property      
      defaultSortProvider = DefaultSortProvider.instance;
      defaultSortIndexerProvider = DefaultSortIndexerProvider.instance;
      
      intArraySort = defaultSortProvider.getSortForArrayOf(int.class);
      longArraySort = defaultSortProvider.getSortForArrayOf(long.class);
      doubleArraySort = defaultSortProvider.getSortForArrayOf(double.class);
   }
   
   //////////////////////
   // Default providers
   
   public static SortProvider getDefaultSortProvider() {
      return defaultSortProvider;
   }
   
   public static SortIndexerProvider getDefaultSortIndexerProvider() {
      return defaultSortIndexerProvider;
   }
   
   ///////////////////////////////
   // SortProvider static mirror

   public static <T> Sort getSortForArrayOf(Class<T> _class) {
      return defaultSortProvider.getSortForArrayOf(_class);
   }

   public static <T> Sort getSortForListOf(Class<T> _class) {
      return defaultSortProvider.getSortForListOf(_class);
   }
   
   /**
    * Return a Sort for int[]
    * @param a the array
    * @return
    */
   public static Sort getSortFor(int[] a) {
      return defaultSortProvider.getSortFor(a);
   }
   
   /**
    * Return a Sort for long[]
    * @param a the array
    * @return
    */
   public static Sort getSortFor(long[] a) {
      return defaultSortProvider.getSortFor(a);
   }   
   
   /**
    * Return a Sort for double[]
    * @param a the array
    * @return
    */
   public static Sort getSortFor(double[] a) {
      return defaultSortProvider.getSortFor(a);
   }   
   
   /**
    * Return a Sort for String[]
    * @param a the array
    * @return
    */
   public static Sort getSortFor(String[] a) {
      return defaultSortProvider.getSortFor(a);
   }   
   
   /**
    * Return a Sort for List<T>
    * @param a the list
    * @return
    */
   public static <T> Sort getSortFor(List<T> l) {
      return defaultSortProvider.getSortFor(l);
   }   
   

   ///////////////////////
   // Sort static mirror
   
   // FUTURE - complete primitives
   
   // int
   
   public static void sort(int[] a, SortParameters sortParameters) {
      intArraySort.sort(a, sortParameters);
   }
   
   public static void sort(int[] a, int i0, int i1) {
      intArraySort.sort(a, i0, i1);
   }

   public static void sort(int[] a) {
      intArraySort.sort(a);
   }   
   
   // long
   
   public static void sort(long[] a, SortParameters sortParameters) {
      longArraySort.sort(a, sortParameters);
   }
   
   public static void sort(long[] a, int i0, int i1) {
      longArraySort.sort(a, i0, i1);
   }

   public static void sort(long[] a) {
      longArraySort.sort(a);
   }   
   
   // double

   public static void sort(double[] a, SortParameters sortParameters) {
      doubleArraySort.sort(a, sortParameters);
   }
   
   public static void sort(double[] a, int i0, int i1) {
      doubleArraySort.sort(a, i0, i1);
   }

   public static void sort(double[] a) {
      doubleArraySort.sort(a);
   }   
   
   // Generic array, explicit Comparator
   
   public static <T> void sort(T[] a, SortParameters sortParameters, Comparator<T> comparator) {
      if (a.length > 1) {
         defaultSortProvider.getSortForArrayOf(a[0].getClass()).sort(a, sortParameters, comparator);         
      }
   }
   
   public static <T> void sort(T[] a, int i0, int i1, Comparator<T> comparator) {
      if (a.length > 1) {
         defaultSortProvider.getSortForArrayOf(a.getClass()).sort(a, i0, i1, comparator);
      }
   }
   
   public static <T> void sort(T[] a, Comparator<T> comparator) {
      if (a.length > 1) {
         defaultSortProvider.getSortForArrayOf(a.getClass()).sort(a, comparator);
      }
   }
   
   
   // Comparable arrays
      
   // sort() for arrays of comparable objects
   public static <T extends Comparable<? super T>> void sort(T[] a, SortParameters sortParameters) {
      if (a.length > 1) {
         defaultSortProvider.getSortForArrayOf(a.getClass()).sort(a, sortParameters);
      }
   }
   
   public static <T extends Comparable<? super T>> void sort(T[] a, int i0, int i1) {
      if (a.length > 1) {
         defaultSortProvider.getSortForArrayOf(a.getClass()).sort(a, i0, i1);
      }
   }
   
   public static <T extends Comparable<? super T>> void sort(T[] a) {
      if (a.length > 1) {
         defaultSortProvider.getSortForArrayOf(a.getClass()).sort(a);
      }
   }
   
   
   // Generic List, explicit Comparator
   
   public static <T> void sort(List<T> l, SortParameters sortParameters, Comparator<T> comparator) {
      if (l.size() > 1) {
         defaultSortProvider.getSortForArrayOf(l.getClass()).sort(l, sortParameters, comparator);
      }
   }
   
   public static <T> void sort(List<T> l, int i0, int i1, Comparator<T> comparator) {
      if (l.size() > 1) {
         defaultSortProvider.getSortForArrayOf(l.getClass()).sort(l, i0, i1, comparator);
      }
   }
   
   public static <T> void sort(List<T> l, Comparator<T> comparator) {
      if (l.size() > 1) {
         defaultSortProvider.getSortForArrayOf(l.getClass()).sort(l, comparator);
      }
   }
   
   
   // Comparable Lists
   
   // sort() for Lists of comparable objects
   public static <T extends Comparable<? super T>> void sort(List<T> l, SortParameters sortParameters) {
      if (l.size() > 1) {
         defaultSortProvider.getSortForArrayOf(l.getClass()).sort(l, sortParameters);      
      }
   }
   
   public static <T extends Comparable<? super T>> void sort(List<T> l, int i0, int i1) {
      if (l.size() > 1) {
         defaultSortProvider.getSortForArrayOf(l.getClass()).sort(l, i0, i1);      
      }
   }
   
   public static <T extends Comparable<? super T>> void sort(List<T> l) {
      if (l.size() > 1) {
         defaultSortProvider.getSortForArrayOf(l.getClass()).sort(l);
      }
   }
   
   
   ///////////////////////
   // IndexSort static mirror
   
   // IndexSortable

   // Generic arrays, explicit SortIndexer
   
   public static <T> void sort(T[] a, SortParameters sortParameters, SortIndexer<T> sortIndexer) {
      if (a.length > 1) {
         defaultSortProvider.getSortForArrayOf(a.getClass()).sort(a, sortParameters, sortIndexer);      
      }
   }
   
   public static <T> void sort(T[] a, int i0, int i1, SortIndexer<T> sortIndexer) {
      if (a.length > 1) {
         defaultSortProvider.getSortForArrayOf(a.getClass()).sort(a, i0, i1, sortIndexer);      
      }
   }
   
   public static <T> void sort(T[] a, SortIndexer<T> sortIndexer) {
      if (a.length > 1) {
         defaultSortProvider.getSortForArrayOf(a.getClass()).sort(a, sortIndexer);      
      }
   }
   
   
   // IndexSortable; we will find an IndexSorter from the SorterProvider
   
   public static <T extends IndexSortable<? super T>> void sort(T[] a, SortParameters sortParameters) {
      if (a.length > 1) {
         defaultSortProvider.getSortForArrayOf(a.getClass()).sort(a, sortParameters);            
      }
   }
   
   public static <T extends IndexSortable<? super T>> void sort(T[] a, int i0, int i1) {
      if (a.length > 1) {
         defaultSortProvider.getSortForArrayOf(a.getClass()).sort(a, i0, i1);            
      }
   }
   
   public static <T extends IndexSortable<? super T>> void sort(T[] a) {
      if (a.length > 1) {
         defaultSortProvider.getSortForArrayOf(a.getClass()).sort(a);
      }
   }
   
   
   // Generic Lists, explicit SortIndexer
   
   public static <T> void sort(List<T> l, SortParameters sortParameters, SortIndexer<T> sortIndexer) {
      if (l.size() > 1) {
         defaultSortProvider.getSortForListOf(l.getClass()).sort(l, sortParameters, sortIndexer);    
      }
   }
   
   public static <T> void sort(List<T> l, int i0, int i1, SortIndexer<T> sortIndexer) {
      if (l.size() > 1) {
         defaultSortProvider.getSortForListOf(l.getClass()).sort(l, i0, i1, sortIndexer);                  
      }
   }
   
   public static <T> void sort(List<T> l, SortIndexer<T> sortIndexer) {
      if (l.size() > 1) {
         defaultSortProvider.getSortForListOf(l.getClass()).sort(l, sortIndexer);                  
      }
   }
}
