package week1;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF uf;
    private int[][] grid; // 网格，若为OPEN则为其在网格中的位置编号，若为CLOSE则为0，初始化为0
    private int openedNum = 0;// 已经open的site数目
    private final int top, bottom; // 虚节点      扣分点：没加final修饰符：it is initialized only in the declaration or constructor.
    private final int n; // size of grid

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n){
        if (n <= 0){
            throw new IllegalArgumentException("grid size should be greater than one !");
        }
        this.top = n * n + 2; // 初始化top虚节点的编号
        this.bottom = n * n + 1; // 初始化bottom虚节点的编号，其他正常节点1 ~ n * n
        this.n = n;
        grid = new int[n][n];
        uf = new WeightedQuickUnionUF(n * n + 3); // 多了top, bottom虚节点，分别和第一层和最后一层的每一个OPEN节点相连
        // 初始化grid，均为BLOCK
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = 0;
            }
        }
    }

    // check the validation of inputs
    private void validate(int row, int col) {
        if (row < 1 || col < 1 || row > this.n || col > this.n) {
            throw new IllegalArgumentException("invalid input!");
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);
        // 已经OPEN
        if (grid[row-1][col-1] != 0) {
            return;
        }
        // OPEN了，设置为其在网格中的位置编号
        grid[row-1][col-1] = (row - 1) * n + col;
        openedNum++;

        // 以下更新连通情况
        // 第一层的OPEN节点和top虚节点连通
        if (row == 1 && grid[row-1][col-1] != 0) {
            uf.union(top, grid[row-1][col-1]);
        }
        // 最后一层的OPEN节点和bottom虚节点连通
        if (row == n && grid[row-1][col-1] != 0) {
            uf.union(bottom, grid[row-1][col-1]);
        }
        // 一个节点的四面八方（注意在边界上的节点不要越界查询）
        // row-1-1分开写是区分第一个减是坐标对应，第二个则是表示它在上一行，后续-2，-1+1同理。
        if (row > 1 && grid[row - 1 - 1][col-1] != 0) {
            uf.union(grid[row - 2][col - 1], grid[row - 1][col - 1]);
        }
        if (col > 1 && grid[row-1][col-2] != 0) {
            uf.union(grid[row-1][col-2], grid[row-1][col-1]);
        }
        if (row < n && grid[row-1+1][col-1] != 0) {
            uf.union(grid[row][col-1], grid[row-1][col-1]);
        }
        if (col < n && grid[row-1][col] != 0) {
            uf.union(grid[row-1][col], grid[row-1][col-1]);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        validate(row, col);
        return grid[row-1][col-1] != 0;
    }

    // is the site (row, col) full? （full理解为是否能被水填满，也就是和top虚节点是不是连通）
    public boolean isFull(int row, int col){
        validate(row, col);
        return uf.find(top) == uf.find(grid[row-1][col-1]); // 扣分点：没加&&isOpen   && isOpen(row, col);
        // 不用判断grid[row-1][col-1] != 0 因为单根节点的父节点是自己
    }

    // returns the number of open sites
    public int numberOfOpenSites(){
        return openedNum;
    }

    // does the system percolate?
    public boolean percolates(){
        return uf.find(top) == uf.find(bottom);
    }

    public static void main(String[] args) {
        Percolation p = new Percolation(1);
        // for(int i=0;i<10;i++){
        //     p.open(StdRandom.uniform(1, 6), StdRandom.uniform(1, 6));
        // }
        // p.open(1, 1);
        System.out.println(p.isFull(1, 1));
        System.out.println(p.isOpen(1, 1));
        if (p.percolates()) System.out.println("percolates");
        else System.out.println("does not percolate");
        System.out.println(p.numberOfOpenSites());
    }
}
