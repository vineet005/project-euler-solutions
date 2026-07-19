public class Problem_401_Difficulty_7 {

    /*
     * Problem statement:
     * Let sigma_2(n) represent the sum of the squares of the divisors of n.
     * Let SIGMA_2(n) represent the summatory function of sigma_2.
     * Find SIGMA_2(10^15) modulo 10^9.
     *
     * Solution: Square Root Decomposition
     * Step 1: Change the order of summation. The problem is equivalent to 
     *         summing d^2 * floor(n/d) for all d from 1 to n.
     * Step 2: For d <= sqrt(n), we compute the sum directly.
     * Step 3: For d > sqrt(n), the quotient m = floor(n/d) remains constant 
     *         over intervals of d. We iterate through possible quotients m 
     *         and multiply m by the sum of squares of d in that interval.
     * Step 4: Use the square pyramidal number formula for the sum of squares, 
     *         carefully handling large integers to avoid overflow before the modulo.
     *
     * Time Complexity: O(sqrt(n))
     * Space Complexity: O(1)
     */

    public static void main(String[] args) {
        System.out.println(solve());
    }

    private static final long MOD = 1000000000L;

    private static long solve() {
        long n = 1000000000000000L;
        long limit = (long) Math.sqrt(n);
        long totalSum = 0;

        // 1. Small divisors (d <= sqrt(n))
        for (long d = 1; d <= limit; d++) {
            long term = (d * d) % MOD;
            long count = (n / d) % MOD;
            totalSum = (totalSum + term * count) % MOD;
        }

        // 2. Large divisors (d > sqrt(n))
        for (long m = 1; m <= n / (limit + 1); m++) {
            long dUpper = n / m;
            long dLower = n / (m + 1);

            long sumSquares = (sumOfSquares(dUpper) - sumOfSquares(dLower) + MOD) % MOD;
            totalSum = (totalSum + (m % MOD) * sumSquares) % MOD;
        }

        return totalSum;
    }

    // Calculates sum of squares up to x modulo 10^9
    // Care must be taken dividing by 6 since 6 and 10^9 are not coprime
    private static long sumOfSquares(long x) {
        long a = x;
        long b = x + 1;
        long c = 2 * x + 1;

        if (a % 2 == 0) a /= 2; else b /= 2;
        if (a % 3 == 0) a /= 3; else if (b % 3 == 0) b /= 3; else c /= 3;

        a %= MOD;
        b %= MOD;
        c %= MOD;

        return (((a * b) % MOD) * c) % MOD;
    }
}
