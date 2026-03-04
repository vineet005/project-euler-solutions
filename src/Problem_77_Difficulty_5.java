public class Problem_77_Difficulty_5 {

    /*
     * Problem statement:
     * Find the first number that can be written as the sum of primes
     * in over 5000 different ways.
     *
     *
     * We must count only combinations (order does not matter).
     *
     *
     * Solution 1: Standard Dynamic Programming (Prime Partition Count)
     * Step 1: Generate all primes up to a safe upper bound.
     * Step 2: Use a DP array where ways[i] = number of ways to write i as sum of primes.
     * Step 3: For each prime p, update:
     *         ways[i] += ways[i - p]
     *
     * This is identical to the coin‑change combination algorithm.
     *
     * Time Complexity: O(n * number_of_primes)
     * Space Complexity: O(n)
     *
     * Issue:
     * We must guess an upper bound (like 100 or 200) and compute all ways[] up to that.
     * Works fine, but does unnecessary work.
     *
     *
     * Solution 2 (Optimized): Incremental n + On‑Demand Prime Generation
     * Optimization 1: Instead of generating all primes upfront, generate primes
     *                 only when needed (simple isPrime check).
     *
     * Optimization 2: For each n, create a DP array of size n+1 only.
     *
     * Optimization 3: Stop immediately when ways[n] > 5000.
     *
     * Why this is faster:
     * - No large sieve
     * - No large DP array
     * - No wasted computation beyond the answer
     *
     * Time Complexity: Much lower in practice, since n stops at 71.
     * Space Complexity: O(n) with minimal memory footprint.
     *
     *
     */

    static void main() {
        findFirstPrimePartitionAbove();
    }

    private static final int THRESHOLD = 5000;

    private static void findFirstPrimePartitionAbove() {
        int n = 2;
        java.util.List<Integer> primes = new java.util.ArrayList<>();

        while (true) {
            if (isPrime(n)) primes.add(n);

            int[] ways = new int[n + 1];
            ways[0] = 1;

            for (int p : primes) {
                for (int i = p; i <= n; i++) {
                    ways[i] += ways[i - p];
                }
            }

            if (ways[n] > THRESHOLD) {
                System.out.println(n);
                break;
            }

            n++;
        }
    }

    private static boolean isPrime(int x) {
        if (x < 2) return false;
        if (x % 2 == 0) return x == 2;
        for (int i = 3; i * i <= x; i += 2) {
            if (x % i == 0) return false;
        }
        return true;
    }
}
