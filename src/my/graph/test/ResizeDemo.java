package my.graph.test;


import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class ResizeDemo {
    public static void main(String[] args) {
        if (args.length != 3) {
            StdOut.println("Usage:\njava ResizeDemo [image filename] [num cols to remove] [num rows to remove]");
            return;
        }

        Picture inputImg = new Picture(args[0]);
        int removeColumns = Integer.parseInt(args[1]);
        int removeRows = Integer.parseInt(args[2]); 

        StdOut.printf("image is %d columns by %d rows\n", inputImg.width(), inputImg.height());
        SeamCarver sc = new SeamCarver(inputImg);

        /*double[][] es = sc.energies;
        for (int i = 1; i < es.length - 1; i++) {
            for (int j = 1; j < es[0].length - 1; j++) {
                System.out.print(String.format("%.2f\t", es[i][j]));
            }
            System.out.println();
        }*/
        Stopwatch sw = new Stopwatch();

       /* for (int i = 0; i < removeRows; i++) {
            int[] horizontalSeam = sc.findHorizontalSeam();
            sc.removeHorizontalSeam(horizontalSeam);
        }*/
        System.out.println("-------------");
        for (int i = 0; i < removeColumns; i++) {
            int[] verticalSeam = sc.findVerticalSeam();
//            System.out.println("-----" + i + "------");
//            System.out.println("seam's length : " + verticalSeam.length);
//            System.out.println("pic's width : " + sc.width());
            sc.removeVerticalSeam(verticalSeam);
        }
        System.out.println("-------------");
        Picture outputImg = sc.picture();

        StdOut.printf("new image size is %d columns by %d rows\n", sc.width(), sc.height());
        
        StdOut.println("Resizing time: " + sw.elapsedTime() + " seconds.");
        /*es = sc.energies;
        for (int i = 1; i < es.length - 1; i++) {
            for (int j = 1; j < es[0].length - 1; j++) {
                System.out.print(String.format("%.2f\t", es[i][j]));
            }
            System.out.println();
        }*/
        inputImg.show();
        outputImg.show();
    }

}
