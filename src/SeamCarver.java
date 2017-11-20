import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    
    private Picture pic;
    
    public SeamCarver(Picture picture)                // create a seam carver object based on the given picture
    {
        if (picture == null)    throw new java.lang.IllegalArgumentException();
        this.pic = picture;
    }
    public Picture picture()                          // current picture
    {
        return pic;
    }
    public     int width()                            // width of current picture
    {
        return pic.width();
    }
    public     int height()                           // height of current picture
    {
        return pic.height();
    }
    public  double energy(int x, int y)               // energy of pixel at column x and row y
    {
        if ((x == 0 || x == width() - 1) || (y == 0 || y == height() - 1))    
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
    public   int[] findHorizontalSeam()               // sequence of indices for horizontal seam
    {
        return null;
    }
    public   int[] findVerticalSeam()                 // sequence of indices for vertical seam
    {
        return null;
    }
    public    void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
    {
        if (height() <= 1)   throw new java.lang.IllegalArgumentException();
        validateArray(seam);
    }
    public    void removeVerticalSeam(int[] seam)     // remove vertical seam from current picture
    {
        if (width() <= 1)   throw new java.lang.IllegalArgumentException();
        validateArray(seam);
    }
    private void validateArray(int[] arr) {
        if (arr == null)    throw new java.lang.IllegalArgumentException();
    }
}
