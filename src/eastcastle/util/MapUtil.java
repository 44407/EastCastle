package eastcastle.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MapUtil {
   public enum NoDelimiterAction {
      Ignore, Warn, Exception
   };
   
   public static Map<String, String> parseStringMap(String s, char delimiter, char mapChar, NoDelimiterAction noDelimiterAction) {
      try {
         return parseStringMap(new ByteArrayInputStream(s.replace(delimiter, '\n').getBytes()), mapChar, noDelimiterAction);
      } catch (IOException ioe) {
         throw new RuntimeException(ioe);
      }
   }

   public static Map<String, String> parseStringMap(InputStream in, char mapChar, NoDelimiterAction noDelimiterAction)
         throws IOException {
      return parseMap(in, mapChar, noDelimiterAction, new StringParser(), new StringParser());
   }

   public static <K, V> Map<K, V> parseMap(InputStream in, char mapChar, NoDelimiterAction noDelimiterAction,
         Function<String, K> keyParser, Function<String, V> valueParser) throws IOException {
      BufferedReader reader;
      String line;
      int lineNumber;
      Map<K, V> map;

      map = new HashMap<>();
      lineNumber = 0;
      reader = new BufferedReader(new InputStreamReader(in));
      do {
         ++lineNumber;
         line = reader.readLine();
         if (line != null) {
            int dIndex;

            dIndex = line.indexOf(mapChar);
            if (dIndex >= 0) {
               String kDef;
               String vDef;

               kDef = line.substring(0, dIndex);
               vDef = line.substring(dIndex + 1);
               map.put(keyParser.apply(kDef), valueParser.apply(vDef));
            } else {
               switch (noDelimiterAction) {
               case Ignore:
                  break;
               case Warn:
                  //Log.warningf("No delimiter found on line %d", lineNumber);
                  break;
               case Exception:
                  throw new RuntimeException("No delimiter found on line: " + lineNumber);
               default:
                  throw new RuntimeException("Panic");
               }
            }
         }
      } while (line != null);
      return map;
   }

   private static class StringParser implements Function<String, String> {
      @Override
      public String apply(String s) {
         return s;
      }
   }
}