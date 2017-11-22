import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private Picture pic;

    private double[][] energies;

    public SeamCarver(Picture picture)                // create a seam carver object based on the given picture
    {
        if (picture == null)    throw new java.lang.IllegalArgumentException();
        this.pic = picture;
        this.energies = new double[pic.height() + 2][pic.width() + 2];
        for (int i = 0; i < pic.height() + 2; i++) {
            for (int j = 0; j < pic.width() + 2; j++) {
                if ((i == 0 || i == pic.height() + 1) ||
                        (j == 0 || j == pic.width() + 1))
                    energies[i][j] = 2000;                           // to simplify code
                else    energies[i][j] = energy(i - 1, j - 1);       // get array energies
            }
        }
        /*this.energies = new double[pic.height()][pic.width()];
        for (int i = 0; i < pic.height(); i++) {
            for (int j = 0; j < pic.width(); j++) {
                energies[i][j] = energy(i, j);       // get array energies
            }
        }*/
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
        Color right = pic.get(x + 1, y);
        Color left = pic.get(x - 1, y);
        double ans = square(right.getBlue() - left.getBlue())
                + square(right.getGreen() - left.getGreen())
                + square(right.getRed() - left.getRed());
        return ans;
    }
    private double square(int i) {
        return i * i;
    }
    private double getDeltaYSquare(int x, int y) {
        Color up = pic.get(x, y + 1);
        Color down = pic.get(x, y - 1);
        double ans = square(up.getBlue() - down.getBlue())
                + square(up.getGreen() - down.getGreen())
                + square(up.getRed() - down.getRed());
        return ans;
    }
    public int[] findHorizontalSeam()               // sequence of indices for horizontal seam
    {
        return null;
    }
    public int[] findVerticalSeam()                 // sequence of indices for vertical seam
    {
        return null;
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
}
