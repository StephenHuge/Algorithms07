import java.awt.Color;

import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Picture;

/**
 * default iterate order of array is row -> col when direction is vertical, col -> row when horizontal
 */
public class SeamCarverWithOuterPixel {
    
    private static final boolean HORIZONTAL = true;

    private static final boolean VERTICAL = false;

     Pixel[][] pixels;

    private IndexMinPQ<Pixel> minPQ;

    private Picture pic;

    public SeamCarverWithOuterPixel(Picture picture)                // create a seam carver object based on the given picture
    {
        if (picture == null)    throw new java.lang.IllegalArgumentException("Illegal picture");
        this.pic = picture;
        init(pic);
        getSP();        // get shortest path
    }
    private void init(Picture picture) {
        int w = picture.width();
        int h = picture.height();
        pixels = new Pixel[h][w];
        minPQ    = new IndexMinPQ<>(h * w);

        for (int i = 0; i < pixels.length; i++) {         // height of pixels (row)
            for (int j = 0; j < pixels[0].length; j++) {  // width of pixels  (col)
                pixels[i][j] = new Pixel(j, i, energy(j, i));              
            }
        }
        for (int j = 0; j < pixels[0].length; j++) {
            pixels[0][j].setDistance(1000.0);
        }
    }
    /**
     * get shortest path from top to bottom by modifying energyTo and edgeTo
     */
    private void getSP() {
        for (int i = 0; i < pixels.length; i++) {         
            for (int j = 0; j < pixels[0].length; j++) {
                Pixel p = pixels[i][j];
                minPQ.insert(i * width() + j, p);
            }
            while (!minPQ.isEmpty()) {
                Pixel p = minPQ.minKey();
                minPQ.delMin();
                Pixel[] neighbors = neighbors(p, VERTICAL);
                if (neighbors == null)  continue;
                for (Pixel n : neighbors)
                    if (n != null)  relax(p, n);
            }
        }
    }
    private void relax(Pixel p, Pixel n) {
        if (n.dist() > p.energy() + p.dist()) {
//            System.out.println("releax : " + n);
            n.setDistance(p.energy() + p.dist());
            n.setFather(p);
            if (minPQ.contains(n.row() * width() + n.col()))    
                minPQ.changeKey(n.row() * width() + n.col(), n);
            else minPQ.insert(n.row() * width() + n.col(), n);
        }
    }
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
            // TODO
            return null;
        }
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
        return null;
    }
    public int[] findVerticalSeam()                 // sequence of indices for vertical seam
    {
        return null;
    }
    public void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
    {
    }
    public void removeVerticalSeam(int[] seam)     // remove vertical seam from current picture
    {
    }
}
