import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Picture;

/**
 * default iterate order of array is row -> col when direction is vertical, col -> row when horizontal
 */
public class SeamCarver {

    private double[][] energies;

    private double[][] energyTo;

    private int[][] edgeTo;

    private IndexMinPQ<Pixel> minPQ;

    private Picture pic;

    public SeamCarver(Picture picture)                // create a seam carver object based on the given picture
    {
        if (picture == null)    throw new java.lang.IllegalArgumentException("Illegal picture");
        this.pic = picture;
        init(pic);
        getSP();        // get shortest path
    }
    private void init(Picture picture) {
        int w = picture.width(), h = picture.height() + 2;
        energies = new double[h + 2][w + 2];
        energyTo = new double[h][w];
        edgeTo   = new int[h][w];
        minPQ    = new IndexMinPQ<>(h * w);

        for (int i = 0; i < energies.length; i++) {         // height of energies
            for (int j = 0; j < energies[0].length; j++) {  // width of energies
                energies[i][j] = energy(i, j);              // get energies of every pixel in the picture
            }
        }
        for (int i = 0; i < energyTo.length; i++) {
            for (int j = 0; j < energyTo[0].length; j++) {
                if (i == 0) {
                    energyTo[i][j] = 1000.0;
                    edgeTo[i][j]   = 0;                        
                } else {
                    energyTo[i][j] = Double.POSITIVE_INFINITY;  // default energyTo is positive infinity
                    edgeTo[i][j]   = -1;                        // default energyTo is -1, means no path available
                }
            }
        }
    }
    /**
     * get shortest path from top to bottom by modifying energyTo and edgeTo
     */
    private void getSP() {
        for (int i = 0; i < energyTo.length; i++) {
            for (int j = 0; j < energyTo[0].length; j++) {
                Pixel[] neighbors = neighbors(i, j);
                for (Pixel p : neighbors)
                    relax(i, j, p);
            }
        }
    }
    private void relax(int i, int j, Pixel p) {
        if (energyTo[p.row()][p.col()] > energyTo[i][j] + p.dist()) {
            energyTo[p.row()][p.col()] = energyTo[i][j] + p.dist();
//            edgeTo[p.row()][p.col()] = 
        }
    }
    private Pixel[] neighbors(int i, int j) {
        if (i == height())      return null;
        return  new Pixel[] {
                new Pixel(j - 1, i + 1, energies[i + 1][j - 1]), 
                new Pixel(j    , i + 1, energies[i + 1][j    ]),
                new Pixel(j + 1, i + 1, energies[i + 1][j + 1])}; 
    }
    public Picture picture()                          // current picture
    {
        return null;
    }
    public int width()                            // width of current picture
    {
        return -1;
    }
    public int height()                           // height of current picture
    {
        return -1;
    }
    public double energy(int x, int y)               // energy of pixel at column x and row y
    {
        return -1;
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
    private static class Pixel implements Comparable<Pixel> {
        private int row, col;
        private double distance;

        public Pixel(int col, int row, double distance) {
            this.col = col;
            this.row = row;
            this.distance = distance;
        }
        public int row() {
            return row;
        }
        public void setRow(int row) {
            this.row = row;
        }
        public int col() {
            return col;
        }
        public void setCol(int col) {
            this.col = col;
        }
        public double dist() {
            return distance;
        }
        public void setDistance(double distance) {
            this.distance = distance;
        }
        @Override
        public int compareTo(Pixel p) {
            if (this.dist() - p.dist() < 0) return -1;
            if (this.dist() - p.dist() > 0) return 1;
            else                            return 0;
        }
    }
    public static void main(String[] args) {
    }

}
