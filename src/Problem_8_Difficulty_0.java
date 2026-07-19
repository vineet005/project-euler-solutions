public class Problem_8_Difficulty_0 {

    /*
     * Problem statement:
     * Find the thirteen adjacent digits in the 1000-digit number that have the 
     * greatest product. What is the value of this product?
     *
     * Solution: Sliding Window
     * Step 1: Store the 1000-digit number as a single String.
     * Step 2: Iterate through the string, evaluating every contiguous substring of length 13.
     * Step 3: For each substring, compute the product of its digits.
     *         Since the maximum possible product is 9^13 (2,541,865,828,329), 
     *         this value fits perfectly within a standard 64-bit signed integer (long), 
     *         preventing overflow issues.
     * Step 4: Keep track of the maximum product encountered and return it.
     * 
     * Optimization note: One could theoretically optimize this by instantly skipping 
     * ahead whenever a '0' is encountered within the 13-digit window (since the product 
     * becomes 0). However, given the extremely small search space (1000 characters), 
     * a simple brute-force sliding window executes in a fraction of a millisecond.
     *
     * Time Complexity: O(N * K) where N = 1000 and K = 13. Computes instantly.
     * Space Complexity: O(N) to store the 1000-digit string.
     */

    public static void main(String[] args) {
        System.out.println(solve());
    }

    private static long solve() {
        String numStr = "73167176531330624919225119674426574742355349194934" +
                        "96983520312774506326239578318016984801869478851843" +
                        "85861560789112949495459501737958331952853208805511" +
                        "12540698747158523863050715693290963295227443043557" +
                        "66896648950445244523161731856403098711121722383113" +
                        "62229893423380308135336276614282806444486645238749" +
                        "30358907296290491560440772390713810515859307960866" +
                        "70172427121883998797908792274921901699720888093776" +
                        "65727333001053367881220235421809751254540594752243" +
                        "52584907711670556013604839586446706324415722155397" +
                        "53697817977846174064955149290862569321978468622482" +
                        "83972241375657056057490261407972968652414535100474" +
                        "82166370484403199890008895243450658541227588666881" +
                        "16427171479924442928230863465674813919123162824586" +
                        "17866458359124566529476545682848912883142607690042" +
                        "24219022671055626321111109370544217506941658960408" +
                        "07198403850962455444362981230987879927244284909188" +
                        "84580156166097919133875499200524063689912560717606" +
                        "05886116467109405077541002256983155200055935729725" +
                        "71636269561882670428252483600823257530420752963450";

        int k = 13;
        long maxProduct = 0;

        for (int i = 0; i <= numStr.length() - k; i++) {
            long currentProduct = 1;
            for (int j = 0; j < k; j++) {
                currentProduct *= (numStr.charAt(i + j) - '0');
            }
            if (currentProduct > maxProduct) {
                maxProduct = currentProduct;
            }
        }

        return maxProduct;
    }
}
