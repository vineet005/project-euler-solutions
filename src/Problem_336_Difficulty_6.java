import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Problem_336_Difficulty_6 {

    /*
     * Problem statement:
     * Find the 2011th lexicographic maximix arrangement for eleven carriages.
     * A maximix arrangement is one that requires the maximum possible number of 
     * rotations (which is 2N - 3) using Simple Simon's sorting algorithm.
     *
     * Solution: Reverse Engineering the Algorithm
     * Step 1: In the forward process, Simon fixes carriages one by one from A to the end.
     *         For a carriage at position i to require the maximum 2 rotations, it must 
     *         first be moved to the end of the train, and then to position i.
     * Step 2: Working backward from the sorted target array (A,B,C,D...), the last 
     *         forward step always involves reversing the last 2 carriages.
     * Step 3: For each preceding carriage (from N-2 down to 1), reversing the two 
     *         rotations means we first reverse a suffix of length (N - i + 1), and then 
     *         reverse a suffix of length k, where k can be anything from 2 to (N - i).
     * Step 4: Using recursive backtracking, we simulate this exact reverse process to 
     *         generate all valid maximix arrangements.
     * Step 5: For N=11, there are exactly 9! = 362,880 maximix arrangements. We generate 
     *         all of them, sort them lexicographically, and select the 2011th (index 2010).
     *
     * Time Complexity: O((N-2)! * N log((N-2)!)) due to sorting.
     * Space Complexity: O((N-2)! * N) to store the generated arrangements.
     */

    public static void main(String[] args) {
        System.out.println(solve());
    }

    private static String solve() {
        int n = 11;
        List<String> maximix = new ArrayList<>();
        
        // Start with the sorted target array
        int[] current = new int[n];
        for (int i = 0; i < n; i++) {
            current[i] = i; // 0 to 10 corresponds to A to K
        }
        
        // Revert the guaranteed final step (reverse suffix of length 2)
        reverseSuffix(current, 2);
        
        // Generate all possible backwards states recursively
        generate(n - 2, current, maximix);
        
        // Sort lexicographically to find the specific order
        Collections.sort(maximix);
        
        // Return the 2011th arrangement (0-indexed -> 2010)
        return maximix.get(2010);
    }
    
    private static void generate(int i, int[] current, List<String> maximix) {
        // Base Case: We have worked backwards to the initial starting configuration
        if (i == 0) {
            StringBuilder sb = new StringBuilder(current.length);
            for (int x : current) {
                sb.append((char) ('A' + x));
            }
            maximix.add(sb.toString());
            return;
        }
        
        int n = current.length;
        
        // The first backwards step: reverse suffix of length (N - i + 1)
        int length1 = n - i + 1;
        
        // The second backwards step: choose a suffix length k to reverse
        // Forward process rule: k must be between 2 and N - i inclusive
        for (int k = 2; k <= n - i; k++) {
            int[] next = current.clone();
            reverseSuffix(next, length1);
            reverseSuffix(next, k);
            generate(i - 1, next, maximix);
        }
    }
    
    private static void reverseSuffix(int[] arr, int len) {
        int left = arr.length - len;
        int right = arr.length - 1;
        while (left < right) {
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
    }
}
