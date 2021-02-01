package week3;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

    private int numberOfSeg;
    private LineSegment[] lineSegmentsArr;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        this.numberOfSeg = 0;
        List<LineSegment> lineSegments = new ArrayList<>();
        // Throw an IllegalArgumentException if the argument to the constructor is null
        if (points == null) throw new java.lang.IllegalArgumentException();
        // if any point in the array is null, or if the argument to the constructor contains a repeated point.
        for(int i=0; i < points.length; i++) {
            if(points[i] == null)	throw new java.lang.IllegalArgumentException();
            for(int j = i+1; j < points.length; j++) {
                if(points[j] == null)	throw new java.lang.IllegalArgumentException();
                if(points[i].compareTo(points[j]) == 0)	throw new java.lang.IllegalArgumentException();
            }
        }
        // make sure that the lineSegment include two endpoints
        Point[] points1 = points.clone();
        Arrays.sort(points1);
        // bruteforce, find the segment
        for (int p = 0; p < points1.length - 3; ++p) {
            for (int q = p + 1; q < points1.length - 2; ++q) {
                for (int r = q + 1; r < points1.length - 1; ++r) {
                    for (int s = r + 1; s < points1.length; ++s) {
                        double slope_pq = points1[p].slopeTo(points1[q]);
                        double slope_pr = points1[p].slopeTo(points1[r]);
                        double slope_ps = points1[p].slopeTo(points1[s]);
                        if (floatEqual(slope_pq, slope_pr, 1.0E-8) &&
                                floatEqual(slope_pq, slope_ps, 1.0E-8)) {
                            lineSegments.add(new LineSegment(points1[p], points1[s]));
                            numberOfSeg++;
                        }
                    }
                }
            }
        }
        lineSegmentsArr = new LineSegment[lineSegments.size()];
        for (int i = 0; i < lineSegmentsArr.length; ++i)
            lineSegmentsArr[i] = lineSegments.get(i);
    }

    // compare two floats, we should not use ==
    private boolean floatEqual(double a, double b, double threshold) {
        if (a == Double.NEGATIVE_INFINITY)
            return b == Double.NEGATIVE_INFINITY;
        if (a == Double.POSITIVE_INFINITY)
            return b == Double.POSITIVE_INFINITY;
        return Math.abs(a - b) < threshold;
    }

    // the number of line segments
    public int numberOfSegments() {
        return numberOfSeg;
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegmentsArr;
    }

    public static void main(String[] args) {
        int size = StdIn.readInt();
        Point[] points = new Point[size];
        for (int i = 0; i < size; ++i) {
            int x = StdIn.readInt();
            int y = StdIn.readInt();
            points[i] = new Point(x, y);
        }
        BruteCollinearPoints bcp = new BruteCollinearPoints(points);
        LineSegment[] lineSegments = bcp.segments();
        for (int i = 0; i < lineSegments.length; ++i) {
            StdOut.println(lineSegments[i]);
        }
        StdOut.println(bcp.numberOfSeg);

    }
}
