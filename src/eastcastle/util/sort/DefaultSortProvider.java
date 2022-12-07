package eastcastle.util.sort;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Default Sort provider.
 * 
 * Presently provides RadJSort for classes that support RadJ.
 * Provides SystemSort for others
 */
class DefaultSortProvider implements SortProvider {
   @SuppressWarnings("rawtypes")
   private final ConcurrentMap<Class, Sort> providerMap;
   
   public static final DefaultSortProvider   instance = new DefaultSortProvider();
   
   private DefaultSortProvider() {
      providerMap = new ConcurrentHashMap<>();
      //providerMap.put(boolean.class, RadJSort.defaultInstance);
      //providerMap.put(byte.class, RadJSort.defaultInstance);
      //providerMap.put(short.class, RadJSort.defaultInstance);
      providerMap.put(int.class, RadJSort.defaultInstance);
      providerMap.put(long.class, RadJSort.defaultInstance);
      //providerMap.put(float.class, RadJSort.defaultInstance);
      providerMap.put(double.class, RadJSort.defaultInstance);
      
      //providerMap.put(Boolean.class, RadJSort.defaultInstance);
      providerMap.put(Byte.class, RadJSort.defaultInstance);
      providerMap.put(Short.class, RadJSort.defaultInstance);
      providerMap.put(Integer.class, RadJSort.defaultInstance);
      providerMap.put(Long.class, RadJSort.defaultInstance);
      providerMap.put(Float.class, RadJSort.defaultInstance);
      providerMap.put(Double.class, RadJSort.defaultInstance);
      
      providerMap.put(String.class, RadJSort.defaultInstance);
      providerMap.put(UUID.class, RadJSort.defaultInstance);
   }
   
   @Override
   public <T> Sort getSortForArrayOf(Class<T> c) {
      Sort  sort;
      
      sort = providerMap.get(c);
      if (sort == null) {
         return SystemSort.instance;
      } else {
         return sort;
      }
   }
}
