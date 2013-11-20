import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;
import java.util.*;

public class Sudok
{
    private final static int n = 3;
    private static int matrix[][] = new int[n*n][n*n];

    public static void main(String[] args) throws FileNotFoundException
    {
        processFile("puzzle.txt");
    }
    public static void processFile(String file) throws FileNotFoundException {
        long t1 = System.currentTimeMillis();
        readMatrix(file);
        ArrayList<Integer> perm = permissible(matrix, 0, 0);
        System.out.println(perm);
        Solve();
        printMatrix();
        System.out.println("Total time: "+(System.currentTimeMillis()-t1)/1000.0+" seconds");

    }
    public static void readMatrix(String file) throws FileNotFoundException {
        Scanner input = new Scanner(new FileReader(file));
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++)
                matrix[i][j] = input.nextInt();
        }
    }
    public static void printMatrix() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++)
                System.out.print(matrix[i][j]+" ");
            System.out.println();
        }

    }
    public static int[][] deepCopy(int[][] matrix) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++)
                copy[i][j] = matrix[i][j];
        }
        return copy;
    }
    public static boolean Solve() {
        int i, j;
        boolean exit = false;
        ArrayList<Integer> p = new ArrayList<Integer>();
        int[][] store = new int[9][9];
        while (true) {
            HashMap<Point, ArrayList<Integer>> options = getOptions();
            if (options == null)
                return false;
            else if (options.size() == 0)
                return true;
            for (int n = 2; n < 9; n++) {
            //    System.out.println(n);
                for (i = 0; i < 9; i++) {
                    for (j = 0; j < 9; j++) {
                        if (matrix[i][j] == 0) {
                             p =  options.get(new Point(i,j));
                             if (p.size() == n) {
                                 store = deepCopy(matrix);
                            //     printMatrix();
                            //     System.out.println(1);
                                 for (Integer op : p) {

                                /*     if ( i == 0 && j == 0) {
                                         printMatrix();
                                         System.out.println("hey");
                                         matrix[0][0] = 4;
                                     }
                                     else*/
                                         matrix[i][j] = op;
                                     if (Solve())
                                         return true;
                                     matrix = deepCopy(store);
                                 }
                            //     System.out.println(2);
                            //     printMatrix();
                             }
                        }
                    }

                }
            }
            return false;
        }
    }
    public static HashMap<Point, ArrayList<Integer>> getOptions() {
        HashMap<Point, ArrayList<Integer>> options = new HashMap<Point, ArrayList<Integer>>();
        int i, j;
        for (i = 0; i < 9; i++) {
            for (j = 0; j < 9; j++) {
                if (matrix[i][j] == 0) {
                ArrayList<Integer> p = permissible(matrix, i, j);
                //    System.out.printf("(%d,%d) -> ", i ,j);
                //    System.out.println(p);
                    if (p.size() == 0) {
                //        System.out.println("Traceback");
                        return null;
                    }
                    else if (p.size() == 1) {
                        matrix[i][j] = p.get(0);
                        return getOptions();
                    }
                    else
                        options.put(new Point(i,j), p);
                }
            }

        }
        return options;
    }
    public static boolean[][] setTrue(boolean[][] ok) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++)
                ok[i][j] = true;
        }
        return ok;
    }
    public static ArrayList<Integer> permissible(int[][] matrix, int i, int j) {
        boolean[][] ok = new boolean[9][9];
        setTrue(ok);
        ArrayList<Integer> digits = new ArrayList<Integer>();
        int k, m;
        for (k = 1; k <= 9; k++)
            digits.add(k);

        for (k = 0; k < 9; k++) {
            if (matrix[k][j] != 0)
                digits.remove((Integer)matrix[k][j]);
        }

        for (k = 0; k < 9; k++) {
            if (matrix[i][k] != 0)
                digits.remove((Integer)matrix[i][k]);
        }
        int boxi = (i/3) * 3;
        int boxj = (j/3) * 3;
        for (k = boxi; k < boxi+3; k++) {
            for (m = boxj; m < boxj+3; m++) {
                if (matrix[k][m] != 0)
                    digits.remove((Integer)matrix[k][m]);
            }
        }
        return digits;
    }
}
