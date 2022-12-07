package eastcastle.util.sort.analysis;

import java.util.HashMap;
import java.util.Map;

import eastcastle.util.sort.Order;

public class TestSpecification {
   private final SortImplementation sort;
   private final Type type;
   private final int  size;
   private final Order order;
   private final double warmupTimeSeconds;
   private final double testTimeSeconds;
   private final int testReps;
   private final DistributionType distributionType;
   private final Map<String, String>  distParameters;
   private final Map<String, String>  parameters;
   
   public TestSpecification(SortImplementation sort, Type type, Order order, int size, double warmupTimeSeconds, double testTimeSeconds, int testReps, 
                           DistributionType distributionType, Map<String, String> parameters, Map<String, String> globalParameters) {
      this.sort = sort;
      this.type = type;
      this.order = order;
      this.size = size;
      this.warmupTimeSeconds = warmupTimeSeconds;
      this.testTimeSeconds = testTimeSeconds;
      this.testReps = testReps;
      this.distributionType = distributionType;
      this.distParameters = parameters;
      this.parameters = new HashMap<>();
      this.parameters.putAll(globalParameters);
      this.parameters.putAll(distParameters); // per dist take precedence
   }
   
   public TestSpecification(SortImplementation sort, Type type, Order order, int size, double warmupTimeSeconds, double testTimeSeconds, int testReps, 
         DistributionTypeAndParameters distributionTypeAndParameters, Map<String, String> globalParameters) {
      this(sort, type, order, size, warmupTimeSeconds, testTimeSeconds, testReps, 
            distributionTypeAndParameters.getDistributionType(), distributionTypeAndParameters.getParameters(), globalParameters);
   }
   
   public SortImplementation getSortImplementation() {
      return sort;
   }

   public Type getType() {
      return type;
   }
   
   public Order getOrder() {
      return order;
   }

   public int getSize() {
      return size;
   }

   public double getWarmupTimeSeconds() {
      return warmupTimeSeconds;
   }

   public double getTestTimeSeconds() {
      return testTimeSeconds;
   }
   
   public int getTestReps() {
      return testReps;
   }
   
   public DistributionType getDistributionType() {
      return distributionType;
   }
   
   public Map<String, String> getParameters() {
      return parameters;
   }
   
   public int getIntParameter(String name, boolean required, int defaultValue) {
      String   v;
      
      v = parameters.get(name);
      if (v == null) {
         if (required) {
            throw new RuntimeException("Can't find required parameter:"+ name);
         } else {
            return defaultValue;
         }
      } else {
         return Integer.parseInt(v);
      }
   }
   
   public int getIntParameter(String name) {
      return getIntParameter(name, true, 0);
   }
   
   public boolean getBooleanParameter(String name, boolean required, boolean defaultValue) {
      String   v;
      
      v = parameters.get(name);
      if (v == null) {
         if (required) {
            throw new RuntimeException("Can't find required parameter:"+ name);
         } else {
            return defaultValue;
         }
      } else {
         return Boolean.parseBoolean(v);
      }
   }
   
   public boolean getBooleanParameter(String name) {
      return getBooleanParameter(name, true, false);
   }
   
   public Distribution getDistributionFromType() {
      switch (getDistributionType()) {
         case Random:
            return RandomDistribution.instance; 
         case SortedRanges:
            return new SortedRangesDistribution(this);
         case PartiallySorted:
            return new PartiallySortedDistribution(this);
         case NSwapped:
            return new NSwappedDistribution(this);
         default: throw new UnsupportedOperationException("Unsupported distribution: "+ getDistributionType());
      }
   }
   
   private Map<String,String> filterKnownParameters() {
      return filterKnownParameters(getDistributionFromType());
   }
   
   private Map<String,String> filterKnownParameters(Distribution d) {
      Map<String, String>  m;
      
      m = new HashMap<>();
      for (Map.Entry<String, String> e : parameters.entrySet()) {
         if (d.getKnownParameters().contains(e.getKey())) {
            m.put(e.getKey(), e.getValue());
         }
      }
      return m;
   }
   
   @Override
   public String toString() {
      return String.format("%-10s\t%5.1f\t%5.1f\t%8d\t%10s\t%-16s\t%-40s\t%-8s", type.getName(), warmupTimeSeconds, testTimeSeconds, size, order, distributionType, filterKnownParameters(), sort);
   }
}
