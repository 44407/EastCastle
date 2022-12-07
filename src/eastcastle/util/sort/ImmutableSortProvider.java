package eastcastle.util.sort;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ImmutableSortProvider implements SortProvider {
   @SuppressWarnings("rawtypes")
   private final ConcurrentMap<Class, Sort> providerMap;
   private final Sort   defaultSort;
   
   @SuppressWarnings("rawtypes")
   public ImmutableSortProvider(Map<Class, Sort> sourceMap, Sort defaultSort) {
      providerMap = new ConcurrentHashMap<>();
      providerMap.putAll(sourceMap);
      this.defaultSort = defaultSort;
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
