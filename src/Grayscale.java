

import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class Grayscale {

    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        int width  = picture.width();
        int height = picture.height();

        // convert to grayscale
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                Color color = picture.get(col, row);
                Color gray = Luminance.toGray(color);
                picture.set(col, row, gray);
            }
        }
        picture.show();
    }

}