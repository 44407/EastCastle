package eastcastle.util.sort;

import java.util.ArrayList;
import java.util.List;

/**
 * Sort order
 */
public enum Order {
   ASCENDING,
   DESCENDING;
   
   public boolean isAscending() {
      return this == ASCENDING;
   }
   
   public static List<Order> parseList(String def) {
      String[] defs;
      List<Order>  vals;
      
      defs = def.split(",");
      vals = new ArrayList<>(defs.length);
      for (String s : defs) {
         vals.add(Order.valueOf(s));
      }
      return vals;
   }   
}
