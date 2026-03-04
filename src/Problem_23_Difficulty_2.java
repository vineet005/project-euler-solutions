public class Problem_23_Difficulty_2 {
    /*
    * Problem Statement: A number is called:
    *    - perfect : sum of proper divisors = number
    *    - deficient : sum of proper divisors < number
    *    - abundant : sum of proper divisors > number
    *
    * For e.g: 12 -> divisors: 1,2,3,4,6 -> sum = 16
    * which is abundant
    *
    * Task: Find sum of all +ve integers that CANNOT
    * be written as sum of two abundant numbers
    *
    * Important fact: It is known that
    * every number > 28123 can be
    * written as the sum of two abundant numbers.
    * So, we only need to check numbers from 1 to 28123
    *
    * Solution 1:
    * Step 1 - Create a boolean array of size 28123
    * Step 2: For each number, compute sum of proper divisors.
    * If sum > number -> mark it abundant
    * Step 3 - For every i and j from 1...28123:
    * If both are abundant -> mark i_j as possible
    * Step 4: Add all numbers that are NOT marked.
    *
    * Issue with this solution:
    * The nested loops ~49 million times(iterations)
    * Works, but slow and unnecessary.
    *
    * Solution 2: (Optimized)
    * Step 1: Still find all abundant numbers, but store them
    * in a compact int[] abundantList instead of scanning
    * the whole boolean array repeatedly.
    *
    * Step 2: When checking sums:
    *  -Only loop over abundant numbers
    *  -Break inner loop early when i+j > LIMIT
    * This reduces ~49 million iterations to ~15-18 million.
    *
    * Step 3:Improve divisor-sum functionality:
    *  -Stop early if sum already exceeds the number
    * Avoid unnecessary checks.
    *
    * Time Complexity: O(n * sqrt(n)) for divisor sums.
    * but the abundant-pair checking becomes MUCH faster.
    *
    *
    * */

    static void main() {
        System.out.println(nonAbundantSum());
    }

    private static final int LIMIT = 28123;

    private static int nonAbundantSum(){
        boolean[] isAbundant = new boolean[LIMIT + 1];
        int count = 0;

        // Identify Abundant Numbers
        for(int i = 1; i <= LIMIT; i++){
            if(sumOfProperDivisor(i) > i){
                isAbundant[i] = true;
                count++;
            }
        }

        // Convert Abundant numbers into a compact list
        int[] abundantList = new int[count];
        int idx = 0;
        for(int i = 1; i <= LIMIT; i++){
            if(isAbundant[i]) abundantList[idx++] = i;
        }

        boolean[] canBeWritten = new boolean[LIMIT + 1];

        for(int i = 0; i < abundantList.length; i++){
            for(int j = i; j < abundantList.length; j++){
                int sum = abundantList[i] + abundantList[j];
                if(sum > LIMIT) break;
                canBeWritten[sum] = true;
            }
        }

        int total = 0;
        for(int i = 1; i <= LIMIT; i++){
            if(!canBeWritten[i]) total += i;
        }

        return total;
    }

    // divisor-sum function
    private static int sumOfProperDivisor(int n){
        if(n == 1) return 0;

        int sum = 1;
        int sqrt = (int) Math.sqrt(n);

        for(int i = 2; i <= sqrt; i++){
            if(n % i == 0){
                sum += i;
                int pair = n/i;
                if(pair != i) sum += pair;

                if(sum > n) return sum;
            }
        }

        return sum;
    }
}

