package eastcastle.util.sort.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import eastcastle.util.MapUtil;
import eastcastle.util.sort.Order;
import eastcastle.util.sort.ParallelSystemSort;
import eastcastle.util.sort.RadJSort;
import eastcastle.util.sort.Sort;
import eastcastle.util.sort.SortParameters;
import eastcastle.util.sort.SystemSort;
import eastcastle.util.sort.UUIDSortIndexer;

/**
 * Benchmarking application for measuring the performance of sorts 
 */
public class MeasureSorts {
   private final RadJSort     radJSort;
   private final SystemSort   systemSort;
   private final ParallelSystemSort   parallelSystemSort;
   
   public MeasureSorts() {
      radJSort = RadJSort.defaultInstance;
      systemSort = new SystemSort();
      parallelSystemSort = new ParallelSystemSort();
   }
   
   /**
    * Heuristic: guess the number of runs for the given size to determine timing
    * @param size
    * @return
    */
   private int getTimingRuns(int size) {
      if (size == 0) {
         return 1;
      } else {
         return Math.max(1, 262144 / size);
      }
   }
   
   private int computeRunsFromTiming(double targetTimeSeconds, long averageElapsedNanos) {
      long  targetTimeNanos;
      long  estimatedRuns;
      
      targetTimeNanos = (long)(targetTimeSeconds * 1e9);
      estimatedRuns = Math.max(targetTimeNanos / averageElapsedNanos, 1);
      return (int)estimatedRuns;
   }
   
   private long testOneInt(Sort sort, int runs, TestSpecification testSpec, boolean includeOverhead) {
      long  totalElapsed;

      totalElapsed = 0;
      for (int i = 0; i < runs; i++) {
         long  t0;
         long  t1;
         long  t2;
         int[] a;
         boolean  sortedCorrectly;      
         int[] unsorted;
         
         t0 = System.nanoTime();
         a = testSpec.getDistributionFromType().getInt(testSpec.getSize(), testSpec.getParameters());
         unsorted = a.clone();         
         if (!testSpec.getType().isList()) {
            t1 = System.nanoTime();
            sort.sort(a, SortParameters.DEFAULT.order(testSpec.getOrder()));
            t2 = System.nanoTime();
         } else {
            List<Integer>  listA;

            listA = new ArrayList<>(a.length);
            for (int j = 0; j < a.length; ++j) {
               listA.add(a[j]);
            }
            t1 = System.nanoTime();
            sort.sort(listA, SortParameters.DEFAULT.order(testSpec.getOrder()));
            t2 = System.nanoTime();
            for (int j = 0; j < a.length; ++j) {
               a[j] = listA.get(j);
            }
         }
         if (includeOverhead) {
            totalElapsed += t2 - t0;
         } else {
            totalElapsed += t2 - t1;
         }
         sortedCorrectly = sanityCheck(a, testSpec.getOrder());
         if (!sortedCorrectly) {
            System.out.println("................");
            System.out.printf("Failed %s\n", sort.getClass().getName());
            display(unsorted);
            System.out.println("................");
            display(a);
            System.out.println("................");
            System.exit(-1);
         }
      }      
      return totalElapsed / runs;
   }

   private long testOneLong(Sort sort, int runs, TestSpecification testSpec, boolean includeOverhead) {
      long  totalElapsed;
      
      totalElapsed = 0;
      for (int i = 0; i < runs; i++) {
         long  t0;
         long  t1;
         long  t2;
         long[] source;
         long[] a;
         boolean  sortedCorrectly;
         
         t0 = System.nanoTime();
         source = testSpec.getDistributionFromType().getLong(testSpec.getSize(), testSpec.getParameters());
         a = new long[source.length];
         System.arraycopy(source, 0, a, 0, a.length);
         t1 = System.nanoTime();
         sort.sort(a, SortParameters.DEFAULT.order(testSpec.getOrder()));
         t2 = System.nanoTime();
         if (includeOverhead) {
            totalElapsed += t2 - t0;
         } else {
            totalElapsed += t2 - t1;
         }
         sortedCorrectly = sanityCheck(a, testSpec.getOrder());
         if (!sortedCorrectly) {
            System.out.println("................");
            System.out.printf("Failed %s\n", sort.getClass().getName());
            display(a);
            System.out.println("................");
            System.exit(-1);
         }
      }
      return totalElapsed / runs;
   }
   
   private long testOneDouble(Sort sort, int runs, TestSpecification testSpec, boolean includeOverhead) {
      long  totalElapsed;
      
      totalElapsed = 0;
      for (int i = 0; i < runs; i++) {
         long  t0;
         long  t1;
         long  t2;
         double[] source;
         double[] a;
         boolean  sortedCorrectly;
         
         t0 = System.nanoTime();
         source = testSpec.getDistributionFromType().getDouble(testSpec.getSize(), testSpec.getParameters());
         a = new double[source.length];
         System.arraycopy(source, 0, a, 0, a.length);
         t1 = System.nanoTime();
         sort.sort(a, SortParameters.DEFAULT.order(testSpec.getOrder()));
         t2 = System.nanoTime();
         if (includeOverhead) {
            totalElapsed += t2 - t0;
         } else {
            totalElapsed += t2 - t1;
         }
         sortedCorrectly = sanityCheck(a, testSpec.getOrder());
         if (!sortedCorrectly) {
            System.out.println("................");
            System.out.printf("Failed %s\n", sort.getClass().getName());
            display(a);
            System.out.println("................");
            System.exit(-1);
         }
      }
      return totalElapsed / runs;
   }
   
   private long testOneString(Sort sort, int runs, TestSpecification testSpec, boolean includeOverhead) {
      long  totalElapsed;
      
      totalElapsed = 0;
      for (int i = 0; i < runs; i++) {
         long  t0;
         long  t1;
         long  t2;
         String[] source;
         String[] a;
         boolean  sortedCorrectly;
         
         t0 = System.nanoTime();
         source = testSpec.getDistributionFromType().getString(testSpec.getSize(), testSpec.getParameters());
         a = new String[source.length];
         System.arraycopy(source, 0, a, 0, a.length);
         t1 = System.nanoTime();
         sort.sort(a, SortParameters.DEFAULT.order(testSpec.getOrder()));
         t2 = System.nanoTime();
         if (includeOverhead) {
            totalElapsed += t2 - t0;
         } else {
            totalElapsed += t2 - t1;
         }
         //sortedCorrectly = sanityCheck(a, testSpec.getOrder());
         sortedCorrectly = true;
         if (!sortedCorrectly) {
            System.out.println("................");
            System.out.printf("Failed %s\n", sort.getClass().getName());
            display(a);
            System.out.println("................");
            System.exit(-1);
         }
      }
      return totalElapsed / runs;
   }
   
   private long testOneIntegerBoxed(Sort sort, int runs, TestSpecification testSpec, boolean includeOverhead) {
      long  totalElapsed;
      
      totalElapsed = 0;
      for (int i = 0; i < runs; i++) {
         long  t0;
         long  t1;
         long  t2;
         int[] source;
         Integer[] a;
         boolean  sortedCorrectly;
         
         t0 = System.nanoTime();
         source = testSpec.getDistributionFromType().getInt(testSpec.getSize(), testSpec.getParameters());
         a = new Integer[source.length];
         for (int j = 0; j < a.length; j++) {
            a[j] = source[j];
         }
         t1 = System.nanoTime();
         sort.sort(a, SortParameters.DEFAULT.order(testSpec.getOrder()));
         t2 = System.nanoTime();
         if (includeOverhead) {
            totalElapsed += t2 - t0;
         } else {
            totalElapsed += t2 - t1;
         }
         sortedCorrectly = sanityCheck(a, testSpec.getOrder());
         if (!sortedCorrectly) {
            System.out.println("................");
            System.out.printf("Failed %s\n", sort.getClass().getName());
            display(a);
            System.out.println("................");
            System.exit(-1);
         }
      }
      return totalElapsed / runs;
   }
   
   private long testOneUUID(Sort sort, int runs, TestSpecification testSpec, boolean includeOverhead) {
      long  totalElapsed;
      
      totalElapsed = 0;
      for (int i = 0; i < runs; i++) {
         long  t0;
         long  t1;
         long  t2;
         UUID[] source;
         UUID[] a;
         boolean  sortedCorrectly;
         
         t0 = System.nanoTime();
         source = testSpec.getDistributionFromType().getUUID(testSpec.getSize(), testSpec.getParameters());
         a = new UUID[source.length];
         System.arraycopy(source, 0, a, 0, a.length);
         t1 = System.nanoTime();
         sort.sort(a, SortParameters.DEFAULT.order(testSpec.getOrder()));
         t2 = System.nanoTime();
         if (includeOverhead) {
            totalElapsed += t2 - t0;
         } else {
            totalElapsed += t2 - t1;
         }
         sortedCorrectly = sanityCheck(a, testSpec.getOrder());
         if (!sortedCorrectly) {
            System.out.println("................");
            System.out.printf("Failed %s\n", sort.getClass().getName());
            display(a);
            System.out.println("................");
            System.exit(-1);
         }
      }
      return totalElapsed / runs;
   }
   
   private long testOneStringList(Sort sort, int runs, TestSpecification testSpec, boolean includeOverhead) {
      long  totalElapsed;
      
      totalElapsed = 0;
      for (int i = 0; i < runs; i++) {
         long  t0;
         long  t1;
         long  t2;
         boolean  sortedCorrectly;      
         List<String> source;
         List<String> l;
         
         t0 = System.nanoTime();
         source = testSpec.getDistributionFromType().getStringList(testSpec.getSize(), testSpec.getParameters());
         l = new ArrayList<>(source.size());
         l.addAll(source);
         t1 = System.nanoTime();
         sort.sort(l, SortParameters.DEFAULT.order(testSpec.getOrder()));
         t2 = System.nanoTime();
         if (includeOverhead) {
            totalElapsed += t2 - t0;
         } else {
            totalElapsed += t2 - t1;
         }
         sortedCorrectly = sanityCheck(l, testSpec.getOrder());
         if (!sortedCorrectly) {
            System.out.println("................");
            System.out.printf("Failed %s\n", sort.getClass().getName());
            displayList(l);
            System.out.println("................");
            System.exit(-1);
         }
      }
      return totalElapsed / runs;
   }
   
   private long testOneIndexSortableString(Sort sort, int runs, TestSpecification testSpec, boolean includeOverhead) {
      long  totalElapsed;
      
      totalElapsed = 0;
      for (int i = 0; i < runs; i++) {
         long  t0;
         long  t1;
         long  t2;
         String[] _source;
         IndexSortableString[] source;
         IndexSortableString[] a;
         boolean  sortedCorrectly;
         
         t0 = System.nanoTime();
         _source = testSpec.getDistributionFromType().getString(testSpec.getSize(), testSpec.getParameters());
         source = new IndexSortableString[_source.length];
         for (int j = 0; j < source.length; j++) {
            source[j] = new IndexSortableString(_source[j]);
         }
         a = new IndexSortableString[source.length];
         System.arraycopy(source, 0, a, 0, a.length);
         t1 = System.nanoTime();
         sort.sort(a, SortParameters.DEFAULT.order(testSpec.getOrder()));
         t2 = System.nanoTime();
         if (includeOverhead) {
            totalElapsed += t2 - t0;
         } else {
            totalElapsed += t2 - t1;
         }
         /*
         sortedCorrectly = sanityCheck(a, testSpec.getOrder());
         if (!sortedCorrectly) {
            System.out.println("................");
            System.out.printf("Failed %s\n", sort.getClass().getName());
            display(a);
            System.out.println("................");
            System.exit(-1);
         }
         */
      }
      return totalElapsed / runs;
   }
   
   public boolean sanityCheck(int[] a, Order order) {
      if (a.length > 0) {
         int   prev;

         if (order == Order.ASCENDING) {
            prev = a[0];
            for (int i = 1; i < a.length; i++) {
               if (a[i] < prev) {
                  return false;
               }
               prev = a[i];
            }
         } else {
            prev = a[0];
            for (int i = 1; i < a.length; i++) {
               if (a[i] > prev) {
                  return false;
               }
               prev = a[i];
            }
         }
      }
      return true;
   }
   
   public boolean sanityCheck(long[] a, Order order) {      
      if (a.length > 0) {
         long   prev;
         
         prev = a[0];
         if (order == Order.ASCENDING) {
            for (int i = 1; i < a.length; i++) {
               if (a[i] < prev) {
                  System.out.printf("%d\t%d\t%x\n", i - 1, prev, prev);
                  System.out.printf("%d\t%d\t%x\n", i, a[i], a[i]);
                  return false;
               }
               prev = a[i];
            }
         } else {
            for (int i = 1; i < a.length; i++) {
               if (a[i] > prev) {
                  System.out.printf("%d\t%d\t%x\n", i - 1, prev, prev);
                  System.out.printf("%d\t%d\t%x\n", i, a[i], a[i]);
                  return false;
               }
               prev = a[i];
            }
         }
      }
      return true;
   }
   
   public boolean sanityCheck(double[] a, Order order) {
      if (a.length > 0) {
         double   prev;
         
         prev = a[0];
         if (order == Order.ASCENDING) {
            for (int i = 1; i < a.length; i++) {
               if (a[i] < prev) {
                  System.out.printf("%f < %f\n", a[i], prev);
                  return false;
               }
               prev = a[i];
            }
         } else {
            for (int i = 1; i < a.length; i++) {
               if (a[i] > prev) {
                  System.out.printf("%f > %f\n", a[i], prev);
                  return false;
               }
               prev = a[i];
            }
         }
      }
      return true;
   }
   
   public <T extends Comparable<T>> boolean sanityCheck(T[] a, Order order) {
      if (a.length > 0) {
         T   prev;
         
         prev = a[0];
         if (order == Order.ASCENDING) {
            for (int i = 1; i < a.length; i++) {
               if (prev.compareTo(a[i]) == 1) {
                  System.out.printf("%s\t%x\t%d\n", a[i], UUIDSortIndexer.instance.sortIndex((UUID)a[i]), UUIDSortIndexer.instance.sortIndex((UUID)a[i]));
                  System.out.printf("%s < %s\t%d\n", a[i], prev, i);
                  return false;
               }
               prev = a[i];
            }
         } else {
            for (int i = 1; i < a.length; i++) {
               if (prev.compareTo(a[i]) == -1) {
                  System.out.printf("%s > %s\n", a[i], prev);
                  return false;
               }
               prev = a[i];
            }
         }
      }
      return true;
   }
   
   public <T extends Comparable<T>> boolean sanityCheck(List<T> l, Order order) {
      if (l.size() > 0) {
         Comparable<T>   prev;
         
         prev = l.get(0);
         if (order == Order.ASCENDING) {
            for (int i = 1; i < l.size(); i++) {
               if (prev.compareTo(l.get(i)) == 1) {
                  return false;
               }
               prev = l.get(i);
            }
         } else {
            for (int i = 1; i < l.size(); i++) {
               if (prev.compareTo(l.get(i)) == -1) {
                  return false;
               }
               prev = l.get(i);
            }
         }
      }
      return true;
   }
   
   public void display(int[] a) {
      for (int i = 0; i < a.length; i++) {
         System.out.printf("%d\t%d\t%x\n", i, a[i], a[i]);
      }
   }
   
   public void display(long[] a) {
      for (int i = 0; i < a.length; i++) {
         System.out.printf("%d\t%d\n", i, a[i]);
      }
   }
   
   public void display(double[] a) {
      for (int i = 0; i < a.length; i++) {
         System.out.printf("%d\t%f\t%x\n", i, a[i], Double.doubleToRawLongBits(a[i]));
      }
   }
   
   public void display(Object[] a) {
      for (int i = 0; i < a.length; i++) {
         System.out.printf("%d\t%s\n", i, a[i]);
      }
   }
   
   public <T> void displayList(List<T> l) {
      for (int i = 0; i < l.size(); i++) {
         System.out.printf("%d\t%s\n", i, l.get(i).toString());
      }
   }
   
   public void test(TestSpecification testSpec) {
      try {
         switch (testSpec.getType()) {
         case IntegerPrimitiveArray: testInt(testSpec); break;
         case IntegerList: testInt(testSpec); break;
         case LongPrimitiveArray: testLong(testSpec); break;
         case DoublePrimitiveArray: testDouble(testSpec); break;
         case StringArray: testString(testSpec); break;
         case IntegerBoxedArray: testIntegerBoxed(testSpec); break;
         case IndexSortableStringArray: testIndexSortableString(testSpec); break;
         case StringList: testStringList(testSpec); break;
         case UUIDArray: testUUID(testSpec); break;
         default: throw new RuntimeException("panic");
         }
      } catch (UnsupportedOperationException uoe) {
         System.out.printf("%s\tUnsupported presently\n", testSpec);         
      }
   }   
   
   private Sort getSort(TestSpecification testSpec) {
      switch (testSpec.getSortImplementation()) {
      case RadJ: return radJSort;
      case System: return systemSort;
      case ParallelSystem: return parallelSystemSort;
      default: throw new UnsupportedOperationException("Unsupported SortImplementation: "+ testSpec.getSortImplementation());
      }
   }
   
   private void displayTestResults(TestSpecification testSpec, int runs, long averageElapsedNanos) {
      System.out.printf("%45s\t%8d\t%9d\n", testSpec, runs, averageElapsedNanos);
   }
   
   public void testInt(TestSpecification testSpec) {
      long  averageElapsedNanos;
      Sort  sort;
      int   runs;
      
      sort = getSort(testSpec);      
      
      // timing
      averageElapsedNanos = testOneInt(sort, getTimingRuns(testSpec.getSize()), testSpec, true);
      // warmup
      averageElapsedNanos = testOneInt(sort, computeRunsFromTiming(testSpec.getWarmupTimeSeconds(), averageElapsedNanos), testSpec, true);
      // run
      runs = computeRunsFromTiming(testSpec.getTestTimeSeconds(), averageElapsedNanos);
      for (int i = 0; i < testSpec.getTestReps(); i++) {
         averageElapsedNanos = testOneInt(sort, runs, testSpec, false);
         displayTestResults(testSpec, runs, averageElapsedNanos);
      }
      
      //display(a);
      //System.out.println();
   }
   
   public void testLong(TestSpecification testSpec) {
      long  averageElapsedNanos;
      Sort  sort;
      int   runs;
      
      sort = getSort(testSpec);
      
      // timing
      averageElapsedNanos = testOneLong(sort, getTimingRuns(testSpec.getSize()), testSpec, true);
      // warmup
      averageElapsedNanos = testOneLong(sort, computeRunsFromTiming(testSpec.getWarmupTimeSeconds(), averageElapsedNanos), testSpec, true);
      // run
      runs = computeRunsFromTiming(testSpec.getTestTimeSeconds(), averageElapsedNanos);
      for (int i = 0; i < testSpec.getTestReps(); i++) {
         averageElapsedNanos = testOneLong(sort, runs, testSpec, false);
         displayTestResults(testSpec, runs, averageElapsedNanos);
      }
   }
   
   public void testDouble(TestSpecification testSpec) {
      long  averageElapsedNanos;
      Sort  sort;
      int   runs;
      
      sort = getSort(testSpec);
      
      // timing
      averageElapsedNanos = testOneDouble(sort, getTimingRuns(testSpec.getSize()), testSpec, true);
      // warmup
      averageElapsedNanos = testOneDouble(sort, computeRunsFromTiming(testSpec.getWarmupTimeSeconds(), averageElapsedNanos), testSpec, true);
      // run
      runs = computeRunsFromTiming(testSpec.getTestTimeSeconds(), averageElapsedNanos);
      for (int i = 0; i < testSpec.getTestReps(); i++) {
         averageElapsedNanos = testOneDouble(sort, runs, testSpec, false);
         displayTestResults(testSpec, runs, averageElapsedNanos);
      }
   }
   
   public void testString(TestSpecification testSpec) {
      long  averageElapsedNanos;
      Sort  sort;
      int   runs;
      
      sort = getSort(testSpec);
      
      // timing
      averageElapsedNanos = testOneString(sort, getTimingRuns(testSpec.getSize()), testSpec, true);
      // warmup
      averageElapsedNanos = testOneString(sort, computeRunsFromTiming(testSpec.getWarmupTimeSeconds(), averageElapsedNanos), testSpec, true);
      // run
      runs = computeRunsFromTiming(testSpec.getTestTimeSeconds(), averageElapsedNanos);
      for (int i = 0; i < testSpec.getTestReps(); i++) {
         averageElapsedNanos = testOneString(sort, runs, testSpec, false);
         displayTestResults(testSpec, runs, averageElapsedNanos);
      }
   }

   public void testIntegerBoxed(TestSpecification testSpec) {
      long  averageElapsedNanos;
      Sort  sort;
      int   runs;
      
      sort = getSort(testSpec);
      
      // timing
      averageElapsedNanos = testOneIntegerBoxed(sort, getTimingRuns(testSpec.getSize()), testSpec, true);
      // warmup
      averageElapsedNanos = testOneIntegerBoxed(sort, computeRunsFromTiming(testSpec.getWarmupTimeSeconds(), averageElapsedNanos), testSpec, true);
      // run
      runs = computeRunsFromTiming(testSpec.getTestTimeSeconds(), averageElapsedNanos);
      for (int i = 0; i < testSpec.getTestReps(); i++) {
         averageElapsedNanos = testOneString(sort, runs, testSpec, false);
         displayTestResults(testSpec, runs, averageElapsedNanos);
      }
   }
   
   public void testIndexSortableString(TestSpecification testSpec) {
      long  averageElapsedNanos;
      Sort  sort;
      int   runs;
      
      sort = getSort(testSpec);
      
      // timing
      averageElapsedNanos = testOneIndexSortableString(sort, getTimingRuns(testSpec.getSize()), testSpec, true);
      // warmup
      averageElapsedNanos = testOneIndexSortableString(sort, computeRunsFromTiming(testSpec.getWarmupTimeSeconds(), averageElapsedNanos), testSpec, true);
      // run
      runs = computeRunsFromTiming(testSpec.getTestTimeSeconds(), averageElapsedNanos);
      for (int i = 0; i < testSpec.getTestReps(); i++) {
         averageElapsedNanos = testOneString(sort, runs, testSpec, false);
         displayTestResults(testSpec, runs, averageElapsedNanos);
      }
   }
   
   public void testStringList(TestSpecification testSpec) {
      long  averageElapsedNanos;
      Sort  sort;
      int   runs;
      
      sort = getSort(testSpec);
      
      // timing
      averageElapsedNanos = testOneStringList(sort, getTimingRuns(testSpec.getSize()), testSpec, true);
      // warmup
      averageElapsedNanos = testOneStringList(sort, computeRunsFromTiming(testSpec.getWarmupTimeSeconds(), averageElapsedNanos), testSpec, true);
      // run
      runs = computeRunsFromTiming(testSpec.getTestTimeSeconds(), averageElapsedNanos);
      for (int i = 0; i < testSpec.getTestReps(); i++) {
         averageElapsedNanos = testOneStringList(sort, runs, testSpec, false);
         displayTestResults(testSpec, runs, averageElapsedNanos);
      }
   }
   
   public void testUUID(TestSpecification testSpec) {
      long  averageElapsedNanos;
      Sort  sort;
      int   runs;
      
      sort = getSort(testSpec);
      
      // timing
      averageElapsedNanos = testOneUUID(sort, getTimingRuns(testSpec.getSize()), testSpec, true);
      // warmup
      averageElapsedNanos = testOneUUID(sort, computeRunsFromTiming(testSpec.getWarmupTimeSeconds(), averageElapsedNanos), testSpec, true);
      // run
      runs = computeRunsFromTiming(testSpec.getTestTimeSeconds(), averageElapsedNanos);
      for (int i = 0; i < testSpec.getTestReps(); i++) {
         averageElapsedNanos = testOneStringList(sort, runs, testSpec, false);
         displayTestResults(testSpec, runs, averageElapsedNanos);
      }
   }
   
   private static List<Integer> getInts(String def) {
      String[] defs;
      List<Integer>  ints;
      
      defs = def.split(",");
      ints = new ArrayList<>(defs.length);
      for (String s : defs) {
         ints.add(Integer.valueOf(s));
      }
      return ints;
   }
   
   public static void main(String[] args) {
      if (args.length != 9) {
         System.out.println("args: <warmupTimeSeconds> <testTimeSeconds> <testReps> <size,...> <Type,...> <Order,...> <Distribution{parameter=value,...},...> <SortImplementation,...> <parameter=value,...>");
      } else {
         try {
            MeasureSorts  st;
            List<Integer>  sizes;
            double   warmupTimeSeconds;
            double   testTimeSeconds;
            int      testReps;
            List<Type> types;
            List<Order> orders;
            List<DistributionTypeAndParameters>   distributionTypesAndParameters;
            List<SortImplementation>   sortImplementations;
            Map<String, String>  globalParameters;
            
            warmupTimeSeconds = Double.parseDouble(args[0]);
            testTimeSeconds = Double.parseDouble(args[1]);
            testReps = Integer.parseInt(args[2]);
            sizes = getInts(args[3]);
            types = Type.parseList(args[4]);     
            orders = Order.parseList(args[5]);
            distributionTypesAndParameters = DistributionTypeAndParameters.parseList(args[6]);
            sortImplementations = SortImplementation.parseList(args[7]);
            globalParameters = MapUtil.parseStringMap(args[8], ',', '=', MapUtil.NoDelimiterAction.Exception);
            st = new MeasureSorts();
            for (Order order : orders) {
               for (Type type : types) {
                  for (DistributionTypeAndParameters distributionTypeAndParameters : distributionTypesAndParameters) {
                     for (int size : sizes) {
                        for (SortImplementation sortImplementation : sortImplementations) {
                           TestSpecification testSpec;
                           
                           testSpec = new TestSpecification(sortImplementation, type, order, size, warmupTimeSeconds, testTimeSeconds, testReps, 
                                 distributionTypeAndParameters, globalParameters);
                           st.test(testSpec);
                        }
                     }
                  }
               }
            }
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }
}
