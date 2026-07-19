public class Problem_130_Difficulty_8 {

    /*
     * Problem statement:
     * A number consisting entirely of ones is called a repunit. R(k) is a repunit of length k.
     * A(n) is the least value of k for which R(k) is divisible by n (where gcd(n, 10) = 1).
     * Find the sum of the first 25 composite values of n for which gcd(n, 10) = 1 
     * and n - 1 is divisible by A(n).
     *
     * Solution: Iterative Modulo Arithmetic
     * Step 1: We only need to consider composite numbers n where gcd(n, 10) = 1.
     *         Since n must be coprime to 10, it cannot end in 0, 2, 4, 5, 6, or 8.
     *         We check odd numbers and skip multiples of 5.
     * Step 2: To calculate A(n), we iteratively compute R(k) mod n using the recurrence:
     *         R(k) = (R(k-1) * 10 + 1) mod n.
     *         Because gcd(n, 10) = 1, we are mathematically guaranteed to eventually 
     *         find a k <= n where R(k) = 0 mod n.
     * Step 3: Check if (n - 1) % A(n) == 0. If true and n is composite, add it to the sum.
     * Step 4: Stop exactly once we have found 25 such composite numbers.
     *
     * Time Complexity: O(M * sqrt(M) + M * A(n)) where M is the maximum number evaluated.
     *                  Since M is small (< 20,000) and A(n) is fast to compute, it runs instantly.
     * Space Complexity: O(1)
     */

    public static void main(String[] args) {
        System.out.println(solve());
    }

    private static long solve() {
        int count = 0;
        long sum = 0;
        int n = 9; // 9 is the first composite odd number

        while (count < 25) {
            // Check if coprime to 10 and composite
            if (n % 5 != 0 && !isPrime(n)) {
                int a = getA(n);
                if ((n - 1) % a == 0) {
                    count++;
                    sum += n;
                }
            }
            n += 2; // Skip even numbers
        }

        return sum;
    }

    private static int getA(int n) {
        int k = 1;
        int rem = 1 % n;
        
        while (rem != 0) {
            k++;
            rem = (rem * 10 + 1) % n;
        }
        
        return k;
    }

    private static boolean isPrime(int n) {
        if (n < 2) return false;
        if (n == 2 || n == 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
        }
        
        return true;
    }
}
