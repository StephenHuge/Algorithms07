import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private Picture pic;

    double[][] energies;

    public SeamCarver(Picture picture)                // create a seam carver object based on the given picture
    {
        this.pic = picture;
        energies = new double[picture.height() + 2][picture.width() + 2];   // (col + 2)* (row + 2)
        for (int i = 0; i < energies.length; i++) {
            for (int j = 0; j < energies[0].length; j++) {
                if ((i == 0 || i == energies.length) || (j == 0 || j == energies[0].length))
                    energies[i][j] = 2000;
                else 
                    energies[i][j] = energy(j -1, i - 1);   
            }
        }
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
        return ans;
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
    static void printHorizontalAns(int[] ans, double[][] energies) {
        String marker = " ";
        for (int i = 1; i < energies.length - 1; i++) {
            for (int j = 1; j < energies[0].length - 1; j++) {
                if (ans[j - 1] == (i - 1)) marker = "*";
                System.out.print(String.format("%.2f%s   ", energies[i][j], marker));
                marker = " ";
            }
            System.out.println();
        }
    }
    public void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
    {
    }
    public void removeVerticalSeam(int[] seam)     // remove vertical seam from current picture
    {
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
    static void printVerticalAns(int[] ans, double[][] energies) {
        String marker = " ";
        for (int i = 1; i < energies.length - 1; i++) {
            for (int j = 1; j < energies[0].length - 1; j++) {
                if (ans[i - 1] == (j - 1)) marker = "*";
                System.out.print(String.format("%.2f%s   ", energies[i][j], marker));
                marker = " ";
            }
            System.out.println();
        }

    }
}
