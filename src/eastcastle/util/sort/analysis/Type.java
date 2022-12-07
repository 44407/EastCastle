package eastcastle.util.sort.analysis;

import java.util.ArrayList;
import java.util.List;

public enum Type {
   BooleanPrimitiveArray ("boolean[]"), 
   BytePrimitiveArray ("byte[]"), 
   ShortPrimitiveArray ("short[]"), 
   IntegerPrimitiveArray ("int[]"), 
   LongPrimitiveArray ("long[]"), 
   FloatPrimitiveArray ("float[]"), 
   DoublePrimitiveArray ("double[]"), 
   StringArray ("String[]"), 
   IndexSortableStringArray ("IndexSortableString[]"), 
   UUIDArray ("UUID[]"), 
   ByteBoxedArray ("Byte[]"),
   ShortBoxedArray ("Short[]"),
   IntegerBoxedArray ("Integer[]"),
   LongBoxedArray ("Long[]"),
   FloatBoxedArray ("Float[]"),
   DoubleBoxedArray ("Double[]"),
   IntegerList ("List<Integer>"), 
   StringList ("List<String>"), 
   ;
   
   private final String name;
   
   private Type(String name) {
      this.name = name;
   }
   
   public String getName() {
      return name;
   }   
   
   public static Type fromName(String name) {
      for (Type type : Type.values()) {
         if (type.name.equals(name)) {
            return type;
         }
      }
      return null;
   }
   
   public boolean isList() {
      switch (this) {
      case IntegerList: return true;
      case StringList: return true;
      default: return false;
      }
   }
   
   public static List<Type> parseList(String def) {
      String[] defs;
      List<Type>  vals;
      
      defs = def.split(",");
      vals = new ArrayList<>(defs.length);
      for (String s : defs) {
         vals.add(fromName(s));
      }
      return vals;
   }
}
