package eastcastle.util.sort.analysis;

import java.util.ArrayList;
import java.util.List;

public enum DistributionType {
   Random, SortedRanges, PartiallySorted, NSwapped;
   
   public static List<DistributionType> parseList(String def) {
      String[] defs;
      List<DistributionType>  vals;
      
      defs = def.split(",");
      vals = new ArrayList<>(defs.length);
      for (String s : defs) {
         vals.add(DistributionType.valueOf(s));
      }
      return vals;
   }
}
