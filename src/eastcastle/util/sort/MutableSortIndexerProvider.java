package eastcastle.util.sort;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Provides SortIndexers for default supported classes
 */
public class MutableSortIndexerProvider implements SortIndexerProvider {
   @SuppressWarnings("rawtypes")
   private final ConcurrentMap<Class, SortIndexer> providerMap;
   
   public MutableSortIndexerProvider() {
      providerMap = new ConcurrentHashMap<>();
   }
   
   @SuppressWarnings("rawtypes")
   public MutableSortIndexerProvider(Map<Class, SortIndexer> sourceMap) {
      providerMap = new ConcurrentHashMap<>();
      providerMap.putAll(sourceMap);
   }
   
   @SuppressWarnings("rawtypes")
   public void putAll(Map<Class, SortIndexer> sourceMap) {
      providerMap.putAll(sourceMap);
   }
   
   @SuppressWarnings("rawtypes")
   public void put(Class _class, SortIndexer sortIndexer) {
      providerMap.put(_class, sortIndexer);
   }
   
   @SuppressWarnings("unchecked")
   @Override
   public <T extends Comparable<? super T>> SortIndexer<T> getSortIndexer(Class<T> c) {
      return (SortIndexer<T>)providerMap.get(c);
   }
}
