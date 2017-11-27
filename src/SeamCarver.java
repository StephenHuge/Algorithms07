import java.awt.Color;

import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

/**
 * default iterate order of array is row -> col when direction is vertical, col -> row when horizontal
 */
public class SeamCarver {

    private static final boolean HORIZONTAL = false;

    private static final boolean VERTICAL = true;

    Pixel[][] pixels;

    private IndexMinPQ<Pixel> minPQ;

    private Picture pic;

    private int[] verticalAns;

    private int[] horizontalAns;

    public SeamCarver(Picture picture)                // create a seam carver object based on the given picture
    {
        if (picture == null)    throw new java.lang.IllegalArgumentException("Illegal picture");
        this.pic = picture;
        init(pic, VERTICAL);
        Pixel ansV = getSP(VERTICAL);        // get shortest path
        verticalAns = getVerticalAns(ansV);
        init(pic, HORIZONTAL);
        Pixel ansH = getSP(HORIZONTAL);        // get shortest path
                horizontalAns = getHorizontalAns(ansH);
    }
    private void init(Picture picture, boolean direction) {
        int w = picture.width();
        int h = picture.height();
        pixels = new Pixel[h][w];
        minPQ    = new IndexMinPQ<>(h * w);

        for (int i = 0; i < pixels.length; i++)          // height of pixels (row)
            for (int j = 0; j < pixels[0].length; j++)   // width of pixels  (col)
                pixels[i][j] = new Pixel(j, i, energy(j, i)); 

        if (direction == VERTICAL) {
            for (int j = 0; j < pixels[0].length; j++) {
                Pixel son = pixels[1][j];
                son.setDistance(1000.0 + son.energy());
                if (j == 0)     son.setFather(pixels[0][j]); 
                else            son.setFather(pixels[0][j - 1]);
            }
        } else if (direction == HORIZONTAL) {
            for (int j = 0; j < pixels.length; j++) {
                Pixel son = pixels[j][1];
                son.setDistance(1000.0 + son.energy());
                if (j == 0)     son.setFather(pixels[j][0]); 
                else            son.setFather(pixels[j - 1][0]);
            }
        }
    }
    /**
     * get shortest path from top to bottom by modifying energyTo and edgeTo
     * @param horizontal2 
     */
    private Pixel getSP(boolean direction) {
        int len = 0;
        int second = 0;
        if (direction == VERTICAL) {
            len = pixels[0].length;
            second = width();
        } else if (direction == HORIZONTAL) {
            len = pixels.length;
            second = height();
        }
        //        for (int j = 0; j < pixels[0].length; j++) {
        for (int j = 0; j < len; j++) {
            Pixel p = direction ? pixels[1][j] : pixels[j][1];
            //            minPQ.insert(width() + j, p);           // insert second row
            minPQ.insert(second + j, p);           // insert second row
        }
        while (!minPQ.isEmpty()) {
            Pixel p = minPQ.minKey();
            minPQ.delMin();
            Pixel[] neighbors = neighbors(p, VERTICAL);
            if (neighbors == null)  return p;
            for (Pixel n : neighbors)
                if (n != null)  relax(p, n, direction);     
        }
        return null;
    }
    private void relax(Pixel pixel, Pixel neighbor, boolean direction) {
        if (neighbor.dist() > neighbor.energy() + pixel.dist()) {
            //            System.out.printf("Pixel :    %s -- %.2f\n", pixel, pixel.dist());
            //            System.out.printf("Neighbor : %s -- %.2f\n", neighbor, neighbor.dist());
            neighbor.setDistance(neighbor.energy() + pixel.dist());
            //            System.out.printf("releax     %s's neighbor : %s, energy to %.2f\n",
            //                    pixel, neighbor, neighbor.dist());
            neighbor.setFather(pixel);
            int key = direction ? neighbor.row() * width() + neighbor.col() :
                neighbor.col() * height() + neighbor.row();

            if (minPQ.contains(key))     minPQ.changeKey(key, neighbor);    // update value in minPQ
            else                         minPQ.insert(key, neighbor);
        }
    }
    /**
     * get three p's neighbors
     * @param p
     * @param direction
     * @return
     */
    private Pixel[] neighbors(Pixel p, boolean direction) {
        if (direction == VERTICAL) {
            if (p.row() == height() - 1)    return null;
            Pixel left, down, right;
            if (p.col() == 0)               left = null;
            else                            left = pixels[p.row() + 1][p.col() - 1];
            if (p.col() == width() - 1)     right = null;
            else                            right = pixels[p.row() + 1][p.col() + 1];

            down =  pixels[p.row() + 1][p.col()];
            return new Pixel[] { left, down, right};
        } else {
            if (p.col() == width() - 1)    return null;
            Pixel up, right, down;
            if (p.row() == 0)               up = null;
            else                            up = pixels[p.row() - 1][p.col() + 1];
            if (p.row() == height() - 1)    down = null;
            else                            down = pixels[p.row() + 1][p.col() + 1];

            right =  pixels[p.row()][p.col() + 1];
            return new Pixel[] { up, right, down};
        }
    }
    private int[] getVerticalAns(Pixel p) {
        if (p == null)  throw new java.lang.IllegalArgumentException();
        int[] ans = new int[height()];
        int pivot = height() - 1;
        while (p != null) {
            ans[pivot--] = p.col();
            p = p.father();
        }
        return ans;
    }
    private int[] getHorizontalAns(Pixel p) {
        if (p == null)  throw new java.lang.IllegalArgumentException();
        int[] ans = new int[width()];
        int pivot = width() - 1;
        while (p != null) {
            ans[pivot--] = p.row();
            p = p.father();
        }
        return ans;
    }
    public Picture picture()                          // current picture
    {
        return this.pic;
    }
    public int width()                            // width of current picture
    {
        return this.pic.width();
    }
    public int height()                           // height of current picture
    {
        return this.pic.height();
    }
    public double energy(int x, int y)               // energy of pixel at column x and row y
    {
        if ((x < 0 || x > width()) || (y < 0 || y > height()) )
            throw new java.lang.IllegalArgumentException("Illegal x and y");
        if ((x == 0 || x == width() - 1) || (y == 0 || y == height() - 1))    
            return 1000.0;
        Color up    = pic.get(x - 1, y);
        Color down  = pic.get(x + 1, y);
        Color right = pic.get(x, y + 1);            // get (col, row + 1)
        Color left  = pic.get(x, y - 1);

        double dx = square(right.getBlue() - left.getBlue())
                + square(right.getGreen() - left.getGreen())
                + square(right.getRed() - left.getRed());
        double dy = square(up.getBlue() - down.getBlue())
                + square(up.getGreen() - down.getGreen())
                + square(up.getRed() - down.getRed());

        double ans = Math.sqrt(dx + dy);
        return ans;
    }
    private double square(int i) {
        return i * i;
    }
    public int[] findHorizontalSeam()               // sequence of indices for horizontal seam
    {
        return horizontalAns;
    }
    public int[] findVerticalSeam()                 // sequence of indices for vertical seam
    {
        return verticalAns;
    }
    public void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
    {
    }
    public void removeVerticalSeam(int[] seam)     // remove vertical seam from current picture
    {
    }

    public static void main(String[] args) {
        Picture picture = new Picture("src/7x10.png");
        StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(), picture.height());

        //        SeamCarver sc = new SeamCarver(picture);
        SeamCarver sc = new SeamCarver(picture);

        /***********************************************************
         * test energy() 
         ************************************************************/
        StdOut.printf("Printing energy calculated for each pixel.\n");        

        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                StdOut.printf("%9.2f ", sc.pixels[row][col].energy());
            StdOut.println();
        }
        System.out.println("-----------------------------------------");
        /***********************************************************
         * test init() 
         ************************************************************/
        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                StdOut.printf("%9.2f ", sc.pixels[row][col].dist());
            StdOut.println();
        }

        System.out.println("-----------------------------------------");
        StdOut.printf("Printing distance calculated for each pixel.\n");
        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                StdOut.print(sc.pixels[row][col].father() + "\t");
            StdOut.println();
        }
    }
}
