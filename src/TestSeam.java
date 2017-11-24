import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Picture;

class TestSeam {
    private static Picture pic = new Picture("src/5X3.png");
    private static SeamCarver seam = new SeamCarver(pic);
    
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
        fail("Not yet implemented");
    }

    @Test
    void testFindHorizontalSeam() {
        fail("Not yet implemented");
    }

    @Test
    void testFindVerticalSeam() {
        fail("Not yet implemented");
    }

    @Test
    void testRemoveHorizontalSeam() {
        fail("Not yet implemented");
    }

    @Test
    void testRemoveVerticalSeam() {
        fail("Not yet implemented");
    }

}
