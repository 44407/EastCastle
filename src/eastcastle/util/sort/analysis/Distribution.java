package eastcastle.util.sort.analysis;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface Distribution {
   int[] getInt(int size, Map<String, String> parameters);
   long[] getLong(int size, Map<String, String> parameters);
   double[] getDouble(int size, Map<String, String> parameters);
   String[] getString(int size, Map<String, String> parameters);
   UUID[] getUUID(int size, Map<String, String> parameters);
   IndexSortableString[] getIndexSortableString(int size, Map<String, String> parameters);
   
   List<String> getStringList(int size, Map<String, String> parameters);
   
   void fill(int[] a, int i0, int i1, Map<String, String> parameters);
   void fill(long[] a, int i0, int i1, Map<String, String> parameters);
   void fill(double[] a, int i0, int i1, Map<String, String> parameters);
   void fill(String[] a, int i0, int i1, Map<String, String> parameters);
   void fill(UUID[] a, int i0, int i1, Map<String, String> parameters);
   
   Set<String> getKnownParameters();
}
