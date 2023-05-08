import java.io.*;
import java.util.*;

public class main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter Matrix File Name: ");
        String filename = sc.nextLine();
        File inputFile = new File(filename);
        int size = 0;

        try{
            Scanner reader = new Scanner(inputFile);
            size = Integer.parseInt(reader.nextLine().trim());
        }
        catch(Exception e) {
            System.out.println(e);
        }
            
        List<String> sequences = new ArrayList<String>();
        double[][] matrix = new double[size][size];

        try{
            Scanner reader = new Scanner(inputFile);

            for(int i = 0; i < size; i++){
                if(i == 0) reader.nextLine();
                String line = reader.nextLine();
                System.out.println(line);
                String[] lineSplit = line.split("\\s+");

                sequences.add(lineSplit[0]);
                // matrix[0][i] = lineSplit[0];
                // matrix[i][0] = lineSplit[0];
                matrix[i][i] = 0;

                for(int j = 1; j < lineSplit.length; j++){
                    System.out.print(lineSplit[i] + " and ");
                }

                for(int j = 1; j < lineSplit.length; j++){
                    System.out.println(lineSplit[j]);
                    matrix[i][j-1] = Double.parseDouble(lineSplit[j]);
                                        System.out.println("here1");

                    matrix[j-1][i] = Double.parseDouble(lineSplit[j]);
                                        System.out.println("here2");

                }
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }

        System.out.println(sequences);

        System.out.println("Input matrix is:");
        printMatrix(sequences, matrix);
    }

    public static void printMatrix(List<String> names, double[][] matrix){
        System.out.print("\t");
        for(int i = 0; i < names.size(); i++){
            System.out.print(names.get(i) + "\t");
        }

        System.out.println();

        for(int i = 0; i < matrix.length; i++){
            System.out.print(names.get(i) + "\t");
            for(int j = 0; j < matrix[i].length; j++){
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
