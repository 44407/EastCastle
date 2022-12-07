package eastcastle.util.sort;

public class StringSortIndexer implements SortIndexer<String> {
   public static final StringSortIndexer   instance = new StringSortIndexer();
   
   private StringSortIndexer() {
   }
   

   @Override
   public boolean indicesAreSigned() {
      return false;
   }
   
   @Override
   public int compare(String s0, String s1) {
      return s0.compareTo(s1);
   }
   
   private int getStringBytesAsInt(String s) throws NotIndexSortableException {
      char  c0;
      char  c1;
      char  c2;
      char  c3;
      
      if (s.length() >= 4) {
         c0 = s.charAt(0);
         c1 = s.charAt(1);
         c2 = s.charAt(2);
         c3 = s.charAt(3);
      } else {
         c0 = 0;
         c1 = 0;
         c2 = 0;
         c3 = 0;
         switch (s.length()) {
            case 1:
               c0 = s.charAt(0);
               break;
            case 2:               
               c0 = s.charAt(0);
               c1 = s.charAt(1);
               break;
            case 3:               
               c0 = s.charAt(0);
               c1 = s.charAt(1);
               c2 = s.charAt(2);
               break;
            default: throw new RuntimeException();
         }
      }
      if (((c0 | c1 | c2 | c3) & 0xff00) != 0) {
         throw new NotIndexSortableException();
      } else {
         return (((byte)c0) << 24)
             | (((byte)c1 & 0xff) << 16)
             | ((byte)c2 & 0xff) << 8
             | ((byte)c3 & 0xff);
      }
   }
   
   @Override
   public int sortIndex(String s) throws NotIndexSortableException {
      return getStringBytesAsInt(s);
   }
}
