import java.awt.Color;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver1 {

    private static final boolean HORIZONTAL = true;

    private static final boolean VERTICAL = false;

    private boolean picChanged = false;

    private Picture pic;

    double[][] energies;

    public SeamCarver1(Picture picture)                // create a seam carver object based on the given picture
    {
        if (picture == null)    throw new java.lang.IllegalArgumentException("Picture can't be null");
        this.pic = picture;
        getEnergies();
    }
    private void getEnergies() {
        energies = new double[pic.height() + 2][pic.width() + 2];   // (col + 2)* (row + 2)
        for (int i = 0; i < energies.length; i++) {
            for (int j = 0; j < energies[0].length; j++) {
                if ((i == 0 || i == energies.length - 1) || (j == 0 || j == energies[0].length - 1))
                    energies[i][j] = 2000;
                else 
                    energies[i][j] = energy(j -1, i - 1);   
            }
        }
        picChanged = false;
    }
    public Picture picture()                          // current picture
    {
        if (picChanged) getEnergies();
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
        int[] ans = null;
        int minEnergy = 0; 
        for (int i = 0; i < height(); i++) {
            //            System.out.println("width : " + width());
            int[] t = new int[width() + 1];
            getHorizontalSeam(new Axis(0, i), t);
            if ((minEnergy == 0) || (t[t.length - 1] < minEnergy)) {
                ans = t; 
                minEnergy = ans[ans.length - 1];
            }
        }
        ans = trim(ans, ans.length - 1);
        return ans;
    }
    private void getHorizontalSeam(Axis current, int[] ans) {
        int x = current.x, y = current.y;
        double a = 0, b = 0, c = 0;     // three next pixels' energies

        ans[x] = y;                                     // store the path
        ans[ans.length - 1] += energies[y + 1][x + 1];  // save the energy of path
        if (x == width() - 1)   return;
        else {
            x++;    
            // get next three pixels' energies
            a = energies[y    ][x + 1];         
            b = energies[y + 1][x + 1];
            c = energies[y + 2][x + 1];
            //            System.out.println(String.format("Pixel-%s : a-%.2f, b-%.2f, c-%.2f", current, a, b, c));
            y = (a > b) ? (b > c ? (y + 1) : y) : (c < a ? (y + 1) : (y - 1));      // get the min energy 
        }
        getHorizontalSeam(new Axis(x, y), ans);  // recursively execute this method 
        return;
    }
    public int[] findVerticalSeam()                 // sequence of indices for vertical seam
    {
        int[] ans = null;
        int minEnergy = 0; 
        for (int i = 0; i < width(); i++) {             // every entry in row is possible
            int[] t = new int[height() + 1];            // seam's length is height()
            getVerticalSeam(new Axis(i, 0), t);         // axis is (col, row)
            if ((minEnergy == 0) || (t[t.length - 1] < minEnergy)) {
                ans = t; 
                minEnergy = ans[ans.length - 1];
            }
        }
        ans = trim(ans, ans.length - 1);
        return ans;
    }
    private int[] trim(int[] ans, int len) {
        int[] t = new int[len];
        for (int i = 0; i < len; i++) {
            t[i] = ans[i];
        }
        return t;
    }
    private void getVerticalSeam(Axis current, int[] ans) {
        int x = current.x, y = current.y;               // current axis
        double a = 0, b = 0, c = 0;                     // three next pixels' energies

        ans[y] = x;                                     // store the path
        ans[ans.length - 1] += energies[y + 1][x + 1];  // save the energy of path
        if (y == height() - 1)   return;
        else {
            y++;
            // get next three pixels' energies
            a = energies[y + 1][x    ];         
            b = energies[y + 1][x + 1];
            c = energies[y + 1][x + 2];
            //            System.out.println(String.format("Pixel-%s : a-%.2f, b-%.2f, c-%.2f", current, a, b, c));
            x = (a > b) ? (b > c ? (x + 1) : x) : (c < a ? (x + 1) : (x - 1));      // get the min energy 
        }
        getVerticalSeam(new Axis(x, y), ans);  // recursively execute this method 
        return;

    }
    public void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
    {
        validateArray(seam, HORIZONTAL);
        Picture newPic = new Picture(width(), height() - 1);
        for (int i = 0; i < newPic.width(); i++) {
            boolean gotton = false;         // whether we got the deleted pixel
            for (int j = 0; j < newPic.height(); j++) {
                if (seam[i] == j) {
                    gotton = true;
                    continue;
                }  
                if (!gotton) newPic.set(i, j, pic.get(i, j));
                else         newPic.set(i, j - 1, pic.get(i, j));
            }
        }
        pic = newPic;
        picChanged = true;
        getEnergies();
    }
    public void removeVerticalSeam(int[] seam)     // remove vertical seam from current picture
    {
        validateArray(seam, VERTICAL);
        Picture newPic = new Picture(width() - 1, height());
        for (int i = 0; i < newPic.height(); i++) {
            boolean gotton = false;         // whether we got the deleted pixel
            for (int j = 0; j < newPic.width(); j++) {
                if (seam[i] == j) {
                    gotton = true;
                    continue;
                }  
                if (!gotton) newPic.set(j, i, pic.get(j, i));
                else         newPic.set(j - 1, i, pic.get(j, i));
            }
        }
        pic = newPic;
        picChanged = true;
        getEnergies();
    }
    private void validateArray(int[] arr, boolean dir) {
        if (arr == null)    throw new java.lang.IllegalArgumentException();
        // for checking array's length and range of entries in array
        int len = 0, range = 0;                  
        if (dir == VERTICAL)          {
            len = height();
            range = width();
        } else if (dir == HORIZONTAL) {
            len = width();
            range = height();
        }
        if ((arr.length != len) || (range <= 1))  // if range == 1, no more need to remove any pixels
            throw new java.lang.IllegalArgumentException();
        checkArrayContent(arr, range);
    }
    private void checkArrayContent(int[] arr, int range) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (Math.abs(arr[i] - arr[i + 1]) > 1)            // only adjacent vertex is allowed     
                throw new java.lang.IllegalArgumentException();
            if (arr[i] < 0 || arr[i] >= range)            // check whether entry is in the range 
                throw new java.lang.IllegalArgumentException(arr[i] + " is out of range");
        }
        int last = arr.length - 1;                     // in for loop, last array entry is not checked
        if (arr[last] < 0 || arr[last] >= range)        throw new java.lang.IllegalArgumentException();
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
    
    public static void main(String[] args) {
        Picture pic = new Picture(args[0]);
        SeamCarver1 sc = new SeamCarver1(pic);
        
        StdOut.printf("Printing energy calculated for each pixel.\n");
        for (int i = 0; i < sc.energies.length; i++) {
            for (int j = 0; j < sc.energies[0].length; j++) 
                StdOut.printf("%9.0f ", sc.energies[i][j]);
            StdOut.println();
        }
    }
}
