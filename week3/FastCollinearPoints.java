package week3;/* *****************************************************************************
 *  Name: AAlchemist
 *  Date: 1/29/2021
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private int numberOfSeg;
    private LineSegment[] lineSegmentsArr;
    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        this.numberOfSeg = 0;
        java.util.List<LineSegment> lineSegments = new ArrayList<>();
        // Throw an IllegalArgumentException if the argument to the constructor is null
        if (points == null) throw new IllegalArgumentException();
        // Throw an IllegalArgumentException if any point in the array is null,
        // or if the argument to the constructor contains a repeated point.
        for(int i=0; i < points.length; i++) {
            if(points[i] == null)	throw new IllegalArgumentException();
            for(int j = i+1; j < points.length; j++) {
                if(points[j] == null)	throw new IllegalArgumentException();
                if(points[i].compareTo(points[j]) == 0)	throw new IllegalArgumentException();
            }
        }
        if (points.length < 4) {
            numberOfSeg = 0;
            lineSegmentsArr = new LineSegment[0];
            return;
        }
        Point[] clonePoints = Arrays.copyOf(points, points.length);
        int N = clonePoints.length;
        Arrays.sort(clonePoints);
        // for (Point point : clonePoints) StdOut.println(point);
        Point currentCheck;
        Point[] otherPoints = new Point[N - 1];
        for (int i = 0; i < N; ++i) {
            currentCheck = clonePoints[i];
            for (int j = 0; j < N; ++j) {
                if (j < i) otherPoints[j] = clonePoints[j];
                else if (j > i) otherPoints[j - 1] = clonePoints[j];
            }
            // Sort other points by the slope to current check point.
            Arrays.sort(otherPoints, currentCheck.slopeOrder());
            int count = 2;
            for (int j = 1; j < otherPoints.length; ++j) {
                double slope1 = currentCheck.slopeTo(otherPoints[j - 1]);
                double slope2 = currentCheck.slopeTo(otherPoints[j]);
                if (floatEqual(slope1, slope2, 1E-8)) {
                    count++;
                    // Situation1: the last point of the segment reach to the end of otherPoints.
                    // The second point(otherPoints[j-count+2]) should be larger than the first one.
                    // It helps us to avoid duplicated segments.
                    if (j == otherPoints.length - 1 && count >= 4 &&
                        currentCheck.compareTo(otherPoints[j - count + 2]) < 0) {
                        Point start = currentCheck;
                        Point end = otherPoints[j];
                        lineSegments.add(new LineSegment(start, end));
                        // StdOut.println(lineSegments);
                    }
                }
                // Situation2: the last point of the segment is otherPoints[-1]
                else {
                    // The second point(otherPoints[j-count+1]) should be larger than the first one.
                    if (count >= 4 && currentCheck.compareTo(otherPoints[j - count + 1]) < 0) {
                        Point start = currentCheck;
                        Point end = otherPoints[j - 1];
                        lineSegments.add(new LineSegment(start, end));
                        // StdOut.println(lineSegments);
                    }
                    count = 2; // continue to find other segments
                }
                // StdOut.println("count = " + count);
            }
        }
        lineSegmentsArr = new LineSegment[lineSegments.size()];
        for (int i = 0; i < lineSegmentsArr.length; ++i)
            lineSegmentsArr[i] = lineSegments.get(i);
    }
    // the number of line segments
    public int numberOfSegments() {
        return numberOfSeg;
    }
    // the line segments
    public LineSegment[] segments() {
        return lineSegmentsArr;
    }
    // compare two floats, we should not use ==
    private boolean floatEqual(double a, double b, double threshold) {
        if (a == Double.NEGATIVE_INFINITY)
            return b == Double.NEGATIVE_INFINITY;
        if (a == Double.POSITIVE_INFINITY)
            return b == Double.POSITIVE_INFINITY;
        return Math.abs(a - b) < threshold;
    }

    public static void main(String[] args) {
        edu.princeton.cs.algs4.In in = new edu.princeton.cs.algs4.In("input9.txt");
        int n = in.readInt();
        edu.princeton.cs.algs4.StdOut.println("total "+n+" points");
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            // StdOut.println("("+x+","+y+")");
            points[i] = new Point(x,y);
        }

        //draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(0.01);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        //print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        edu.princeton.cs.algs4.StdOut.println("line number: " + collinear.numberOfSegments());
        for (LineSegment segment : collinear.segments()) {
            edu.princeton.cs.algs4.StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}