import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    
    Pixel[][] pixels;
    
    private Picture pic;
    
    private int width;
    
    private int height;
    
    private VerticalSolver vs;
    
    private HorizontalSolver hs;
    
    public SeamCarver(Picture picture)                // create a seam carver object based on the given picture
    {
        if (picture == null)    throw new java.lang.IllegalArgumentException("Illegal picture");
        this.pic = picture;
        this.width = pic.width();
        this.height = pic.height();
        initEnergy();
        this.vs = new VerticalSolver(pixels);
        this.hs = new HorizontalSolver(pixels);
    }
    private void initEnergy() {
        int w = pic.width();
        int h = pic.height();
        pixels = new Pixel[h][w];

        for (int i = 0; i < pixels.length; i++)          // height of pixels (row)
            for (int j = 0; j < pixels[0].length; j++)   // width of pixels  (col)
                pixels[i][j] = new Pixel(j, i, energy(j, i)); 
    }
    public Picture picture()                          // current picture
    {
        return pic;
    }
    public int width()                            // width of current picture
    {
        return width;
    }
    public int height()                           // height of current picture
    {
        return height;
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
        return hs.solve();
    }
    public int[] findVerticalSeam()                 // sequence of indices for vertical seam
    {
        return vs.solve();
    }
    public void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
    {
        hs.remove(seam);
        hs.refresh();
    }
    public void removeVerticalSeam(int[] seam)     // remove vertical seam from current picture
    {
        vs.remove(seam, pic);
        pic = vs.refresh();
        width--;
    }

    public static void main(String[] args) {
    }

}
