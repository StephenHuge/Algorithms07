package my.graph.test;
import java.awt.Color;

import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

/**
 * This class is to solve SeamCarver Problem in vertical direction.
 * There are three methods: 
 * 1. solve()   : it receives a Picture, then find a path with smallest energy, and return it
 * 2. remove()  : it removes path gotten from solve()
 * 3. refresh() : refresh this picture, return a modified one  
 */
public class VerticalSolver {

    private IndexMinPQ<Pixel> minPQ;

    private Pixel[][] pixels;

    private int h;

    private int w;

    private Picture after;

    public VerticalSolver(Pixel[][] ps) {
        this.h = ps.length;
        this.w = ps[0].length; 
        pixels = copy(ps);
        minPQ = new IndexMinPQ<>(h * w);
        init();
    }
    private Pixel init() {
        for (int j = 0; j < pixels[0].length; j++) {
            pixels[0][j].setDistance(1000.0);           // initialize the first row
            Pixel son = pixels[1][j];
            son.setDistance(1000.0 + son.energy());
            if (j == 0)     son.setFather(pixels[0][j]); 
            else            son.setFather(pixels[0][j - 1]);
        }
        // 1. get distances of pixels
        for (int i = 1; i < w - 1; i++) {
            minPQ.insert((w * 1) + i, pixels[1][i]);
        }
        Pixel ans = null;
        while (!minPQ.isEmpty()) {
            Pixel p = minPQ.minKey();
            minPQ.delMin();
            Pixel[] neighbors = neighbors(p);
            // 2. judge if we are in the last row, if yes, then stop step 1 and get the pixel
            if (neighbors == null) {
                ans = p;
                break;
            } 
            for (Pixel n : neighbors)
                if (n != null)  relax(p, n);     
        }
        return ans;
    }
    private Pixel[][] copy(Pixel[][] ps) {
        Pixel[][] ans = new Pixel[h][w];
        for (int i = 0; i < ps.length; i++) 
            for (int j = 0; j < ps[0].length; j++) 
                ans[i][j] = ps[i][j];
        return ans;
    }

    public int[] solve() {
        Pixel lastRow = init();
        // 3. use this pixel gotten from step 2 to generate the path, return it as an integer array
        int[] ans = new int[h];
        int pivot = h - 1;
        Pixel node = lastRow;
        while (node != null) {
            ans[pivot--] = node.col();
            node = node.father();
        }
        return ans;
    }
    private void relax(Pixel pixel, Pixel neighbor) {
        if (neighbor.dist() > pixel.energy() + pixel.dist()) {
            neighbor.setDistance(pixel.energy() + pixel.dist());
            neighbor.setFather(pixel);
            int key = neighbor.row() * w + neighbor.col();

            if (minPQ.contains(key))     minPQ.changeKey(key, neighbor);    // update value in minPQ
            else                         minPQ.insert(key, neighbor);
        }
    }
    private Pixel[] neighbors(Pixel p) {
        if (p.row() == h - 1)    return null;
        Pixel left, down, right;
        if (p.col() == 0)               left = null;
        else                            left = pixels[p.row() + 1][p.col() - 1];
        if (p.col() == w - 1)     right = null;
        else                            right = pixels[p.row() + 1][p.col() + 1];

        down =  pixels[p.row() + 1][p.col()];
        return new Pixel[] { left, down, right};
    }
    public void remove(int[] seam, Picture picture) {
        // 1. number in seam represent for col with specific index, 
        // when seam[i] = j means : pixel that col = i, row = j need to be removed
        Picture ans = new Picture(w - 1, h);
        for (int i = 0; i < ans.height(); i++) {
            for (int j = 0, k = 0; j < ans.width(); j++, k++) {
                System.out.printf("copy : row %d, col %d\n", i, j);
                if (seam[i] == j) k++;
                ans.set(j, i, picture.get(k, i));
            }
        }
        after = ans;
    } 
    public Picture refresh() {
        return after;
    }
    public Pixel[][] pixels() {
        return pixels;
    }
}

/**
 * default iterate order of array is row -> col when direction is vertical, col -> row when horizontal
 */
class SeamCarverWithOuterPixel {

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

//        for (int i = 0; i < pixels.length; i++)          // height of pixels (row)
//            for (int j = 0; j < pixels[0].length; j++)   // width of pixels  (col)
//                pixels[i][j] = new Pixel(j, i, energy(j, i)); 

//        for (int j = 0; j < pixels[0].length; j++) {
//            pixels[0][j].setDistance(1000.0);           // initialize the first row
//            Pixel son = pixels[1][j];
//            son.setDistance(1000.0 + son.energy());
//            if (j == 0)     son.setFather(pixels[0][j]); 
//            else            son.setFather(pixels[0][j - 1]);
//        }
    }
    /**
     * get shortest path from top to bottom by modifying energyTo and edgeTo
     */
    private void getSP() {
        for (int i = 0; i < pixels.length; i++) {         
            for (int j = 0; j < pixels[0].length; j++) {
                Pixel p = pixels[i][j];
                minPQ.insert(i * width() + j, p);           // insert first row
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
    private void relax(Pixel pixel, Pixel neighbor) {
        if (neighbor.dist() > pixel.energy() + pixel.dist()) {
            //            System.out.println("releax : " + neighbor);
            neighbor.setDistance(pixel.energy() + pixel.dist());
            neighbor.setFather(pixel);
            int key = neighbor.row() * width() + neighbor.col();

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

    public static void main(String[] args) {
        Picture picture = new Picture("src/7x10.png");
        StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(), picture.height());

        //        SeamCarver sc = new SeamCarver(picture);
        SeamCarverWithOuterPixel sc = new SeamCarverWithOuterPixel(picture);

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
                StdOut.printf("%9.2f ", sc.pixels[row][col].dist());
            StdOut.println();
        }
    }
}
