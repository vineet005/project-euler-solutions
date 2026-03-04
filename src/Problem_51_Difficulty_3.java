import java.util.Arrays;

public class Problem_51_Difficulty_3 {
    /*
    * Problem Statement:
    * Replace certain digits of a prime with the same
    * digit (0-9).
    * If this produces a "family" of primes os size 8,
    * find the smallest such prime.
    *
    * Goal:
    * Find the smallest prime which is part of an
    * EIGHT-prime digit-replacement family.
    *
    * Solution 1:
    * Step 1 - Generate primes up to a limit (eg: 2 million)
    * Step 2: For each prime:
    *  - Try ALL digit masks(bitmasks)
    *  - Replace masked digits with 0-9
    *  - Count how many results are prime
    *
    * Issues with this solution:
    *  - Too many masks
    *  - Many masks produce invalid numbers
    *  - Most masks cannot possibly reach 8 primes
    *  - Very slow: millions of unnecessary checks
    *
    * Solution 2: Optimized
    * Key Observations:
    * 1. Only digits 0,1 or 2 need to be replaced.
    * Reason: To get 8 primes, the replaced digit must appear
    * multiple times. Higher digits rarely repeats enough.
    *
    * 2. The repeated digit must appear at least 3 times.
    * Otherwise, replacing it 0-9 cannot yield 8 primes
    *
    * 3. Skip masks that replace the first digit with 0.
    *
    * 4. Early termination:
    * if remaining replacements cannot reach 8 primes,
    * break immediately.
    *
    * 5. Use a fast sieve for primality lookup.
    *
    * Result:
    * - Search space reduced by ~95%.
    * - Runs in milliseconds
    * - Produces correct answer.
    * */

    static void main() {
        System.out.println(findSmallestPrimeInFamily());
    }

    private static final int LIMIT = 2_000_000;

    private static int findSmallestPrimeInFamily(){
        boolean[] isPrime = sieve(LIMIT);

        for(int p = 11; p < LIMIT; p += 2){
            if(!isPrime[p]) continue;

            char[] digits = String.valueOf(p).toCharArray();
            int len = digits.length;

            // Only replace digits 0, 1, 2
            for(char target: new char[]{'0', '1', '2'}){
                int count = 0; // Count Occurrences of target digit
                for(char d: digits) if(d == target) count++;

                if(count < 3) continue;

                int[] pos = new int[count];
                int idx = 0;
                for(int i = 0; i < len; i++){
                    if(digits[i] == target) pos[idx++] = i;
                }

                int familyCount = 0;
                int smallestPrime = -1;

                for(int r = 0; r <= 9; r++){
                    if(pos[0] == 0 && r == 0) continue;

                    char[] newDigits = digits.clone();
                    for(int position: pos){
                        newDigits[position] = (char) ('0' + r);
                    }

                    int candidate = Integer.parseInt(new String(newDigits));

                    if(candidate < LIMIT && isPrime[candidate]){
                        familyCount++;
                        if(smallestPrime == -1) smallestPrime = candidate;
                    }

                    if(familyCount + (9-r) < 8) break;
                }

                if(familyCount == 8) return smallestPrime;
            }
        }

        return -1;
    }

    private static boolean[] sieve(int n){
        boolean[] prime = new boolean[n+1];
        Arrays.fill(prime, true);
        prime[0] = prime[1] = false;

        for(int i = 2; i * i <= n; i++){
            if(prime[i]){
                for(int j = i*i; j <= n; j+= i){
                    prime[j] = false;
                }
            }
        }

        return prime;
    }
}
