package eastcastle.util.sort;

/**
 * Demarcates a "run" of monotonically increasing or decreasing values, contained within an array or list.
 */
class Run {
   private Order  order;
   private int i0;
   private int i1;
   
   public Run(Order order, int i0, int i1) {
      this.order = order;
      this.i0 = i0;
      this.i1 = i1;
   }
   
   public void set(Order order, int i0, int i1) {
      this.order = order;
      this.i0 = i0;
      this.i1 = i1;
   }

   public Order getOrder() {
      return order;
   }

   public int getI0() {
      return i0;
   }

   public int getI1() {
      return i1;
   }
   
   @Override
   public String toString() {
      return String.format("%s:%d:%d", order, i0, i1);
   }
}
