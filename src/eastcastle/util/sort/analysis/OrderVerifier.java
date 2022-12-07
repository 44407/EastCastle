package eastcastle.util.sort.analysis;

import java.util.Comparator;
import java.util.List;

import eastcastle.util.sort.Order;

public class OrderVerifier {
   public static void verify(int[] a, Order order) {
      if (a.length > 2) {
         int   prev;
         
         prev = a[0];
         if (order == Order.ASCENDING) {
            for (int i = 1; i < a.length; i++) {
               int   cur;
               
               cur = a[i];
               if (cur < prev) {
                  ArrayDisplay.display(a);
                  throw new RuntimeException(String.format("Verification failed %d %d %d", i, prev, cur));
               }
            }
         } else {
            for (int i = 1; i < a.length; i++) {
               int   cur;
               
               cur = a[i];
               if (cur > prev) {
                  ArrayDisplay.display(a);
                  throw new RuntimeException(String.format("Verification failed %d %d %d", i, prev, cur));
               }
            }
         }
      }
   }
   
   public static void verify(long[] a, Order order) {
      if (a.length > 2) {
         long   prev;
         
         prev = a[0];
         if (order == Order.ASCENDING) {
            for (int i = 1; i < a.length; i++) {
               long   cur;
               
               cur = a[i];
               if (cur < prev) {
                  ArrayDisplay.display(a);
                  throw new RuntimeException(String.format("Verification failed %d %d %d", i, prev, cur));
               }
            }
         } else {
            for (int i = 1; i < a.length; i++) {
               long   cur;
               
               cur = a[i];
               if (cur > prev) {
                  ArrayDisplay.display(a);
                  throw new RuntimeException(String.format("Verification failed %d %d %d", i, prev, cur));
               }
            }
         }
      }
   }
   
   public static void verify(double[] a, Order order) {
      if (a.length > 2) {
         double   prev;
         
         prev = a[0];
         if (order == Order.ASCENDING) {
            for (int i = 1; i < a.length; i++) {
               double   cur;
               
               cur = a[i];
               if (cur < prev) {
                  ArrayDisplay.display(a);
                  throw new RuntimeException(String.format("Verification failed %d %f %f", i, prev, cur));
               }
            }
         } else {
            for (int i = 1; i < a.length; i++) {
               double   cur;
               
               cur = a[i];
               if (cur > prev) {
                  ArrayDisplay.display(a);
                  throw new RuntimeException(String.format("Verification failed %d %f %f", i, prev, cur));
               }
            }
         }
      }
   }
   
   public static <T extends Comparable<? super T>> void verify(T[] a, Order order) {
      if (a.length > 2) {
         T  prev;
         
         prev = a[0];
         if (order == Order.ASCENDING) {
            for (int i = 1; i < a.length; i++) {
               T   cur;
               
               cur = a[i];
               if (cur.compareTo(prev) < 0) {
                  ArrayDisplay.display(a);
                  throw new RuntimeException(String.format("Verification failed %d %s %s", i, prev, cur));
               }
            }
         } else {
            for (int i = 1; i < a.length; i++) {
               T   cur;
               
               cur = a[i];
               if (cur.compareTo(prev) > 0) {
                  ArrayDisplay.display(a);
                  throw new RuntimeException(String.format("Verification failed %d %s %s", i, prev, cur));
               }
            }
         }
      }
   }
   
   public static <T> void verify(T[] a, Order order, Comparator<T> comparator) {
      if (a.length > 2) {
         T  prev;
         
         prev = a[0];
         if (order == Order.ASCENDING) {
            for (int i = 1; i < a.length; i++) {
               T   cur;
               
               cur = a[i];
               if (comparator.compare(cur, prev) < 0) {
                  ArrayDisplay.display(a);
                  throw new RuntimeException(String.format("Verification failed %d %s %s", i, prev, cur));
               }
            }
         } else {
            for (int i = 1; i < a.length; i++) {
               T   cur;
               
               cur = a[i];
               if (comparator.compare(cur, prev) > 0) {
                  ArrayDisplay.display(a);
                  throw new RuntimeException(String.format("Verification failed %d %s %s", i, prev, cur));
               }
            }
         }
      }
   }
   
   public static <T extends Comparable<? super T>> void verify(List<T> l, Order order) {
      if (l.size() > 2) {
         T  prev;
         
         prev = l.get(0);
         if (order == Order.ASCENDING) {
            for (int i = 1; i < l.size(); i++) {
               T   cur;
               
               cur = l.get(i);
               if (cur.compareTo(prev) < 0) {
                  ArrayDisplay.display(l);
                  throw new RuntimeException(String.format("Verification failed %d %s %s", i, prev, cur));
               }
            }
         } else {
            for (int i = 1; i < l.size(); i++) {
               T   cur;
               
               cur = l.get(i);
               if (cur.compareTo(prev) > 0) {
                  ArrayDisplay.display(l);
                  throw new RuntimeException(String.format("Verification failed %d %s %s", i, prev, cur));
               }
            }
         }
      }
   }
   
   public static <T> void verify(List<T> l, Order order, Comparator<T> comparator) {
      if (l.size() > 2) {
         T  prev;
         
         prev = l.get(0);
         if (order == Order.ASCENDING) {
            for (int i = 1; i < l.size(); i++) {
               T   cur;
               
               cur = l.get(i);
               if (comparator.compare(cur, prev) < 0) {
                  ArrayDisplay.display(l);
                  throw new RuntimeException(String.format("Verification failed %d %s %s", i, prev, cur));
               }
            }
         } else {
            for (int i = 1; i < l.size(); i++) {
               T   cur;
               
               cur = l.get(i);
               if (comparator.compare(cur, prev) > 0) {
                  ArrayDisplay.display(l);
                  throw new RuntimeException(String.format("Verification failed %d %s %s", i, prev, cur));
               }
            }
         }
      }
   }
   
   public static void main(String[] args) {
      try {
         int[] a = new int[4];
         a[0] = 0;
         a[1] = 10;
         a[2] = 20;
         a[3] = 30;
         verify(a, Order.ASCENDING);
         a[3] = 0;
         a[2] = 10;
         a[1] = 20;
         a[0] = 30;
         verify(a, Order.DESCENDING);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
