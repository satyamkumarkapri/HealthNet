package healthnet.dp;

/**
 * 0/1 Knapsack Implementation for Hospital Resource Allocation (e.g. Equipment Budget Planning)
 * Dynamic Programming Approach. Time Complexity: O(N*W)
 */
public class Knapsack01 {

    public static class Item {
        public String name;
        public int cost;
        public int benefit;

        public Item(String name, int cost, int benefit) {
            this.name = name;
            this.cost = cost;
            this.benefit = benefit;
        }
    }

    public int allocate(int budget, Item[] items) {
        int n = items.length;
        int[][] dp = new int[n + 1][budget + 1];

        for (int i = 0; i <= n; i++) {
            for (int w = 0; w <= budget; w++) {
                if (i == 0 || w == 0) {
                    dp[i][w] = 0;
                } else if (items[i - 1].cost <= w) {
                    dp[i][w] = Math.max(items[i - 1].benefit + dp[i - 1][w - items[i - 1].cost], dp[i - 1][w]);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        // To print the selected items
        int res = dp[n][budget];
        int w = budget;
        System.out.println("Selected Equipment for maximum benefit:");
        for (int i = n; i > 0 && res > 0; i--) {
            if (res == dp[i - 1][w]) {
                continue;
            } else {
                System.out.println(items[i - 1].name + " (Cost: " + items[i - 1].cost + ", Benefit: " + items[i - 1].benefit + ")");
                res = res - items[i - 1].benefit;
                w = w - items[i - 1].cost;
            }
        }
        
        return dp[n][budget];
    }
}
