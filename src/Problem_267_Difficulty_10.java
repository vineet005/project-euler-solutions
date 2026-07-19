import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class Problem_267_Difficulty_10 {

    /*
     * Problem statement:
     * Starting with £1 of capital, you can choose a fixed proportion, f, of your capital 
     * to bet on a fair coin toss repeatedly for 1000 tosses.
     * Your return is double your bet for heads and you lose your bet for tails.
     * Choosing f to maximize your chances of having at least £1,000,000,000 after 1,000 flips, 
     * what is the chance that you become a billionaire?
     *
     * Solution: Calculus and Combinatorics
     * Step 1: Let n = 1000 be the number of flips, and k be the number of heads.
     *         The final capital is given by C(f) = (1 + 2f)^k * (1 - f)^(n - k).
     * Step 2: To maximize the chance, we need to minimize the number of heads (k) 
     *         required to reach 10^9. We find the optimal f for a given k by 
     *         setting the derivative of ln(C(f)) to 0:
     *         d/df [ k * ln(1 + 2f) + (n - k) * ln(1 - f) ] = 0
     *         Solving for f yields: f = (3k - n) / 2n
     * Step 3: By testing values of k, we evaluate C_max(k) for the optimal f.
     *         For k = 431, max capital is ~8.95 * 10^8 (strictly less than 10^9).
     *         For k = 432, max capital is ~1.36 * 10^9 (strictly greater than 10^9).
     *         Thus, we need at least 432 heads to become a billionaire.
     * Step 4: The probability of getting at least 432 heads out of 1000 fair coin 
     *         tosses is sum(C(1000, i)) / 2^1000 for i = 432 to 1000.
     * Step 5: We compute this exactly using BigInteger to sum combinations to prevent 
     *         floating point loss, then divide with BigDecimal to 12 decimal places.
     *
     * Time Complexity: O(n) to compute the combinations sequentially.
     * Space Complexity: O(1) beyond BigInteger storage requirements.
     */

    public static void main(String[] args) {
        System.out.println(solve());
    }

    private static String solve() {
        int n = 1000;
        long target = 1000000000L;
        
        // Find the minimum number of heads (k) needed to reach the target capital
        int minK = -1;
        for (int k = 1; k <= n; k++) {
            double f = (3.0 * k - n) / (2.0 * n);
            if (f > 0 && f < 1) {
                // Check if capital >= 10^9
                // Using log10 prevents overflow of double data type during evaluation
                double log10Capital = k * Math.log10(1 + 2 * f) + (n - k) * Math.log10(1 - f);
                if (log10Capital >= Math.log10(target)) {
                    minK = k;
                    break;
                }
            }
        }
        
        // Calculate the exact probability using BigInteger for combination sums
        BigInteger successfulOutcomes = BigInteger.ZERO;
        BigInteger combination = BigInteger.ONE; // represents nCr(1000, 0) initially
        
        for (int k = 0; k <= n; k++) {
            if (k >= minK) {
                successfulOutcomes = successfulOutcomes.add(combination);
            }
            // Move to next combination: nCr(n, k+1) = nCr(n, k) * (n - k) / (k + 1)
            combination = combination.multiply(BigInteger.valueOf(n - k))
                                     .divide(BigInteger.valueOf(k + 1));
        }
        
        BigInteger totalOutcomes = BigInteger.TWO.pow(n);
        
        // Compute precise division restricted to 12 decimal places using standard rounding (HALF_UP)
        BigDecimal prob = new BigDecimal(successfulOutcomes)
                .divide(new BigDecimal(totalOutcomes), 12, RoundingMode.HALF_UP);
                
        return prob.toPlainString(); 
    }
}
