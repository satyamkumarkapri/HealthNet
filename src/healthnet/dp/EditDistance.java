package healthnet.dp;

/**
 * Edit Distance using Dynamic Programming to find similar patient names.
 * Time Complexity: O(M * N)
 */
public class EditDistance {

    public int calculate(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0)
                    dp[i][j] = j; // Min. operations = j
                else if (j == 0)
                    dp[i][j] = i; // Min. operations = i
                else if (word1.charAt(i - 1) == word2.charAt(j - 1))
                    dp[i][j] = dp[i - 1][j - 1];
                else
                    dp[i][j] = 1 + Math.min(dp[i][j - 1], // Insert
                            Math.min(dp[i - 1][j],        // Remove
                                    dp[i - 1][j - 1]));   // Replace
            }
        }
        return dp[m][n];
    }
}
