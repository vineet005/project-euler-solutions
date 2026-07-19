import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Problem_622_Difficulty_14 {

    /*
     * Problem statement:
     * Let s(n) be the minimum number of consecutive riffle shuffles needed to
     * restore a deck of size n to its original configuration (n is even).
     * Find the sum of all values of n that satisfy s(n) = 60.
     *
     * Solution: Multiplicative Order and Prime Factorization
     * Step 1: A perfect riffle shuffle moves a card at index i to index 2i mod (n-1).
     *         Therefore, for the deck to return to its original configuration after 
     *         k shuffles, we must have 2^k ≡ 1 mod (n-1).
     *         This means that (n-1) must be a divisor of 2^k - 1.
     * Step 2: The problem asks for s(n) = 60, meaning the multiplicative order of 
     *         2 modulo (n-1) is exactly 60.
     *         Thus, (n-1) must divide 2^60 - 1.
     * Step 3: To ensure the order is exactly 60 and not a smaller divisor of 60,
     *         (n-1) must NOT divide 2^m - 1 for any proper divisor m of 60.
     *         The maximal proper divisors of 60 are 30, 20, and 12. 
     *         We just need to check that (n-1) does not divide 2^30 - 1, 
     *         2^20 - 1, and 2^12 - 1.
     * Step 4: We efficiently find all divisors of 2^60 - 1 by prime factorizing it 
     *         using trial division (which is instantaneous since the largest prime 
     *         factor of 2^60 - 1 is only 1321, derived from cyclotomic polynomials) 
     *         and then generating combinations of these prime factors.
     * Step 5: Sum (d + 1) for all valid divisors d.
     *
     * Time Complexity: O(sqrt(Largest Prime Factor) + D) where D is the number of 
     *                  divisors of 2^60 - 1. Computes instantly in < 1 ms.
     * Space Complexity: O(P + D) to store the prime factors and divisors.
     */

    public static void main(String[] args) {
        System.out.println(solve());
    }

    private static long solve() {
        long N = (1L << 60) - 1; // 2^60 - 1
        
        // Fast Prime Factorization
        Map<Long, Integer> primeFactors = new HashMap<>();
        long temp = N;
        for (long i = 2; i * i <= temp; i++) {
            while (temp % i == 0) {
                primeFactors.put(i, primeFactors.getOrDefault(i, 0) + 1);
                temp /= i;
            }
        }
        if (temp > 1) {
            primeFactors.put(temp, 1);
        }
        
        // Generate all divisors
        List<Long> primes = new ArrayList<>(primeFactors.keySet());
        List<Long> divisors = new ArrayList<>();
        generateDivisors(0, 1, primes, primeFactors, divisors);
        
        long sum = 0;
        
        // Target maximal proper divisors of 60
        long test30 = (1L << 30) - 1;
        long test20 = (1L << 20) - 1;
        long test12 = (1L << 12) - 1;
        
        for (long d : divisors) {
            // Check if the multiplicative order is strictly 60
            if (test30 % d == 0) continue;
            if (test20 % d == 0) continue;
            if (test12 % d == 0) continue;
            
            // If it doesn't divide the smaller factors, its order is exactly 60.
            sum += (d + 1);
        }
        
        return sum;
    }

    private static void generateDivisors(int index, long currentDivisor, 
            List<Long> primes, Map<Long, Integer> primeFactors, 
            List<Long> divisors) {
        if (index == primes.size()) {
            divisors.add(currentDivisor);
            return;
        }
        
        long p = primes.get(index);
        int maxCount = primeFactors.get(p);
        
        long pPower = 1;
        for (int i = 0; i <= maxCount; i++) {
            generateDivisors(index + 1, currentDivisor * pPower, primes, primeFactors, divisors);
            pPower *= p; // Increment the power of the prime for the next subset partition
        }
    }
}
