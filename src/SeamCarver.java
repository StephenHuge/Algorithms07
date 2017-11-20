import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    public SeamCarver(Picture picture)                // create a seam carver object based on the given picture
    {}
    public Picture picture()                          // current picture
    {
        return null;
    }
    public     int width()                            // width of current picture
    {
        return -1;
    }
    public     int height()                           // height of current picture
    {
        return -1;
    }
    public  double energy(int x, int y)               // energy of pixel at column x and row y
    {
        return -1.0;
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
