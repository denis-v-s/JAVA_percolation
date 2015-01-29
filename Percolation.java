public class Percolation {
    private boolean[][] grid;
    private int N;
    private int virtualTop;
    private int virtualBottom;
    private WeightedQuickUnionUF uf;

    public Percolation(int N) {               // create N-by-N grid, with all sites blocked
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException("N cannot be less than 1");
        }
        this.N = N;
        this.uf = new WeightedQuickUnionUF(N * N + 2);
        this.virtualTop = N * N;
        this.virtualBottom = N * N + 1;
        this.grid = new boolean[N][N];
    }

    public void open(int i, int j) {        // open site (row i, column j) if it is not open already
        if (!this.isOpen(i, j)) {
            this.grid[i - 1][j - 1] = true; // open the site

            if (i == 1) {
                this.uf.union(this.virtualTop, this.getId(0, j - 1));
            }
            if (i == N) {
                this.uf.union(this.virtualBottom, this.getId(N - 1, j - 1));
            }

            // check for nearby open sites and connect them
            if (i > 1 && this.isOpen(i - 1, j)
                && !this.uf.connected(this.getId(i - 2, j - 1), this.getId(i - 1, j - 1))) {    // top site
                this.uf.union(this.getId(i - 2, j - 1), this.getId(i - 1, j - 1));
            }
            if (j < this.N && this.isOpen(i, j + 1)
                && !this.uf.connected(this.getId(i - 1, j), this.getId(i - 1, j - 1))) {    // right site
                this.uf.union(this.getId(i - 1, j), this.getId(i - 1, j - 1));
            }
            if (i < this.N && this.isOpen(i + 1, j)
                && !this.uf.connected(this.getId(i, j - 1), this.getId(i - 1, j - 1))) {    // bottom site
                this.uf.union(this.getId(i, j - 1), this.getId(i - 1, j - 1));
            }
            if (j > 1 && this.isOpen(i, j - 1)
                && !this.uf.connected(this.getId(i - 1, j - 2), this.getId(i - 1, j - 1))) {    // left site
                this.uf.union(this.getId(i - 1, j - 2), this.getId(i - 1, j - 1));
            }
        }
    }

    public boolean isOpen(int i, int j) {     // is site (row i, column j) open?
        i--;
        j--;
        if (this.outOfBounds(i, j)) {
            throw new java.lang.IndexOutOfBoundsException("either i or j is out of bounds");
        }
        return this.grid[i][j];
    }

    // A full site is an open site that can be connected to an open site in the top row via a chain of neighboring
    // (left, right, up, down) open sites
    public boolean isFull(int i, int j) {     // is site (row i, column j) full?
        i--;
        j--;
        if (this.outOfBounds(i, j)) {
            throw new java.lang.IndexOutOfBoundsException("either i or j is out of bounds");
        }
        return this.isOpen(i + 1, j + 1) && this.uf.connected(this.virtualTop, this.getId(i, j));
    }

    public boolean percolates() {             // does the system percolate?
        return this.uf.connected(virtualTop, virtualBottom);
    }

    private int getId(int i, int j) {      // return site ID
        return i * N + j;
    }

    private boolean outOfBounds(int i, int j) {
        return (i >= this.N && j >= this.N && i < 0 && j < 0);
    }

    public static void main(String[] args) {   // test client (optional)

    }
}