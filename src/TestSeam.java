import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

class TestSeam {

    private static Picture pic ;
    private static SeamCarver seam;
    @Test
    void tests() {
        String[] pics = {"5X5.png", 
                "5X3.png",
                "12X6.png",
                "3X15.png",
                "4X3.png"}; 

        /*for (String s : pics) {
            pic = new Picture("src/" + s);
            seam = new SeamCarver(pic);
            System.out.println(s + " : ");
            //            testFindHorizontalSeam();
        }*/
        pic = new Picture("src/" + "HJocean.png");
        seam = new SeamCarver(pic);
        testFindVerticalSeam();
    }
    @Test
    void testSeamCarver() {
        /*System.out.println("seam's energies");
        double[][] es = seam.energies;
        for (int i = 0; i < es.length; i++) {
            for (int j = 0; j < es[0].length; j++) {
                System.out.print(String.format("%.2f\t", es[i][j]));
            }
            System.out.println();
        }*/
    }

    @Test
    void testPicture() {
        fail("Not yet implemented");
    }

    @Test
    void testWidth() {
        System.out.println(pic.width());
        System.out.println(new SeamCarver(pic).width());
    }

    @Test
    void testHeight() {
        System.out.println(pic.height());
        System.out.println(new SeamCarver(pic).height());
    }

    @Test
    void testEnergy() {
        Picture picture = new Picture("src/7x10.png");
        StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(), picture.height());
        
//        SeamCarver sc = new SeamCarver(picture);
        SeamCarver sc = new SeamCarver(picture);
        
        StdOut.printf("Printing energy calculated for each pixel.\n");        

        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                StdOut.printf("%9.2f ", sc.pixels[row][col].energy());
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

    @Test
    void testFindHorizontalSeam() {
        System.out.println("seam's energies");
        /* double[][] es = seam.energies;
        for (int i = 1; i < es.length - 1; i++) {
            for (int j = 1; j < es[0].length - 1; j++) {
                System.out.print(String.format("%.2f\t", es[i][j]));
            }
            System.out.println();
        }*/
        int[] ans = seam.findHorizontalSeam();
//        double[][] energies = seam.energies;
//        SeamCarver.printHorizontalAns(ans, energies);
    }

    @Test
    void testFindVerticalSeam() {
        /*pic = new Picture("src/7x10.png");
        seam = new SeamCarver(pic);
        System.out.println("seam's energies");
         double[][] es = seam.energies;
         for (int i = 1; i < es.length - 1; i++) {
             for (int j = 1; j < es[0].length - 1; j++) {
                 System.out.print(String.format("%7.2f ", es[i][j]));
             }
             System.out.println();
         }
        int[] ans = seam.findVerticalSeam();
        double[][] energies = seam.energies;
        printVerticalAns(ans, energies);*/
    }

    @Test
    void testRemoveHorizontalSeam() {
        fail("Not yet implemented");
    }

    @Test
    void testRemoveVerticalSeam() {
        fail("Not yet implemented");
    }
    
    static void printVerticalAns(int[] ans, double[][] energies) {
        System.out.println("-----------------------");
        String marker = " ";
        for (int i = 1; i < energies.length - 1; i++) {
            for (int j = 1; j < energies[0].length - 1; j++) {
                if (ans[i - 1] == (j - 1)) marker = "*";
                System.out.print(String.format("%7.2f%s ", energies[i][j], marker));
                marker = " ";
            }
            System.out.println();
        }
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
}
