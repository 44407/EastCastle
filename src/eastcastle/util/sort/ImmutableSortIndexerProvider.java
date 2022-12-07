package eastcastle.util.sort;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Provides SortIndexers for default supported classes
 */
public class ImmutableSortIndexerProvider implements SortIndexerProvider {
   @SuppressWarnings("rawtypes")
   private final ConcurrentMap<Class, SortIndexer> providerMap;
   
   @SuppressWarnings("rawtypes")
   public ImmutableSortIndexerProvider(Map<Class, SortIndexer> sourceMap) {
      providerMap = new ConcurrentHashMap<>();
      providerMap.putAll(sourceMap);
   }
   
   @SuppressWarnings("unchecked")
   @Override
   public <T extends Comparable<? super T>> SortIndexer<T> getSortIndexer(Class<T> c) {
      return (SortIndexer<T>)providerMap.get(c);
   }
}
