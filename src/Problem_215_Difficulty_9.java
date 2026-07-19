import java.util.ArrayList;
import java.util.List;

public class Problem_215_Difficulty_9 {

    /*
     * Problem statement:
     * Consider the problem of building a wall out of 2x1 and 3x1 bricks 
     * (horizontal x vertical dimensions) such that the gaps between horizontally-adjacent 
     * bricks never line up in consecutive layers, i.e., never form a "running crack".
     * Calculate W(32,10), the number of ways to form a crack-free 32x10 wall.
     *
     * Solution: Bitmask Generation and Dynamic Programming
     * Step 1: Generate all possible single layers of length 32 using recursion. 
     *         There are only 3,329 valid ways to form a layer of length 32.
     * Step 2: Represent the internal boundary positions (cracks) of each layer 
     *         as a 32-bit integer bitmask. (e.g., a crack at distance 5 sets the 5th bit).
     * Step 3: Build an adjacency graph. Two layers are compatible (crack-free) if 
     *         they share no internal boundaries. This is rapidly checked using 
     *         the bitwise AND operation: (mask1 & mask2) == 0.
     * Step 4: Use Dynamic Programming to compute the number of ways to build the wall 
     *         layer by layer. dp[i] holds the number of valid walls ending with layer i.
     *         For each successive layer up to 10, sum the paths from all compatible layers.
     *
     * Time Complexity: O(N^2 + H * E) where N is the number of valid layers (3329), 
     *                  H is height (10), and E is the number of compatible layer pairs.
     *                  Runs in a fraction of a second.
     * Space Complexity: O(N + E) for the DP array and sparse adjacency graph.
     */

    public static void main(String[] args) {
        System.out.println(solve());
    }

    private static final int WIDTH = 32;
    private static final int HEIGHT = 10;

    private static long solve() {
        List<Integer> layers = new ArrayList<>();
        
        // Step 1: Generate all valid bitmasks for a row of length 32
        generateLayers(WIDTH, 0, 0, layers);

        int n = layers.size();
        
        // Step 2 & 3: Find compatible rows and build adjacency list
        List<List<Integer>> adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        
        for (int i = 0; i < n; i++) {
            int mask1 = layers.get(i);
            for (int j = i + 1; j < n; j++) {
                int mask2 = layers.get(j);
                if ((mask1 & mask2) == 0) { // No aligned cracks
                    adj.get(i).add(j);
                    adj.get(j).add(i); // Symmetry
                }
            }
        }

        // Step 4: Dynamic Programming for wall height
        long[] dp = new long[n];
        for (int i = 0; i < n; i++) {
            dp[i] = 1; // 1 way to have a wall of height 1 ending in layer i
        }

        for (int h = 2; h <= HEIGHT; h++) {
            long[] nextDp = new long[n];
            for (int i = 0; i < n; i++) {
                for (int compatibleLayer : adj.get(i)) {
                    nextDp[i] += dp[compatibleLayer];
                }
            }
            dp = nextDp;
        }

        // Sum up all possible crack-free walls of the target height
        long totalWays = 0;
        for (long ways : dp) {
            totalWays += ways;
        }

        return totalWays;
    }

    private static void generateLayers(int targetWidth, int currentWidth, int currentMask, List<Integer> layers) {
        if (currentWidth == targetWidth) {
            layers.add(currentMask);
            return;
        }
        
        // Try placing a 2x1 brick
        if (currentWidth + 2 <= targetWidth) {
            int nextMask = currentMask;
            // Record crack if we haven't reached the end
            if (currentWidth + 2 < targetWidth) {
                nextMask |= (1 << (currentWidth + 2));
            }
            generateLayers(targetWidth, currentWidth + 2, nextMask, layers);
        }
        
        // Try placing a 3x1 brick
        if (currentWidth + 3 <= targetWidth) {
            int nextMask = currentMask;
            // Record crack if we haven't reached the end
            if (currentWidth + 3 < targetWidth) {
                nextMask |= (1 << (currentWidth + 3));
            }
            generateLayers(targetWidth, currentWidth + 3, nextMask, layers);
        }
    }
}
