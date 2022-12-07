package eastcastle.util;

public class NumUtil {
   /**
    * Return the next integer >= n that is a power of 2
    * @return
    */
   public static int nextPowerOf2(int n) {
      return 1 << nextPowerOf2Exponent(n);    
   }
   
   /**
    * Return the base 2 exponent of the next integer >= n that is a power of 2
    * @return
    */
   public static int nextPowerOf2Exponent(int n) {
      return n == 0 ? 0 : 32 - Integer.numberOfLeadingZeros(n - 1);    
   }
   
   public static void main(String[] args) {
      for (int i = 0; i <= 32; i++) {
         System.out.printf("%d\t%d\t%d\n", i, nextPowerOf2Exponent(i), nextPowerOf2(i));
      }
   }
}
