package eastcastle.util.sort.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eastcastle.util.MapUtil;

public class DistributionTypeAndParameters {
   private final DistributionType      distributionType;
   private final Map<String, String>   parameters;
   
   private static final char  listDelimiter = ',';
   private static final char  parameterListDelimiter = '|';
   private static final char  mapChar = '=';
   
   public DistributionTypeAndParameters(DistributionType distributionType, Map<String, String> parameters) {
      this.distributionType = distributionType;
      this.parameters = parameters;
   }
   
   public DistributionType getDistributionType() {
      return distributionType;
   }
   
   public Map<String, String> getParameters() {
      return parameters;
   }
   
   public DistributionTypeAndParameters parameters(Map<String, String> parameters) {
      return new DistributionTypeAndParameters(distributionType, parameters);
   }
   
   public static DistributionTypeAndParameters parse(String def) {
      int   dtEnd;
      int   pStart;
      DistributionType  dt;
      Map<String, String>  parameters;
      
      def = def.trim();
      pStart = def.indexOf('{');
      if (pStart < 0) {
         dtEnd = def.length();
         parameters = new HashMap<>();
      } else {
         int      pEnd;
         String   pDef;

         dtEnd = pStart;
         // ensure no extra start brace
         if (def.indexOf(pStart + 1, '{') >= 0) {
            throw new RuntimeException("Multiple starting braces: "+ def);
         }
         // find end brace
         pEnd = def.indexOf('}');
         if (pEnd < 0) {
            throw new RuntimeException("No end brace: "+ def);
         }
         // ensure no extra end brace
         if (def.indexOf(pEnd + 1, '}') >= 0) {
            throw new RuntimeException("Extra end brace: "+ def);
         }
         // ensure no trailing text
         if (pEnd + 1 != def.length()) {
            throw new RuntimeException("Found text after parameters: "+ def);
         }
         pDef = def.substring(pStart + 1, pEnd);
         pDef = pDef.trim();
         if (pDef.length() == 0) {
            parameters = new HashMap<>();
         } else {
            parameters = MapUtil.parseStringMap(pDef, parameterListDelimiter, mapChar, MapUtil.NoDelimiterAction.Exception);
         }
      }
      dt = DistributionType.valueOf(def.substring(0, dtEnd));
      return new DistributionTypeAndParameters(dt, parameters);
   }
   
   public static String convertNested(String s, char startDelim, char endDelim, char from, char to) {
      StringBuilder  sb;
      boolean  nested;
      
      nested = false;
      sb = new StringBuilder();
      for (char c : s.toCharArray()) {
         if (c == startDelim) {
            if (nested) {
               throw new RuntimeException("Multiple nesting not supported: "+ s);
            } else {
               nested = true;
               sb.append(c);
            }
         } else if (c == endDelim) {
            if (!nested) {
               throw new RuntimeException("End nest without start: "+ s);
            } else {
               nested = false;
               sb.append(c);
            }
         } else {
            if (!nested) {
               sb.append(c);
            } else {
               if (c == from) {
                  sb.append(to);
               } else {
                  sb.append(c);
               }
            }
         }
      }
      return sb.toString();
   }
   
   public static List<DistributionTypeAndParameters> parseList(String def) {
      String[] defs;
      List<DistributionTypeAndParameters>  vals;
      
      def = convertNested(def, '{', '}', listDelimiter, parameterListDelimiter);
      defs = def.split(",");
      vals = new ArrayList<>(defs.length);
      for (String s : defs) {
         vals.add(parse(s));
      }
      return expandRanges(vals);
   }
   
   public static List<DistributionTypeAndParameters> expandRanges(List<DistributionTypeAndParameters> raw) {
      List<DistributionTypeAndParameters> expanded;
      
      expanded = new ArrayList<>();
      for (DistributionTypeAndParameters distributionTypeAndParameters : raw) {
         expanded.addAll(expandRanges(distributionTypeAndParameters));
      }
      return expanded;
   }
   
   private static List<DistributionTypeAndParameters> expandRanges(DistributionTypeAndParameters distributionTypeAndParameters) {      
      List<DistributionTypeAndParameters> expansion;
      Map<String, String>  rawParameters;
      String[] names;
      String[][]  values;
      int[] indices;
      int   i;
      boolean  done;
      
      expansion = new ArrayList<>();
      rawParameters = distributionTypeAndParameters.getParameters();
      names = new String[rawParameters.size()];
      values = new String[rawParameters.size()][];
      indices = new int[rawParameters.size()];
      i = 0;
      for (Map.Entry<String, String> entry : rawParameters.entrySet()) {
         String   name;
         
         name = entry.getKey();
         names[i] = name;
         values[i] = expand(entry.getValue());
         ++i;
      }
      
      done = false;
      while (!done) {
         Map<String, String>  parameters;

         parameters = new HashMap<>();
         for (int j = 0; j < names.length; j++) {
            parameters.put(names[j], values[j][indices[j]]);
         }
         expansion.add(distributionTypeAndParameters.parameters(parameters));
         
         done = incrementIndices(indices, values);
      }      
      
      return expansion;
   }
   
   private static boolean incrementIndices(int[] indices, String[][] values) {
      boolean  complete;
      
      complete = true;
      for (int i = 0; i < indices.length; i++) {
         indices[i]++;
         if (indices[i] < values[i].length) {
            complete = false;
            break;
         } else {
            indices[i] = 0;
         }
      }
      return complete;
   }

   private static String[] expand(String value) {
      String[] expansion;
      int   index;
      
      index = value.indexOf('-');
      if (index < 0) {
         expansion = new String[1];
         expansion[0] = value;
      } else {
         int   i0;
         int   i1;
         
         i0 = Integer.parseInt(value.substring(0, index).trim());
         i1 = Integer.parseInt(value.substring(index + 1).trim());
         expansion = new String[i1 - i0 + 1];
         for (int i = 0; i < expansion.length; i++) {
            expansion[i] = Integer.toString(i0 + i);
         }
      }
      return expansion;
   }

   @Override
   public String toString() {
      return distributionType + (parameters.size() == 0 ? "" : parameters.toString());
   }

   public static void main(String[] args) {
      System.out.println(parseList(args[0]).toString());
   }
}
