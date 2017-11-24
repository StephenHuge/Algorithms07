import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver2 {

    private Picture pic;

    private double[][] energies;

    public SeamCarver2(Picture picture)                // create a seam carver object based on the given picture
    {
        if (picture == null)    throw new java.lang.IllegalArgumentException();
        this.pic = picture;
        this.energies = new double[pic.height() + 2][pic.width() + 2];  // horizontal length * vertical length
        for (int i = 0; i < pic.height() + 2; i++) {
            for (int j = 0; j < pic.width() + 2; j++) {
                if ((i == 0 || i == pic.height() + 1) ||
                        (j == 0 || j == pic.width() + 1))
                    energies[i][j] = 2000;                           // to simplify code
                else    energies[i][j] = energy(i - 1, j - 1);       // get array energies
            }
        }
    }
    public Picture picture()                          // current picture
    {
        return pic;
    }
    public int width()                            // width of current picture
    {
        return pic.width();
    }
    public int height()                           // height of current picture
    {
        return pic.height();
    }
    public double energy(int x, int y)               // energy of pixel at column x and row y
    {
        if ((x == 0 || x >= width() - 1) || (y == 0 || y >= height() - 1))    
            return 1000.0;
        double dx = getDeltaXSquare(x, y);
        double dy = getDeltaYSquare(x, y);
        double ans = Math.sqrt(dx + dy);
        return ans;
    }
    private double getDeltaXSquare(int x, int y) {
        System.out.println(String.format("point : %d - %d", y, x));
        System.out.println(String.format("left : %d - %d", y - 1, x));
        System.out.println(String.format("right : %d - %d", y + 1, x));
        System.out.println("-------------------------");
        Color right = pic.get(x, y + 1);            // get (col, row + 1)
        Color left = pic.get(x, y - 1);
        double ans = square(right.getBlue() - left.getBlue())
                + square(right.getGreen() - left.getGreen())
                + square(right.getRed() - left.getRed());
        return ans;
    }
    private double square(int i) {
        return i * i;
    }
    private double getDeltaYSquare(int x, int y) {
        System.out.println(String.format("point : %d - %d", y, x));
        System.out.println(String.format("up : %d - %d", y, x + 1));
        System.out.println(String.format("down : %d - %d", y, x - 1));
        System.out.println("-------------------------");
        Color up = pic.get(x - 1, y);
        Color down = pic.get(x + 1, y);
        double ans = square(up.getBlue() - down.getBlue())
                + square(up.getGreen() - down.getGreen())
                + square(up.getRed() - down.getRed());
        return ans;
    }
    public int[] findHorizontalSeam()               // sequence of indices for horizontal seam
    {
        int[] ans = null;
        int minEnergy = 0; 
        for (int i = 0; i < width(); i++) {
            int[] t = new int[width() + 1];
            getNextPixel(new Axis(0, i), Direction.Horizontal, t);
            if ((minEnergy == 0) || (t[t.length - 1] < minEnergy)) {
                ans = t; 
                minEnergy = ans[ans.length - 1];
            }
        }
        return ans;
    }
    public int[] findVerticalSeam()                 // sequence of indices for vertical seam
    {
        return null;
    }

    /**
     * give a pixel, find a line with smallest energy adjacent to it 
     */
    private  Axis getNextPixel(Axis current, Direction dir, int[] ans) {
        int x = current.x, y = current.y;
        double a = 0, b = 0, c = 0; 
        if (dir == Direction.Vertical) {
            ans[y] = x;
            ans[ans.length - 1] += energies[y + 1][x + 1]; 
            if (y == height() - 1)      return current;
            y++;
            // three adjacent pixels
            a = energies[y + 1][x    ];
            b = energies[y + 1][x + 1];
            c = energies[y + 1][x + 2];
//            System.out.println(String.format("Pixel-%s : a-%.2f, b-%.2f, c-%.2f", current, a, b, c));
            x = (a > b) ? (b > c ? (x + 1) : x) : (c < a ? (x + 1) : (x - 1));
        } else if (dir == Direction.Horizontal) {
            ans[x] = y;
            ans[ans.length - 1] += energies[x + 1][y + 1];
            if (x == width() - 1)   return current;
            x++;
            a = energies[y    ][x + 1];
            b = energies[y + 1][x + 1];
            c = energies[y + 2][x + 1];
//            System.out.println(String.format("Pixel-%s : a-%.2f, b-%.2f, c-%.2f", current, a, b, c));
            y = (a > b) ? (b > c ? (y + 1) : y) : (c < a ? (y + 1) : (y - 1));
        }
        Axis dist = getNextPixel(new Axis(x, y), dir, ans);  // recursively execute this method 
        return dist;
    }

    public static void main(String[] args) {
        Picture pic = new Picture(args[0]);
        SeamCarver2 seam = new SeamCarver2(pic);

        System.out.println("seam's energies");
        double[][] es = seam.energies;
        for (int i = 0; i < es.length; i++) {
            for (int j = 0; j < es[0].length; j++) {
                System.out.print(String.format("%.2f\t", es[i][j]));
            }
            System.out.println();
        }
        /*for (int i = 0; i < seam.height(); i++) {
            for (int j = 0; j < seam.width(); j++) {
                System.out.print(String.format("%.2f\t", seam.energies[j + 1][i + 1]));
            }
            System.out.println();
        }*/
        System.out.println("------------------------");
        int[] ans = new int[seam.height() + 1];
        for (int i = 0; i < seam.height(); i++) {
            Axis start = new Axis(i, 0);
            Axis next = seam.getNextPixel(start, Direction.Vertical, ans);
            System.out.print("Array : ");
            for (int j : ans)
                System.out.print(j + " ");
            System.out.println();
            ans[ans.length - 1] = 0;
        }
        printAns(ans, seam.energies, Direction.Vertical);
        /*System.out.println(String.format("Pic : %d X %d", seam.width(), seam.height()));
        int[] ans = new int[seam.width() + 1];
        for (int i = 0; i < seam.width(); i++) {
            Axis start = new Axis(0, i);
            Axis next = seam.getNextPixel(start, Direction.Horizontal, ans);
            System.out.print("Array : ");
            for (int j : ans)
                System.out.print(j + "   ");
            System.out.println();
            ans[ans.length - 1] = 0;
        }
        int[] sol = seam.findHorizontalSeam();
        for (int i : sol)
            System.out.print(i + " ");
        System.out.println();*/
//        printAns(sol, seam.energies, Direction.Horizontal);
    }
    private static void printAns(int[] ans, double[][] energies, Direction dir) {
        String marker = " ";
        for (int i = 1; i < energies.length - 1; i++) {
            for (int j = 1; j < energies[0].length - 1; j++) {
                if (dir == Direction.Vertical) {
                    if (ans[i - 1] == (j - 1)) marker = "*";
                    System.out.print(String.format("%.2f%s   ", energies[i][j], marker));
                    marker = " ";
                } else if (dir == Direction.Horizontal) {
                    if (ans[j - 1] == (i - 1)) marker = "*";
                    System.out.print(String.format("%.2f%s   ", energies[i][j], marker));
                    marker = " ";
                }
            }
            System.out.println();
        }
    }
    public void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
    {
        validateArray(seam, Direction.Horizontal);
    }
    public void removeVerticalSeam(int[] seam)     // remove vertical seam from current picture
    {
        validateArray(seam, Direction.Vertical);
    }
    private void validateArray(int[] arr, Direction dir) {
        if (arr == null)    throw new java.lang.IllegalArgumentException();
        // for checking array's length and range of entries in array
        int len = 0, range = 0;                  
        if (dir == Direction.Vertical)          {
            len = height();
            range = width();
        } else if (dir == Direction.Horizontal) {
            len = width();
            range = height();
        }
        if ((arr.length != len) || (range <= 1))  // if range == 1, no more need to remove any pixels
            throw new java.lang.IllegalArgumentException();
        checkArrayContent(arr, range);
    }
    private void checkArrayContent(int[] arr, int range) {
        for (int i = 0; i < arr.length - 1; i++) {
            // only adjacent vertex is allowed
            if (Math.abs(arr[i] - arr[i + 1]) > 1)     throw new java.lang.IllegalArgumentException();
            // check whether entry is in the range
            if (arr[i] < 0 || arr[i] > range)          throw new java.lang.IllegalArgumentException();
        }
        int last = arr.length - 1;                     // in for loop, last array entry is not checked
        if (arr[last] < 0 || arr[last] > range)        throw new java.lang.IllegalArgumentException();
    }
    private enum Direction {
        Vertical, Horizontal;
    }
    private static class Axis {
        int x;
        int y;
        public Axis(int mx, int my) {
            this.x = mx;
            this.y = my;
        }
        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
}
/* for (int i = 0; i < seam.height(); i++) {
            for (int j = 0; j < seam.width(); j++) {
                Axis start = new Axis(j, i);
                Axis next = seam.getNextPixel(start, Direction.Horizontal);
//                System.out.println(String.format("%s's next smallest pixel is %s", start, next));
                double ans = seam.energies[next.y + 1][next.x + 1];
                System.out.println(String.format("\t%s's next smallest pixel is %.2f", start, ans));
            }
            System.out.println("------------------------");
        }*/

/* public int[] findHorizontalSeam()               // sequence of indices for horizontal seam
    {
        // recursively find shortest path
        int[] ans = null; 
        for (int i = 0; i < width(); i++) {
            ans = new int[width() + 1];
            int[] sol = findPath(Direction.Horizontal, 0, i, ans);
            if (ans[width()] > sol[width()])
                ans = sol;
        }
        ans = trim(ans, width());
        return ans;
    }
    public int[] findVerticalSeam()                 // sequence of indices for vertical seam
    {
        // recursively find shortest path
        int[] ans = null; 
        for (int i = 0; i < height(); i++) {
            ans = new int[height() + 1];
            int[] sol = findPath(Direction.Vertical, 0, i, ans);
            if (ans[height()] > sol[height()])
                ans = sol;
        }
        ans = trim(ans, height());
        return ans;
    }
    private int[] trim(int[] ans, int len) {
        int[] t = new int[len];
        for (int i = 0; i < len; i++) {
            t[i] = ans[i];
        }
        return t;
    }
 *//**
 * @param dir       direction that seam goes
 * @param index     index of the ans array, same direction with seam
 * @param path      the row or col that we manage now
 * @param ans       the answer
 * @return          the answer
 *//*
    private int[] findPath(Direction dir, int index, int path, int[] ans) {
        if ((dir == Direction.Vertical) && (index == height())) {
            for (int p = 0; p < height(); p++)  // get this path's length
                ans[index] += energies[p + 1][ans[p] + 1];
            return ans;  
        } else if ((dir == Direction.Horizontal) && (index == width())) {
            for (int p = 0; p < width(); p++)
                ans[index] += energies[ans[p] + 1][p + 1];
            return ans;  
        }
        if (index == 0)     ans[index] = path;
        else                ans[index] = getMin(dir, index, ans[index - 1]) - 1;
        findPath(dir, index + 1, ans[index], ans);
        return ans;
    }
    private int getMin(Direction dir, int index, int path) {
        double a = 0, b = 0, c = 0;
        if (dir == Direction.Vertical) {
            a = energies[index + 1][path];
            b = energies[index + 1][path + 1];
            c = energies[index + 1][path + 2];
        } else if (dir == Direction.Horizontal) {
            a = energies[path][index + 1];
            b = energies[path + 1][index + 1];
            c = energies[path + 2][index + 1];
        }
        return (a > b) ? (b > c ? (path + 2) : (path + 1)) : (c < a ? (path + 2) : path);
    }*/