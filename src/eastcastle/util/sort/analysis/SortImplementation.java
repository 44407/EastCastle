package eastcastle.util.sort.analysis;

import java.util.ArrayList;
import java.util.List;

public enum SortImplementation {
   RadJ, System, ParallelSystem;
   
   public static List<SortImplementation> parseList(String def) {
      String[] defs;
      List<SortImplementation>  vals;
      
      defs = def.split(",");
      vals = new ArrayList<>(defs.length);
      for (String s : defs) {
         vals.add(SortImplementation.valueOf(s));
      }
      return vals;
   }   
}
