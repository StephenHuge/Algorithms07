import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdIn;

public class TestColor {
    public static void main(String[] args) {
        System.out.println("Input width: ");
        int width = StdIn.readInt();
        System.out.println("Input height: ");
        int height = StdIn.readInt();
        Picture p = SCUtility.randomPicture(width, height);
        p.show();
    }
}
