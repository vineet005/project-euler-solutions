public class Problem_251_Difficulty_24 {

    /*
     * Problem statement:
     * Find how many Cardano Triplets (a,b,c) exist such that 
     * a + b + c <= 110,000,000. A Cardano Triplet satisfies:
     * cbrt(a + b*sqrt(c)) + cbrt(a - b*sqrt(c)) = 1
     *
     * Solution: Algebraic Reduction & Prime Sieve
     * Step 1: Simplify the given equation algebraically to get:
     *         27b^2*c = (a+1)^2 * (8a-1)
     * Step 2: For integers to satisfy this, a must be congruent to 2 mod 3.
     *         Substitute a = 3k - 1, which reduces the equation to:
     *         b^2*c = k^2 * (8k-3)
     * Step 3: Given a + b + c <= 110,000,000, we find the upper bound for k 
     *         is approximately 9,166,667.
     * Step 4: For each k, we must find the square-free decomposition of 8k-3.
     *         We use a sieve to find the largest square factor of 8k-3 to efficiently
     *         count valid pairs of (b,c).
     *
     * Time Complexity: O(K log(log K)) where K is the max limit of k.
     * Space Complexity: O(K) for the square factor sieve.
     */

    public static void main(String[] args) {
        System.out.println(countCardanoTriplets());
    }

    private static final int LIMIT = 110000000;

    private static long countCardanoTriplets() {
        int maxK = (LIMIT + 4) / 12;
        
        // Sieve to store the largest square divisor for numbers up to 8*maxK
        int maxVal = 8 * maxK;
        int[] largestSquareDivisor = new int[maxVal + 1];
        for (int i = 1; i <= maxVal; i++) {
            largestSquareDivisor[i] = 1;
        }

        for (int i = 2; i * i <= maxVal; i++) {
            int sq = i * i;
            for (int j = sq; j <= maxVal; j += sq) {
                largestSquareDivisor[j] = i;
            }
        }

        long count = 0;

        for (long k = 1; k <= maxK; k++) {
            long a = 3 * k - 1;
            long val = 8 * k - 3;
            
            long m = largestSquareDivisor[(int)val];
            
            // The maximum value 'b' can take is k * m. We iterate through divisors
            // However, b must also satisfy a + b + c <= LIMIT
            long maxB = k * m;
            
            // Optimization: Iterate through divisors of k*m to extract valid b values
            // (Abstracted logic for counting divisors that satisfy the bounding constraint)
            count += countValidBDivisors(a, k, val, maxB);
        }

        return count;
    }

    private static long countValidBDivisors(long a, long k, long val, long maxB) {
        long validPairs = 0;
        for (long i = 1; i * i <= maxB; i++) {
            if (maxB % i == 0) {
                long b1 = i;
                long c1 = (k * k * val) / (b1 * b1);
                if (a + b1 + c1 <= LIMIT) validPairs++;

                long b2 = maxB / i;
                if (b1 != b2) {
                    long c2 = (k * k * val) / (b2 * b2);
                    if (a + b2 + c2 <= LIMIT) validPairs++;
                }
            }
        }
        return validPairs;
    }
}
