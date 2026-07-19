import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class Problem_731_Difficulty_11 {

    /*
     * Problem statement:
     * A = sum_{i=1}^{infinity} 1 / (3^i * 10^(3^i))
     * A(n) is the 10 decimal digits of A from the n-th digit onward.
     * Find A(10^16).
     *
     * Solution: Modular Exponentiation and Infinite Series Truncation
     * Step 1: We are looking for the fractional part of 10^(n-1) * A. 
     *         Let's multiply the series by 10^(n-1):
     *         10^(n-1) * A = sum_{i=1}^{infinity} 10^(n - 1 - 3^i) / 3^i
     * Step 2: Split the sum into two parts based on whether 3^i <= n - 1 or 3^i > n - 1.
     * Step 3: For 3^i <= n - 1:
     *         The exponent E = n - 1 - 3^i is non-negative.
     *         The fractional part of 10^E / 3^i is simply (10^E mod 3^i) / 3^i.
     *         We compute this efficiently using modular exponentiation (BigInteger.modPow).
     * Step 4: For 3^i > n - 1:
     *         The exponent is negative, meaning the term evaluates to 1 / (3^i * 10^(3^i - n + 1)).
     *         Because 3^i grows exponentially, these terms shrink incredibly fast.
     *         If the offset (3^i - n + 1) > 20, the term is less than 10^-20, making 
     *         its contribution to the first 10 decimal digits strictly zero. We can safely halt.
     * Step 5: Sum all these fractional parts, take the remainder modulo 1, multiply by 10^10, 
     *         and extract the integer part to get exactly the 10 requested digits.
     *
     * Time Complexity: O(log_3(n) * M) where M is the time for modular exponentiation.
     *                  For n = 10^16, the loop only runs ~33 times and finishes in milliseconds.
     * Space Complexity: O(1) beyond the memory used for BigInteger allocations.
     */

    public static void main(String[] args) {
        System.out.println(solve());
    }

    private static String solve() {
        long n = 10000000000000000L; // 10^16
        BigInteger bigNMinus1 = BigInteger.valueOf(n - 1);
        BigInteger ten = BigInteger.TEN;
        
        BigDecimal fractionalSum = BigDecimal.ZERO;
        
        int i = 1;
        while (true) {
            BigInteger mod = BigInteger.valueOf(3).pow(i);
            
            if (mod.compareTo(bigNMinus1) > 0) {
                // When 3^i > n - 1, the exponent becomes negative
                BigInteger diff = mod.subtract(bigNMinus1);
                
                // If the negative shift pushes the value past 20 decimal places, 
                // it can't possibly affect our target 10 digits. Stop iterating.
                if (diff.compareTo(BigInteger.valueOf(20)) > 0) {
                    break;
                }
                
                // Compute the tiny fractional contribution directly
                BigDecimal denom = new BigDecimal(mod).multiply(BigDecimal.TEN.pow(diff.intValue()));
                BigDecimal term = BigDecimal.ONE.divide(denom, 40, RoundingMode.DOWN);
                fractionalSum = fractionalSum.add(term);
                
            } else {
                // When 3^i <= n - 1, extract the fractional part using modulo arithmetic
                BigInteger exponent = bigNMinus1.subtract(mod);
                BigInteger rem = ten.modPow(exponent, mod);
                
                // Divide the remainder by the modulus to get the fractional decimal value
                BigDecimal term = new BigDecimal(rem).divide(new BigDecimal(mod), 40, RoundingMode.DOWN);
                fractionalSum = fractionalSum.add(term);
            }
            
            i++;
        }
        
        // Strip the whole number part to isolate the combined fractional remainder
        fractionalSum = fractionalSum.remainder(BigDecimal.ONE);
        
        // Shift the decimal point 10 places to the right to surface the target digits
        BigDecimal shifted = fractionalSum.multiply(new BigDecimal("10000000000"));
        
        // Convert to a long and format with leading zeroes (in case the sequence starts with 0)
        long digits = shifted.longValue();
        return String.format("%010d", digits);
    }
}
