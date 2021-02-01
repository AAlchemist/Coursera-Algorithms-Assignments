package week1;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final int trials; // 尝试次数
    private final double[] openProportions;   // 扣分点：要加final，引用类型的final可以改里面的值，只是不能改指向的对象
    // private week1.Percolation p;  // 扣分点：改成local，只有一个方法用到
    private double meanOfOpenProp;
    private double stddevOfOpenProp;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials){
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException("invalid input!");
        this.trials = trials;
        openProportions = new double[trials];
        for (int i = 0; i < trials; ++i) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                p.open(StdRandom.uniform(1, n+1), StdRandom.uniform(1, n+1));
            }
            openProportions[i] = 1.0 * p.numberOfOpenSites() / (n * n);
            // 扣分点：要在构造函数中计算
            meanOfOpenProp = StdStats.mean(openProportions);
            stddevOfOpenProp = StdStats.stddev(openProportions);
        }
    }

    // sample mean of percolation threshold
    public double mean(){
        return meanOfOpenProp;
    }

    // sample standard deviation of percolation threshold
    public double stddev(){
        return stddevOfOpenProp;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo(){
        return meanOfOpenProp - 1.96 * stddevOfOpenProp / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi(){
        return meanOfOpenProp + 1.96 * stddevOfOpenProp / Math.sqrt(trials);
    }


    public static void main(String[] args) {
        // PercolationStats per = new PercolationStats(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
        week1.PercolationStats per = new week1.PercolationStats(5, 100);
        System.out.printf("%-23s"+" = "+per.mean()+"\n","mean");
        System.out.printf("%-23s"+" = "+per.stddev()+"\n","stddev");
        System.out.printf("%-23s"+" = ["+per.confidenceLo()+","+per.confidenceHi()+"]","95% confidence interval");
    }
}
