public class Problem_828_Difficulty_13 {

    /*
     * Problem statement:
     * Find the minimum score to reach a target number using a given set of six numbers.
     * The score of a solution is the sum of the numbers used.
     * All intermediate values must be strictly positive integers.
     * Sum the minimum scores with the formula: sum(3^n * s_n) mod 1005075251.
     * 
     * Solution: Bitmask Dynamic Programming
     * Step 1: We represent subsets of the 6 numbers using a 6-bit integer (bitmask from 1 to 63).
     * Step 2: We use DP to compute all possible positive integers that can be formed for 
     *         each bitmask.
     * Step 3: We strictly enforce that intermediate values are positive integers.
     * Step 4: We implement a custom, primitive open-addressing `LongSet` to handle lookups.
     * Step 5: After evaluating all masks, we track the one with the minimal score.
     * 
     * Time Complexity: O(T * 3^K * M^2). Computes instantly.
     * Space Complexity: O(2^K * M) dynamically allocated per case. Highly cache-friendly.
     */

    public static void main(String[] args) {
        System.out.println(solve());
    }

    private static final long MOD = 1005075251L;

    private static long solve() {
        // Data embedded directly to bypass File I/O issues
        String[] data = {
            "211:2,3,4,6,7,25", "321:3,7,8,10,10,100", "197:1,3,6,9,50,75", "322:1,4,5,7,50,100",
            "373:2,3,6,9,10,75", "619:1,3,5,6,9,100", "836:4,5,5,9,75,100", "636:1,5,5,10,25,100",
            "853:1,5,6,9,75,100", "320:2,3,6,25,75,100", "201:1,3,3,9,25,75", "994:3,4,5,8,9,25",
            "583:5,5,6,9,10,10", "188:3,4,4,10,10,50", "988:2,2,8,8,25,75", "710:1,2,5,6,25,75",
            "803:2,3,7,8,25,75", "369:3,4,5,9,50,100", "907:1,2,7,7,25,100", "535:2,7,8,9,25,75",
            "460:2,2,9,10,25,50", "109:6,10,10,25,50,100", "999:1,5,7,10,25,100", "387:3,5,6,7,25,50",
            "171:5,6,7,10,25,50", "451:1,2,4,6,75,100", "139:2,4,5,6,7,8", "789:1,5,25,50,75,100",
            "533:1,2,4,5,7,75", "391:1,2,4,5,5,6", "258:1,3,5,9,25,75", "580:1,2,3,6,9,100",
            "214:2,3,8,10,50,75", "650:5,6,8,25,75,100", "489:5,8,9,9,50,75", "849:4,4,7,10,25,50",
            "465:4,6,7,50,75,100", "426:2,5,8,8,75,100", "153:2,3,3,4,5,25", "908:4,6,6,7,25,50",
            "603:2,3,5,6,8,10", "770:3,5,7,8,25,100", "761:2,3,4,6,50,100", "727:4,6,25,50,75,100",
            "489:3,6,25,50,75,100", "928:2,10,25,50,75,100", "622:3,4,10,25,50,75", "545:2,4,4,6,10,75",
            "269:1,9,25,50,75,100", "467:2,4,6,7,25,100", "458:3,5,6,7,9,100", "988:2,7,25,50,75,100",
            "939:1,2,8,10,25,100", "442:2,3,4,7,10,75", "579:2,4,7,8,25,100", "480:1,2,6,8,25,100",
            "254:6,10,25,50,75,100", "441:1,7,8,9,10,75", "548:1,3,10,10,25,50", "325:1,3,6,9,10,50",
            "163:1,1,9,9,50,100", "945:1,3,5,10,50,75", "702:6,8,8,9,25,100", "286:6,7,10,50,75,100",
            "163:3,7,7,25,50,100", "892:1,2,3,6,8,50", "989:2,4,7,7,8,75", "497:8,9,10,50,75,100",
            "797:2,4,4,7,75,100", "523:1,3,6,10,25,100", "557:3,4,10,25,75,100", "447:2,2,3,8,9,50",
            "106:3,3,4,8,75,100", "216:1,2,3,8,50,100", "777:1,6,10,25,50,75", "558:1,3,6,7,50,100",
            "332:1,3,5,5,8,100", "517:1,2,4,8,9,25", "295:1,5,6,7,8,50", "875:4,4,25,50,75,100",
            "888:3,5,6,6,8,50", "279:2,6,7,10,50,100", "886:1,2,5,8,75,100", "393:1,4,5,8,9,10",
            "979:1,3,5,9,10,25", "625:4,7,8,9,10,100", "286:7,8,9,25,50,100", "338:2,3,7,10,25,75",
            "313:4,5,10,10,25,75", "400:2,5,6,8,25,100", "539:1,5,25,50,75,100", "714:3,3,5,6,50,100",
            "576:2,4,5,7,10,50", "690:2,5,7,10,25,100", "923:3,7,8,9,25,100", "773:3,5,7,10,50,100",
            "498:1,1,4,4,8,8", "150:5,6,6,8,25,100", "402:1,4,7,10,50,100", "625:4,6,9,9,75,100",
            "347:2,5,7,10,25,100", "914:3,7,8,8,10,75", "586:1,4,6,10,50,75", "630:5,6,9,25,50,75",
            "796:2,2,4,9,9,10", "479:1,6,9,25,75,100", "626:5,6,7,8,9,25", "489:4,8,8,25,50,100",
            "905:4,4,5,5,6,10", "416:1,3,4,10,25,100", "572:2,8,10,25,50,75", "827:3,4,5,6,50,75",
            "334:4,5,10,25,75,100", "458:1,4,5,9,9,50", "903:4,6,7,10,10,25", "700:1,3,3,7,9,9",
            "960:2,2,5,7,8,10", "221:1,2,4,8,9,50", "603:3,6,8,9,10,100", "649:1,7,8,10,10,50",
            "424:1,3,4,5,10,75", "493:2,3,10,10,75,100", "955:1,3,3,10,50,75", "405:4,5,10,10,50,75",
            "604:2,3,4,7,25,50", "373:1,3,5,6,10,50", "728:3,5,5,8,9,50", "966:4,5,6,8,10,100",
            "493:4,8,10,25,50,75", "167:1,4,6,7,9,75", "435:5,5,8,10,25,75", "845:2,3,5,10,50,75",
            "773:2,5,5,7,75,100", "449:4,4,5,7,75,100", "436:1,3,9,25,50,75", "616:7,8,25,50,75,100",
            "340:3,6,9,9,50,100", "903:2,3,4,5,9,50", "557:5,9,9,25,50,100", "405:1,1,3,4,8,9",
            "145:2,3,7,8,25,75", "195:1,1,5,9,10,50", "500:3,4,7,8,10,25", "975:1,3,4,7,8,50",
            "803:2,3,4,8,10,75", "522:2,4,5,8,25,100", "333:3,5,6,7,25,75", "911:2,3,4,5,7,50",
            "749:1,5,6,6,7,10", "217:4,8,25,50,75,100", "301:2,4,6,8,9,25", "309:1,5,6,7,9,50",
            "123:1,2,2,3,8,8", "399:1,2,3,6,25,50", "960:1,3,4,6,25,100", "517:5,7,8,9,50,75",
            "465:1,2,5,7,10,10", "760:3,4,9,10,25,75", "521:2,2,3,6,7,100", "985:1,4,5,8,50,75",
            "246:3,6,25,50,75,100", "179:4,6,6,10,25,100", "290:2,4,9,25,75,100", "983:2,4,5,9,50,75",
            "745:2,3,5,6,9,75", "344:4,7,7,50,75,100", "807:3,4,4,9,25,75", "558:3,4,6,8,75,100",
            "880:1,1,5,7,10,50", "435:1,3,6,6,9,10", "833:5,7,25,50,75,100", "546:1,4,6,9,25,100",
            "809:3,4,10,25,50,100", "677:3,4,8,10,50,75", "400:2,5,6,8,25,100", "132:2,5,8,10,50,100",
            "638:1,2,7,10,25,75", "948:5,8,25,50,75,100", "400:1,4,5,6,7,100", "992:3,3,4,5,8,10",
            "930:9,10,25,50,75,100", "636:3,10,25,50,75,100", "989:1,4,4,7,9,9", "926:3,8,8,9,25,100",
            "995:1,4,6,8,50,75", "485:2,3,9,9,75,100", "674:2,9,10,25,50,75", "352:3,4,5,6,8,8",
            "553:3,4,6,7,9,9", "247:2,3,5,9,25,100", "876:3,4,5,9,10,75", "442:3,3,4,9,10,75",
            "668:1,2,3,4,50,75", "711:6,9,10,10,75,100", "193:5,5,7,8,9,25", "980:1,2,7,9,25,100",
            "519:3,5,6,9,75,100", "361:1,2,3,7,25,75", "472:1,2,6,10,25,100", "615:1,7,8,9,10,75"
        };
        
        long totalSum = 0;
        long powerOf3 = 3;
        
        // Process each challenge line dynamically
        for (String line : data) {
            String[] parts = line.split(":");
            long target = Long.parseLong(parts[0]);
            String[] numStrs = parts[1].split(",");
            long[] A = new long[numStrs.length];
            for (int i = 0; i < numStrs.length; i++) {
                A[i] = Long.parseLong(numStrs[i]);
            }
            
            long score = solveProblem(target, A);
            totalSum = (totalSum + (score % MOD) * powerOf3) % MOD;
            powerOf3 = (powerOf3 * 3) % MOD;
        }
        
        return totalSum;
    }

    private static long solveProblem(long target, long[] A) {
        int k = A.length;
        long[][] V = new long[1 << k][];
        
        // DP across all subset bitmasks
        for (int mask = 1; mask < (1 << k); mask++) {
            if (Integer.bitCount(mask) == 1) {
                // Base case: Subset with a single number
                V[mask] = new long[]{ A[Integer.numberOfTrailingZeros(mask)] };
            } else {
                LongSet set = new LongSet();
                
                // Iterate through all submasks (subsets of the current mask)
                int sub = (mask - 1) & mask;
                while (sub > 0) {
                    int comp = mask ^ sub;
                    
                    // Break symmetry (process each partition pair once)
                    if (sub > comp) {
                        long[] arrA = V[sub];
                        long[] arrB = V[comp];
                        
                        for (long vA : arrA) {
                            for (long vB : arrB) {
                                // Addition & Multiplication
                                set.add(vA + vB);
                                set.add(vA * vB);
                                
                                // Subtraction (ensuring strict positive integers)
                                if (vA > vB) set.add(vA - vB);
                                else if (vB > vA) set.add(vB - vA);
                                
                                // Division (ensuring exact positive integers)
                                if (vB != 0 && vA % vB == 0) set.add(vA / vB);
                                if (vA != 0 && vB % vA == 0) set.add(vB / vA);
                            }
                        }
                    }
                    sub = (sub - 1) & mask;
                }
                V[mask] = set.toArray();
            }
        }
        
        long minScore = Long.MAX_VALUE;
        
        // Find the minimum score among masks that successfully generated the target
        for (int mask = 1; mask < (1 << k); mask++) {
            long[] arr = V[mask];
            boolean found = false;
            for (long v : arr) {
                if (v == target) {
                    found = true;
                    break;
                }
            }
            
            if (found) {
                long currentScore = 0;
                for (int i = 0; i < k; i++) {
                    if (((mask >> i) & 1) == 1) {
                        currentScore += A[i];
                    }
                }
                if (currentScore < minScore) {
                    minScore = currentScore;
                }
            }
        }
        
        return minScore == Long.MAX_VALUE ? 0 : minScore;
    }

    // Custom Open-Addressing Hash Set optimized for primitive longs
    static class LongSet {
        long[] keys;
        int size;
        
        public LongSet() {
            keys = new long[16];
        }
        
        public void add(long key) {
            if (key <= 0) return; // Disallow zeroes and negative numbers mathematically 
            
            int mask = keys.length - 1;
            int pos = (int)(key ^ (key >>> 32)) & mask;
            
            while (keys[pos] != 0) {
                if (keys[pos] == key) return; // Prevent duplicates
                pos = (pos + 1) & mask;
            }
            
            keys[pos] = key;
            size++;
            
            if (size * 2 > keys.length) resize();
        }
        
        private void resize() {
            long[] old = keys;
            keys = new long[old.length * 2];
            int mask = keys.length - 1;
            
            for (long key : old) {
                if (key != 0) {
                    int pos = (int)(key ^ (key >>> 32)) & mask;
                    while (keys[pos] != 0) pos = (pos + 1) & mask;
                    keys[pos] = key;
                }
            }
        }
        
        public long[] toArray() {
            long[] res = new long[size];
            int i = 0;
            for (long key : keys) {
                if (key != 0) res[i++] = key;
            }
            return res;
        }
    }
}
