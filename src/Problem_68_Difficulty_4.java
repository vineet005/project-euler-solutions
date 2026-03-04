import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Problem_68_Difficulty_4 {

    /*
     * Problem Statement:
     * Using the numbers 1 to 10, form a "magic" 5‑gon ring.
     * Each of the 5 lines must have the same total.
     *
     * Working clockwise, starting from the group with the
     * numerically lowest external node, concatenate all
     * 5 groups to form a 16‑digit string.
     *
     * Goal:
     * Find the MAXIMUM 16‑digit string for a magic 5‑gon ring.
     *
     * Solution 1:
     *
     * Step 1 — Permute all 10 numbers (10! = 3.6 million)
     * Step 2 — Interpret first 5 as outer nodes, last 5 as inner
     * Step 3 — Check if all 5 lines have equal sum
     * Step 4 — Build the 16‑digit string and track maximum
     *
     * Issues with this solution:
     *  - Huge search space (3.6 million permutations)
     *  - Many permutations are rotational duplicates
     *  - Many permutations cannot produce a 16‑digit string
     *  - Heavy string creation inside the hot loop
     *
     * Solution 2: Optimized
     *
     * Key Observations:
     * 1. The outer ring MUST contain the number 10.
     *    Otherwise, the final string is < 16 digits.
     *
     * 2. The smallest outer node must be the starting line.
     *    This removes rotational duplicates.
     *
     * 3. Instead of permuting all 10 numbers:
     *      - Choose 5 numbers for outer ring (must include 10)
     *      - Fix the smallest outer node at index 0
     *      - Permute remaining 4 outer nodes
     *      - Permute the 5 inner nodes
     *
     * 4. Early termination:
     *    As soon as any line sum mismatches, skip immediately.
     *
     * 5. Use StringBuilder to avoid repeated string allocations.
     *
     * Result:
     * - Search space reduced by ~90%
     * - Runs much faster
     *
     */

    static void main() {
        System.out.println(solve());
    }

    private static String best = "";

    private static String solve() {
        int[] nums = {1,2,3,4,5,6,7,8,9,10};

        List<int[]> outerCombinations = new ArrayList<>();
        chooseOuter(nums, 0, new int[5], 0, outerCombinations);

        for (int[] outer : outerCombinations) {
            Arrays.sort(outer);
            int min = outer[0];
            permuteOuter(outer, 1, min);
        }

        return best;
    }

    private static void chooseOuter(int[] nums, int idx, int[] chosen, int cidx, List<int[]> out) {
        if (cidx == 5) {
            boolean has10 = false;
            for (int x : chosen) if (x == 10) has10 = true;
            if (has10) out.add(chosen.clone());
            return;
        }
        if (idx == nums.length) return;

        chosen[cidx] = nums[idx];
        chooseOuter(nums, idx + 1, chosen, cidx + 1, out);
        chooseOuter(nums, idx + 1, chosen, cidx, out);
    }

    private static void permuteOuter(int[] outer, int idx, int min) {
        if (idx == outer.length) {
            if (outer[0] == min) {
                boolean[] used = new boolean[11];
                for (int x : outer) used[x] = true;

                int[] inner = new int[5];
                int p = 0;
                for (int i = 1; i <= 10; i++) if (!used[i]) inner[p++] = i;

                permuteInner(inner, 0, outer);
            }
            return;
        }

        for (int i = idx; i < outer.length; i++) {
            swap(outer, idx, i);
            permuteOuter(outer, idx + 1, min);
            swap(outer, idx, i);
        }
    }

    private static void permuteInner(int[] inner, int idx, int[] outer) {
        if (idx == inner.length) {
            check(outer, inner);
            return;
        }
        for (int i = idx; i < inner.length; i++) {
            swap(inner, idx, i);
            permuteInner(inner, idx + 1, outer);
            swap(inner, idx, i);
        }
    }

    private static void check(int[] o, int[] in) {
        int a=o[0], b=o[1], c=o[2], d=o[3], e=o[4];
        int f=in[0], g=in[1], h=in[2], i=in[3], j=in[4];

        int sum = a + f + g;
        if (b + g + h != sum) return;
        if (c + h + i != sum) return;
        if (d + i + j != sum) return;
        if (e + j + f != sum) return;

        StringBuilder sb = new StringBuilder(16);
        sb.append(a).append(f).append(g);
        sb.append(b).append(g).append(h);
        sb.append(c).append(h).append(i);
        sb.append(d).append(i).append(j);
        sb.append(e).append(j).append(f);

        String s = sb.toString();
        if (s.length() == 16 && s.compareTo(best) > 0) best = s;
    }

    private static void swap(int[] arr, int i, int j) {
        int t = arr[i]; arr[i] = arr[j]; arr[j] = t;
    }
}
