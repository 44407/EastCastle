package eastcastle.util.sort;

import java.util.UUID;

public class UUIDSortIndexer implements SortIndexer<UUID> {
   public static final UUIDSortIndexer   instance = new UUIDSortIndexer();
   
   private UUIDSortIndexer() {
   }
   
   @Override
   public boolean indicesAreSigned() {
      return true;
   }
   
   @Override
   public int compare(UUID u0, UUID u1) {
      return u0.compareTo(u1);
   }
   
   private int getMostSignificantBytesAsInt(UUID u) {
      return (int)(u.getMostSignificantBits() >>> 32);
   }
   
   @Override
   public int sortIndex(UUID u) {
      return getMostSignificantBytesAsInt(u);
   }
}
