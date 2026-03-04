public class Problem_1_Difficulty_0 {
    /*
     * Problem statement : Find all numbers below 1000,
     * which are multiple of 3 or 5 and then add them together.
     *
     * Solution 1: Run a loop and add an if condition to check
     * if that number is a multiple of 3 or 5 and then add and return the sum.
     * Time Complexity: O(n), where n is 1000 as we have to run a loop
     * Issue -> Imagine if the number is dynamic and very large.
     *
     *
     * Solution 2(Optimized):
     * Step 1: find multiples of 3 and add -> 3+6+9+...+999
     * Which is also like: 3*(1+2+3+...+333)
     * And the sum of 1+...+n series is (n(n+1))/2
     *
     * Step 2 and 3: We do the same for 5 and 15
     *
     * Why 15? because 15 is counted twice once in 3's list and once in 5's list.
     * If we add: all multiples of 3 and all multiples of 5
     * Then numbers like 15 get counted twice.
     * So we subtract the multiples of 15 once to fix that.
     * Time Complexity: Constant.
     *
     *
     * */

    static void main() {
        System.out.println(sumMultiples(1000)); // Answer is 233168
    }

    private static long sumMultiples(int limit){
        limit--; //because we want numbers below the limit

        return sumOfSeries(limit, 3)
                + sumOfSeries(limit, 5)
                - sumOfSeries(limit, 15);

    }

    private static long sumOfSeries(int limit, int n){
        long count = limit / n; //how many multiples exist

        return n * count * (count + 1) / 2; //formula for sum of 1..count, scaled by n
    }
}
