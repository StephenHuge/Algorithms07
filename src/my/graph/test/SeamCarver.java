package my.graph.test;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private int[][] colors;

    public SeamCarver(Picture picture)                // create a seam carver object based on the given picture
    {
        if (picture == null)    throw new java.lang.IllegalArgumentException();
        colors = new int[picture.width()][picture.height()];

        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                colors[i][j] = picture.getRGB(i, j);
            }
        }
    }
    public Picture picture()                          // current picture
    {
        Picture pic = new Picture(colors.length, colors[0].length);
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                pic.setRGB(i, j, colors[i][j]);
            }
        }
        return pic;
    }
    public int width()                            // width of current picture
    {
        return colors.length;
    }
    public int height()                           // height of current picture
    {
        return colors[0].length;
    }
    public double energy(int x, int y)               // energy of pixel at column x and row y
    {
        if ((x < 0 || x > width()) || (y < 0 || y > height()) )
            throw new java.lang.IllegalArgumentException("Illegal x and y");
        if ((x == 0 || x == width() - 1) || (y == 0 || y == height() - 1))    
            return 1000.0;
        double dx = square(getBlue(x + 1, y) - getBlue(x - 1, y))
                + square(getGreen(x + 1, y) - getGreen(x - 1, y))
                + square(getRed(x + 1, y) - getRed(x - 1, y));
        double dy = square(getBlue(x, y - 1) - getBlue(x, y + 1))
                + square(getGreen(x, y - 1) - getGreen(x, y + 1))
                + square(getRed(x, y - 1) - getRed(x, y + 1));

        double ans = Math.sqrt(dx + dy);
        return ans;
    }
    private double square(int i) {
        return i * i;
    }
    private int getRed(int i, int j) {
        return (colors[i][j] >> 16) & 0xFF;
    }
    private int getGreen(int i, int j) {
        return (colors[i][j] >> 8) & 0xFF;
    }
    private int getBlue(int i, int j) {
        return (colors[i][j] >> 0) & 0xFF;
    }

    public int[] findHorizontalSeam()               // sequence of indices for horizontal seam
    {
        return null;
    }
    public int[] findVerticalSeam()                 // sequence of indices for vertical seam
    {
        int n           = height() * width();
        int[] seam      = new int[height()];
        int[] nodeTo    = new int[n];
        double[] distTo = new double[n];

        for (int i = 0; i < n; i++) {
            if (i < width())    distTo[i] = 1000;
            else                distTo[i] = Double.POSITIVE_INFINITY;
            nodeTo[i] = -1;
        }
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                for (int k = -1; k <= 1; k++) {
                    if (i == height() - 1 || j == 0 || j == width() - 1)   
                        continue;
                    if (distTo[index(i + 1, j + k)] > distTo[index(i, j)] + energy(j, i)) {
                        distTo[index(i + 1, j + k)] = distTo[index(i, j)] + energy(j, i);
                        nodeTo[index(i + 1, j + k)] = index(i, j);
                    }
                }
            }
        }
        System.out.println();
        for (int i = 0; i < n; i++) {
//            System.out.printf("%9.2f(", distTo[i]);
//            System.out.printf( "%3d) ", nodeTo[i] % width());
            if ((i + 1) % width() == 0)
                System.out.println("\n");
        }
        double min = Double.POSITIVE_INFINITY;
        int index = -1;
        for (int i = 1; i <= width(); i++) {
            if (min > distTo[distTo.length - i]) {
                min = distTo[distTo.length - i];
                index = nodeTo[index(height() - 1, i)];
            }
        }
        int pivot = seam.length - 1;
        int point = index % height();
        while (point != -1) {
            seam[pivot--] = point % height();
            point = nodeTo[point];
        }
        return seam;
    }
    private int index(int i, int j) {
        return (i * width()) + j;
    }
    public void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
    {
    }
    public void removeVerticalSeam(int[] seam)     // remove vertical seam from current picture
    {
    }

    public static void main(String[] args) {
        Picture pic = new Picture("src/7x10.png");
        SeamCarver sc = new SeamCarver(pic);

    }
}
