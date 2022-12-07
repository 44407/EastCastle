package eastcastle.util;

public class ThreadLocalStorage {
   private static final boolean  threadLocalStorageDefault = true;
   private static final int defaultDefaultStorageSize = 131072;
   public static final int defaultStorageSize;
   public static final int intStorageSize;
   public static final int longStorageSize;
   public static final int doubleStorageSize;
   public static final int comparableStorageSize;
   
   private static final String   propertyBase = ThreadLocalStorage.class.getName() +".";
   public static final String enableThreadLocalStorageProperty = propertyBase +"EnableThreadLocalStorage";
   public static final String defaultStorageSizeProperty = propertyBase +"DefaultStorageSize";
   public static final String intStorageSizeProperty = propertyBase +"IntStorageSize";
   public static final String longStorageSizeProperty = propertyBase +"LongStorageSize";
   public static final String doubleStorageSizeProperty = propertyBase +"DoubleStorageSize";
   public static final String comparableStorageSizeProperty = propertyBase +"ComparableStorageSize";
   
   private static final boolean  enableThreadLocalStorage;

   private static final ThreadLocal<double[]> tlDouble = new ThreadLocal<>();
   private static final ThreadLocal<Comparable[]> tlComparable = new ThreadLocal<>();
   private static final ThreadLocal<int[]> tlInt = new ThreadLocal<>();
   private static final ThreadLocal<long[]> tlLong = new ThreadLocal<>();
   private static final ThreadLocal<int[]> tlCount = new ThreadLocal<>();
   
   static {
      enableThreadLocalStorage = PropertiesHelper.systemHelper.getBoolean(enableThreadLocalStorageProperty, threadLocalStorageDefault);
      defaultStorageSize = PropertiesHelper.systemHelper.getInt(defaultStorageSizeProperty, defaultDefaultStorageSize);
      intStorageSize = PropertiesHelper.systemHelper.getInt(intStorageSizeProperty, defaultStorageSize);
      longStorageSize = PropertiesHelper.systemHelper.getInt(longStorageSizeProperty, defaultStorageSize);
      doubleStorageSize = PropertiesHelper.systemHelper.getInt(doubleStorageSizeProperty, defaultStorageSize);
      comparableStorageSize = PropertiesHelper.systemHelper.getInt(comparableStorageSizeProperty, defaultStorageSize);
   }

   public static int[] getLocalIntStorage() {
      int[] storage;

      storage = tlInt.get();
      if (storage == null) {
         storage = new int[intStorageSize];
         tlInt.set(storage);
      }
      return storage;
   }

   public static long[] getLocalLongStorage() {
      long[] storage;

      storage = tlLong.get();
      if (storage == null) {
         storage = new long[longStorageSize];
         tlLong.set(storage);
      }
      return storage;
   }

   public static double[] getLocalDoubleStorage() {
      double[] storage;

      storage = tlDouble.get();
      if (storage == null) {
         storage = new double[doubleStorageSize];
         tlDouble.set(storage);
      }
      return storage;
   }

   public static <T extends Comparable<T>> T[] getLocalComparableStorage() {
      T[] storage;

      storage = (T[])tlComparable.get();
      if (storage == null) {
         storage = (T[])new Comparable[comparableStorageSize];
         tlComparable.set(storage);
      }
      return storage;
   }   
   
   /////////////////
   
   public static int[] getOrCreateIntStorage(int size) {
      if (!enableThreadLocalStorage || size > intStorageSize) {
         return new int[size];
      } else {
         return getLocalIntStorage();
      }
   }
   
   public static long[] getOrCreateLongStorage(int size) {
      if (!enableThreadLocalStorage || size > longStorageSize) {
         return new long[size];
      } else {
         return getLocalLongStorage();
      }
   }

   public static double[] getOrCreateDoubleStorage(int size) {
      if (!enableThreadLocalStorage || size > intStorageSize) {
         return new double[size];
      } else {
         return getLocalDoubleStorage();
      }
   }

   public static <T extends Comparable<T>> T[] getOrCreateComparableStorage(int size) {
      if (!enableThreadLocalStorage || size > comparableStorageSize) {
         return (T[])new Comparable[size];
      } else {
         return getLocalComparableStorage();
      }
   }
}
