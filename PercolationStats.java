public class PercolationStats {
    private int iterations;
    private double[] data;

    public PercolationStats(int N, int T) {     // perform T independent experiments on an N-by-N grid
        if (N <= 0 || T <= 0) {
            throw new java.lang.IllegalArgumentException("parameters cannot be less than 0");
        }
        this.iterations = T;
        this.data = new double[T];
        Percolation perc;
        int totalSites = N * N;

        for (int i = 0; i < this.iterations; i++) {
            double openSites = 0;
            perc = new Percolation(N);
            while (!perc.percolates()) {
                // pick random site coordinated
                int x = StdRandom.uniform(N) + 1;
                int y = StdRandom.uniform(N) + 1;
                // open the site if not already open
                if (!perc.isOpen(x, y)) {
                    perc.open(x, y);
                    openSites++;
                }
            }
            // write sites opened until percolates data of current iteration
            this.data[i] = openSites / totalSites;
        }
    }
    public double mean() {                      // sample mean of percolation threshold
        return StdStats.mean(this.data);
    }
    public double stddev() {                    // sample standard deviation of percolation threshold
        return StdStats.stddev(this.data);
    }
    public double confidenceLo() {             // low  endpoint of 95% confidence interval
        return this.mean() - (1.96 * this.stddev()) / java.lang.Math.sqrt(this.iterations);
    }
    public double confidenceHi() {             // high endpoint of 95% confidence interval
        return this.mean() + (1.96 * this.stddev()) / java.lang.Math.sqrt(this.iterations);
    }

    public static void main(String[] args) {   // test client (described below)
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, t);
        System.out.println("mean                    = " + percolationStats.mean());
        System.out.println("stddev                  = " + percolationStats.stddev());
        System.out.println("95% confidence interval = " +
                percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi());
    }
}