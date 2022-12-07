package eastcastle.util.sort;

import java.util.List;

/**
 * Full parameters passed to sort(). 
 */
public class SortParameters {
   private final int i0;
   private final int i1;
   private final Order  order;

   public static final SortParameters DEFAULT = new SortParameters();
   
   public static final int  UNSPECIFIED = -1;
   
   /**
    * 
    * @param i0 index of first element to sort (inclusive)
    * @param i1 index of last element to sort (exclusive; last element sorted is i1 - 1)
    * @param order order of the sort
    * @param distributionHints
    */
   public SortParameters(int i0, int i1, Order order) {
      this.i0 = i0 == UNSPECIFIED ? 0 : i0;
      this.i1 = i1;
      this.order = order;
   }
   
   public SortParameters() {
      this(0, UNSPECIFIED, Order.ASCENDING);
   }
   
   ////////////////////////////
   
   public int getI0() {
      return i0;
   }

   public int getI1() {
      return i1;
   }
   
   public int getNumElements() {
      return i1 - i0;
   }

   public Order getOrder() {
      return order;
   }

   public static int getUnspecified() {
      return UNSPECIFIED;
   }
   
   ////////////////////////////
   
   public SortParameters i0(int i0) {
      return new SortParameters(i0, this.i1, this.order);
   }
   
   public SortParameters i1(int i1) {
      return new SortParameters(this.i0, i1, this.order);
   }
   
   public SortParameters order(Order order) {
      return new SortParameters(this.i0, this.i1, order);
   }
   
   public SortParameters getDefaultsFor(int[] a) {
      SortParameters sortParameters;
      
      sortParameters = this;
      if (i0 == UNSPECIFIED) {
         sortParameters = sortParameters.i0(0);
      }
      if (i1 == UNSPECIFIED) {
         sortParameters = sortParameters.i1(a.length);
      }
      return sortParameters;
   }
   
   public SortParameters getDefaultsFor(long[] a) {
      SortParameters sortParameters;
      
      sortParameters = this;
      if (i0 == UNSPECIFIED) {
         sortParameters = sortParameters.i0(0);
      }
      if (i1 == UNSPECIFIED) {
         sortParameters = sortParameters.i1(a.length);
      }
      return sortParameters;
   }
   
   public SortParameters getDefaultsFor(double[] a) {
      SortParameters sortParameters;
      
      sortParameters = this;
      if (i0 == UNSPECIFIED) {
         sortParameters = sortParameters.i0(0);
      }
      if (i1 == UNSPECIFIED) {
         sortParameters = sortParameters.i1(a.length);
      }
      return sortParameters;
   }
   
   public <T> SortParameters getDefaultsFor(T[] a) {
      SortParameters sortParameters;
      
      sortParameters = this;
      if (i0 == UNSPECIFIED) {
         sortParameters = sortParameters.i0(0);
      }
      if (i1 == UNSPECIFIED) {
         sortParameters = sortParameters.i1(a.length);
      }
      return sortParameters;
   }
   
   public <T> SortParameters getDefaultsFor(List<T> l) {
      SortParameters sortParameters;
      
      sortParameters = this;
      if (i0 == UNSPECIFIED) {
         sortParameters = sortParameters.i0(0);
      }
      if (i1 == UNSPECIFIED) {
         sortParameters = sortParameters.i1(l.size());
      }
      return sortParameters;
   }   
}
