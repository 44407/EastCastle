package eastcastle.util.sort.analysis;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public abstract class BaseDistribution implements Distribution {
   protected final Set<String>   knownParameters;
   
   public static final String   stringLengthParameter = "stringLength";
   public static final String   lsbParameter = "lsb";
   
   private static final int   bitsPerInt = 32;
   private static final int   bitsPerLong = 64;
   
   private static final int      alphabetSize = 26;
   
   BaseDistribution(Set<String> knownParameters) {
      this.knownParameters = knownParameters;
      this.knownParameters.add(lsbParameter);
   }
   
   BaseDistribution(String...knownParametersList) {
      Set<String> knownParameters;
      
      knownParameters = new HashSet<>();
      for (String knownParameter : knownParametersList) {
         knownParameters.add(knownParameter);
      }
      this.knownParameters = knownParameters;
      this.knownParameters.add(lsbParameter);
   }
   
   BaseDistribution() {
      this(new HashSet<>());
   }
   
   ////////////////////
   // parameter utils
   
   private int getIntParameter(Map<String, String> parameters, String name, boolean required, int defaultValue) {
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
   
   protected int getIntParameter(Map<String, String> parameters, String name) {
      return getIntParameter(parameters, name, true, 0);
   }
   
   // get*()
   
   @Override
   public int[] getInt(int size, Map<String, String> parameters) {
      int[] a;
      
      a = new int[size];
      if (size > 0) {
         fill(a, 0, a.length, parameters);
      }
      return a;
   }

   @Override
   public long[] getLong(int size, Map<String, String> parameters) {
      long[] a;
      
      a = new long[size];
      if (size > 0) {
         fill(a, 0, a.length, parameters);
      }
      return a;
   }

   @Override
   public double[] getDouble(int size, Map<String, String> parameters) {
      double[] a;
      
      a = new double[size];
      if (size > 0) {
         fill(a, 0, a.length, parameters);
      }
      return a;
   }

   @Override
   public String[] getString(int size, Map<String, String> parameters) {
      String[] a;
      
      a = new String[size];
      if (size > 0) {
         fill(a, 0, a.length, parameters);
      }
      return a;
   }
   
   @Override
   public UUID[] getUUID(int size, Map<String, String> parameters) {
      UUID[] a;
      
      a = new UUID[size];
      if (size > 0) {
         fill(a, 0, a.length, parameters);
      }
      return a;
   }
   
   @Override
   public IndexSortableString[] getIndexSortableString(int size, Map<String, String> parameters) {
      IndexSortableString[] iss;
      String[] s;
      
      s = getString(size, parameters);
      iss = new IndexSortableString[s.length];
      for (int i = 0; i < iss.length; i++) {
         iss[i] = new IndexSortableString(s[i]);
      }
      return iss;
   }

   
   @Override
   public Set<String> getKnownParameters() {
      return knownParameters;
   }
   
   /////////////////////////////////////
   
   @Override
   public List<String> getStringList(int size, Map<String, String> parameters) {
      return Arrays.asList(getString(size, parameters));
   }
   
   ///////////////////////////////////
   // BaseDistribution implements random filling
   // this is used by both RandomDistribution and by subclasses of BaseDistribution
   
   private int getIntLSBMask(int bits) {
      return 0xffffffff >>> (bitsPerInt - bits);
   }
   
   private int getIntLSBMask(Map<String, String> parameters) {
      String   lsbValue;
      int      lsb;
      
      lsbValue = parameters.get(lsbParameter);
      if (lsbValue == null) {
         lsb = bitsPerInt;
      } else {
         lsb = Integer.parseInt(lsbValue);
         if (lsb < 0) {
            throw new RuntimeException("lsb < 0");
         }
         if (lsb > bitsPerInt) {
            throw new RuntimeException("lsb > "+ bitsPerInt);
         }
      }
      return getIntLSBMask(lsb);
   }
   
   private long getLongLSBMask(int bits) {
      return 0xffffffffffffffffL >>> (bitsPerLong - bits);
   }
   
   private long getLongLSBMask(Map<String, String> parameters) {
      String   lsbValue;
      int      lsb;
      
      lsbValue = parameters.get(lsbParameter);
      if (lsbValue == null) {
         lsb = bitsPerLong;
      } else {
         lsb = Integer.parseInt(lsbValue);
         if (lsb < 0) {
            throw new RuntimeException("lsb < 0");
         }
         if (lsb > bitsPerLong) {
            throw new RuntimeException("lsb > "+ bitsPerLong);
         }
      }
      return getLongLSBMask(lsb);
   }
   
   @Override
   public void fill(int[] a, int i0, int i1, Map<String, String> parameters) {
      int   lsbMask;
      
      lsbMask = getIntLSBMask(parameters);      
      for (int i = i0; i < i1; i++) {
         a[i] = ThreadLocalRandom.current().nextInt() & lsbMask;
      }
   }

   @Override
   public void fill(long[] a, int i0, int i1, Map<String, String> parameters) {
      long   lsbMask;
      
      lsbMask = getLongLSBMask(parameters);      
      for (int i = i0; i < i1; i++) {
         a[i] = ThreadLocalRandom.current().nextLong() & lsbMask;
      }
   }

   @Override
   public void fill(double[] a, int i0, int i1, Map<String, String> parameters) {
      for (int i = i0; i < i1; i++) {
         a[i] = ThreadLocalRandom.current().nextLong();
      }
   }
   
   private static String createRandomString(int length) {
      StringBuilder  sb;
      
      sb = new StringBuilder();
      for (int i = 0; i < length; i++) {
         sb.append((char)('a' + ThreadLocalRandom.current().nextInt(alphabetSize)));
      }
      return sb.toString();
   }

   @Override
   public void fill(String[] a, int i0, int i1, Map<String, String> parameters) {
      int   length;
      
      length = getIntParameter(parameters, stringLengthParameter);
      for (int i = i0; i < i1; i++) {
         a[i] = createRandomString(length);
      }
   }
   
   @Override
   public void fill(UUID[] a, int i0, int i1, Map<String, String> parameters) {
      for (int i = i0; i < i1; i++) {
         a[i] = UUID.randomUUID();
      }
   }  
}
