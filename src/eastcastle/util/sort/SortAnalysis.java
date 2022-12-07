package eastcastle.util.sort;

class SortAnalysis {
   private final Order  initialOrder;
   private final int    size;
   private long   bitDelta;
   private int    numRuns;
   private boolean   allValuesInRuns;
   private int    sortPercentage_x100;
   private long   min;
   private long   max;
   
   private final Order[]  runOrder;
   private final int[]   runI0;
   private final int[]   runI1;
   
   public SortAnalysis(Order initialOrder, int size, int numRuns, Order[] runOrder, int[] runI0, int[] runI1) {      
      this.initialOrder = initialOrder;
      this.size = size;
      this.numRuns = numRuns;
      this.runOrder = runOrder;
      this.runI0 = runI0;
      this.runI1 = runI1;
   }
   
   public void setMinMax(long min, long max) {
      this.min = min;
      this.max = max;
   }
   
   public void setBitDelta(long bitDelta) {
      this.bitDelta = bitDelta;
   }
   
   public void setValuesInRuns(int valuesInRuns) {
      this.allValuesInRuns = valuesInRuns == size;
      sortPercentage_x100 = (int)(((long)valuesInRuns * 10000L) / (long)size);
   }
   
   public Order getInitialOrder() {
      return initialOrder;
   }
   
   public boolean needsSort() {
      return getNumRuns() > 1 || !allValuesInRuns;
   }
   
   public int getNumRuns() {
      return numRuns;
   }
   
   public Run getRun(int index) {
      return new Run(runOrder[index], runI0[index], runI1[index]);
   }
   
   public long getBitDelta() {
      return bitDelta;
   }
   
   public long getRange() {
      return max - min;
   }
   
   public boolean allValuesInRuns() {
      return allValuesInRuns;
   }
   
   public int getSortPercentage_x100() {
      return sortPercentage_x100;
   }
   
   @Override
   public String toString() {
      StringBuilder  sb;
      
      sb = new StringBuilder();
      sb.append(String.format("%x:%d:%s", bitDelta, getNumRuns(), allValuesInRuns));
      for (int i = 0; i < numRuns; i++) {
         Run   run;
         
         run = getRun(i);
         sb.append(':');
         sb.append('{');
         sb.append(run.toString());
         sb.append('}');
      }
      return sb.toString();
   }
}
