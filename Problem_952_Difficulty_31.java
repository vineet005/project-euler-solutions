public class Problem_952_Difficulty_31 {

    /*
     * Problem statement:
     * Given a prime p and a positive integer n < p, let R(p, n) be the 
     * multiplicative order of p modulo n!. Find R(10^9 + 7, 10^7) modulo 10^9 + 7.
     *
     * Solution: Lifting the Exponent Lemma (LTE) + Primorials
     * Step 1: The multiplicative order R(p, n) is the LCM of the orders of p 
     *         modulo each prime power q^k dividing n!.
     * Step 2: Using LTE, we establish that the power of q in the LCM simplifies 
     *         to max(0, v_q(n!) - e_q), where e_q = v_q(p^{O_q} - 1).
     * Step 3: For the vast majority of primes, this just means we are dropping 
     *         one power of q. This makes the LCM highly related to n! itself.
     * Step 4: We calculate the denominator as a product of dropped primes. 
     *         We specifically check for Wieferich primes (where p^(q-1) = 1 mod q^2).
     * Step 5: Answer is evaluated as (n! * modInverse(denominator)) % (10^9 + 7).
     *
     * Time Complexity: O(n) to compute the factorial and sieve.
     * Space Complexity: O(n) for the prime sieve up to 10^7.
     */

    public static void main(String[] args) {
        System.out.println(solve());
    }

    private static final int N = 10000000;
    private static final long MOD = 1000000007L;

    private static long solve() {
        boolean[] sieve = new boolean[N + 1];
        java.util.Arrays.fill(sieve, true);
        java.util.List<Integer> primes = new java.util.ArrayList<>();
        
        for (int p = 2; p <= N; p++) {
            if (sieve[p]) {
                primes.add(p);
                // Use long to prevent integer overflow in loop
                for (long i = (long) p * p; i <= N; i += p) {
                    sieve[(int) i] = false;
                }
            }
        }

        long denominator = 1;

        for (int r : primes) {
            // Legendre's formula for k_r = v_r(n!)
            long k_r = 0;
            long temp = N;
            while (temp > 0) {
                k_r += temp / r;
                temp /= r;
            }

            // Determine e_r
            long e_r;
            if (r == 2) {
                e_r = 3;
            } else {
                e_r = 1;
                long currentMod = (long) r * r;
                
                // Check for Wieferich primes using Fermat's Little Theorem
                while (modPow(MOD, r - 1, currentMod) == 1) {
                    e_r++;
                    currentMod *= r;
                }
            }

            long drop_r = Math.min(k_r, e_r);
            denominator = (denominator * modPow(r, drop_r, MOD)) % MOD;
        }

        // Calculate n! modulo MOD
        long factorial = 1;
        for (int i = 1; i <= N; i++) {
            factorial = (factorial * i) % MOD;
        }

        // Multiply by modular inverse
        return (factorial * modPow(denominator, MOD - 2, MOD)) % MOD;
    }

    private static long modPow(long base, long exp, long mod) {
        long result = 1;
        base = base % mod;
        while (exp > 0) {
            if ((exp & 1) == 1) {
                result = (result * base) % mod;
            }
            base = (base * base) % mod;
            exp >>= 1;
        }
        return result;
    }
}
