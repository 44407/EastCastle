package eastcastle.util.sort.analysis;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import eastcastle.util.sort.RadJSort;

public class TestStringSort {
   private static final Charset   latin1 = Charset.forName("ISO-8859-1");
   
   public TestStringSort() {
   }
   
   public String clean(String s) {
      StringBuilder  sb;

      sb = new StringBuilder();      
      for (int i = 0; i < s.length(); i++) {         
         if (Character.isAlphabetic(s.charAt(i)) && s.charAt(i) <= 127) {
            sb.append(s.charAt(i));
         }         
      }
      return sb.toString();
   }
      
   public String[] getFileWordsAsArray(String fileName) {
      return getFileWordsAsList(fileName).toArray(new String[0]);
   }
   
   public List<String> getFileWordsAsList(String fileName) {
      try {
      List<String>   lines;
      List<String>   words;
      
      words = new ArrayList<>();
      lines = Files.readAllLines(new File(fileName).toPath(), latin1);
      for (String line : lines) {
         String[] toks;
         
         toks = line.split("\\s+");
         for (String tok : toks) {
            String   t;
            
            t = clean(tok);
            if (t.length() > 4) {
               words.add(t);
            }            
         }
      }
      return words;
      } catch (Exception e) {
         e.printStackTrace();
         throw new RuntimeException(e);
      }
   }
   
   private boolean sanityCheck(String[] a) {
      String   prev;
      
      prev = a[0];
      for (int i = 0; i < a.length; i++) {
         if (prev.compareTo(a[i]) == 1) {
            return false;
         }
         prev = a[i];
      }
      return true;
   }
   
   public void countWords(String[] words) {
      int   count;
      String   prev;
      
      count = 1;
      prev = words[0];
      for (int i = 1; i < words.length; i++) {
         String   cur;
         
         cur = words[i];
         if (cur.equals(prev)) {
            ++count;
         } else {
            System.out.printf("%s\t%d\n", prev, count);
            count = 1;
            prev = cur;
         }
      }
      System.out.printf("%s\t%d\n", prev, count);
   }
   
   public void test(String fileName) {
      RadJSort radJSort;
      String[] words;
      
      radJSort = RadJSort.defaultInstance;
      words = getFileWordsAsArray(fileName);
      radJSort.sort(words);
      countWords(words);
      System.out.printf("#words %d\tsanity check %s\n", words.length, sanityCheck(words));
   }
   
   
   public static void main(String[] args) {
      if (args.length != 1) {
         System.out.println("args: <file>");
      } else {
         TestStringSort testStringSort;
         String         fileName;
         
         fileName = args[0];
         testStringSort = new TestStringSort();
         testStringSort.test(fileName);
      }
   }
}
