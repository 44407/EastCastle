package eastcastle.util.sort;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MutableSortProvider implements SortProvider {
   @SuppressWarnings("rawtypes")
   private final ConcurrentMap<Class, Sort> providerMap;
   private final Sort   defaultSort;
   
   public MutableSortProvider(Sort defaultSort) {
      providerMap = new ConcurrentHashMap<>();
      this.defaultSort = defaultSort;
   }
   
   @SuppressWarnings("rawtypes")
   public MutableSortProvider(Map<Class, Sort> sourceMap, Sort defaultSort) {
      providerMap = new ConcurrentHashMap<>();
      providerMap.putAll(sourceMap);
      this.defaultSort = defaultSort;
   }
   
   @SuppressWarnings("rawtypes")
   public void putAll(Map<Class, Sort> providerMap) {
      this.providerMap.putAll(providerMap);
   }
   
   @SuppressWarnings("rawtypes")
   public void put(Class _class, Sort sort) {
      this.providerMap.put(_class, sort);
   }
   
   @Override
   public <T> Sort getSortForArrayOf(Class<T> c) {
      Sort  sort;
      
      sort = providerMap.get(c);
      if (sort == null) {
         return defaultSort;
      } else {
         return sort;
      }
   }
}
