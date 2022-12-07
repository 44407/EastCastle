package eastcastle.util.sort;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Provides SortIndexers for default supported classes
 */
class DefaultSortIndexerProvider implements SortIndexerProvider {
   @SuppressWarnings("rawtypes")
   private final ConcurrentMap<Class, SortIndexer> providerMap;
   
   static final DefaultSortIndexerProvider instance = new DefaultSortIndexerProvider(); 
   
   private DefaultSortIndexerProvider() {
      providerMap = new ConcurrentHashMap<>();
      providerMap.put(Byte.class, ByteSortIndexer.instance);
      providerMap.put(Short.class, ShortSortIndexer.instance);
      providerMap.put(Integer.class, IntegerSortIndexer.instance);
      providerMap.put(Long.class, LongSortIndexer.instance);
      providerMap.put(Float.class, FloatSortIndexer.instance);
      providerMap.put(Double.class, DoubleSortIndexer.instance);
      providerMap.put(String.class, StringSortIndexer.instance);
      providerMap.put(UUID.class, UUIDSortIndexer.instance);
   }
   
   @SuppressWarnings("unchecked")
   @Override
   public <T extends Comparable<? super T>> SortIndexer<T> getSortIndexer(Class<T> c) {
      return (SortIndexer<T>)providerMap.get(c);
   }
}
