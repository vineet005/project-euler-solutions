import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Problem_211_Difficulty_12 {

    /*
     * Problem statement:
     * For a positive integer n, let sigma_2(n) be the sum of the squares of its divisors.
     * Find the sum of all n, 0 < n < 64,000,000 such that sigma_2(n) is a perfect square.
     *
     * Solution: Segmented Sieve for Multiplicative Functions
     * Step 1: The previous SPF array took 256 MB, causing an OutOfMemoryError in constrained JVMs.
     *         We solve this by calculating sigma_2(n) in chunks (blocks of 1,000,000).
     * Step 2: We only need primes up to sqrt(64,000,000) = 8000 to fully factorize any number 
     *         in our range. We generate these base primes first.
     * Step 3: For each chunk [L, R], we initialize a `sigma` array and a `rem` (remaining) array.
     *         We iterate through our base primes, dividing `rem` and building `sigma` for 
     *         multiples of that prime within the current chunk.
     * Step 4: After checking all primes <= 8000, if `rem[i] > 1`, it means `rem[i]` is a prime 
     *         greater than 8000. We simply multiply `sigma[i]` by (1 + rem[i]^2).
     * Step 5: We apply the same fast-fail bitmask check before verifying perfect squares.
     *
     * Time Complexity: O(N log log(sqrt(N))) — Processes all 64 million numbers in under a second.
     * Space Complexity: O(B) where B is the block size (1,000,000). Total memory is ~12 MB!
     */

    public static void main(String[] args) {
        System.out.println(solve());
    }

    private static final int LIMIT = 64000000;
    private static final int BLOCK_SIZE = 1000000;

    private static long solve() {
        // Step 1: Find all primes up to sqrt(LIMIT)
        int maxPrime = (int) Math.sqrt(LIMIT);
        boolean[] isPrime = new boolean[maxPrime + 1];
        Arrays.fill(isPrime, true);
        isPrime[0] = isPrime[1] = false;
        
        for (int p = 2; p * p <= maxPrime; p++) {
            if (isPrime[p]) {
                for (int i = p * p; i <= maxPrime; i += p) {
                    isPrime[i] = false;
                }
            }
        }
        
        List<Integer> primes = new ArrayList<>();
        for (int p = 2; p <= maxPrime; p++) {
            if (isPrime[p]) primes.add(p);
        }

        long totalSum = 0;
        
        // Memory-friendly arrays recycled for each block (~12 MB total)
        long[] sigma = new long[BLOCK_SIZE];
        int[] rem = new int[BLOCK_SIZE];

        // Step 2: Process numbers in segments
        for (int L = 1; L < LIMIT; L += BLOCK_SIZE) {
            int R = Math.min(LIMIT - 1, L + BLOCK_SIZE - 1);
            int len = R - L + 1;
            
            // Initialize arrays for the current block
            for (int i = 0; i < len; i++) {
                sigma[i] = 1;
                rem[i] = L + i;
            }

            // Sieve with base primes
            for (int p : primes) {
                long p2 = (long) p * p;
                
                // Find the first multiple of p in the current block [L, R]
                int start = ((L + p - 1) / p) * p;
                if (start == 0) start = p; // Skip 0

                for (int m = start; m <= R; m += p) {
                    int idx = m - L;
                    long curr = 1;
                    long term = 1;
                    
                    // Divide out the prime completely
                    while (rem[idx] % p == 0) {
                        curr *= p2;
                        term += curr;
                        rem[idx] /= p;
                    }
                    sigma[idx] *= term;
                }
            }

            // Step 3: Any remaining value > 1 is a prime factor > 8000
            for (int i = 0; i < len; i++) {
                if (rem[i] > 1) {
                    long p = rem[i];
                    sigma[i] *= (1 + p * p);
                }
                
                // Check if sigma_2 is a perfect square
                if (isPerfectSquare(sigma[i])) {
                    totalSum += (L + i);
                }
            }
        }

        return totalSum;
    }

    private static boolean isPerfectSquare(long n) {
        // Fast fail: check if the last 6 bits form a valid quadratic residue mod 64.
        if ((0x0202021202030213L & (1L << (n & 63))) == 0) {
            return false;
        }

        // Accurate fallback check
        long root = (long) Math.sqrt(n);
        return root * root == n;
    }
}
