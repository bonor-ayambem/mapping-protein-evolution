import java.io.*;
import java.util.*;

public class main {
    public static void main(String[] args){
        List<Node> njTree = new ArrayList<Node>();

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
                // System.out.println(line);
                String[] lineSplit = line.split("\\s+");

                sequences.add(lineSplit[0]+"*");
                // matrix[0][i] = lineSplit[0];
                // matrix[i][0] = lineSplit[0];
                matrix[i][i] = 0;

                // for(int j = 1; j < lineSplit.length; j++){
                //     System.out.print(lineSplit[i] + " and ");
                // }

                for(int j = 1; j < lineSplit.length; j++){
                    // System.out.println(lineSplit[j]);
                    matrix[i][j-1] = Double.parseDouble(lineSplit[j]);
                    // System.out.println("here1");

                    matrix[j-1][i] = Double.parseDouble(lineSplit[j]);
                    // System.out.println("here2");
                }
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }

        System.out.println(sequences);

        System.out.println("Input matrix is:");
        printMatrix(sequences, matrix);

        System.out.println("Generating Neighbor Joining Tree...");
        njTree = generateNJ(sequences, matrix);
    }

    public static List<Node> generateNJ(List<String> sequences, double[][] matrix){
        List<Node> tree = new ArrayList<>();
        List<Node> nodes = new ArrayList<>();
        
        Map<String, Integer> map = new HashMap<>();
        int N = sequences.size();

        for(int i = 0; i < N; i++){
            Node node = new Node(seqiences.get(i),)
        }
    }

    public static void printMatrix(List<String> names, double[][] matrix){
        System.out.print("\t\t");
        for(int i = 0; i < names.size(); i++){
            System.out.print(names.get(i) + "\t");
        }

        System.out.println();

        for(int i = 0; i < matrix.length; i++){
            System.out.print(names.get(i) + "\t");
            for(int j = 0; j < matrix[i].length; j++){
                System.out.print(matrix[i][j] + "\t\t");
            }
            System.out.println();
        }
    }
}

this is the definition of Node:

import java.util.*;

public class Node {
    private String name;
    private double height;
    List<Node> children = new ArrayList<Node>();

    public Node(String n, double h){
        name = n;
        height = h;
    }

    public void addChild(Node child){
        children.add(child);        
    }

    public String getName(){
        return name;
    }

    public double getHeight(){
        return height;
    }

    public List<Node> getChildren(){
        return children;
    }

    public boolean isLeaf(){
        return children.size() == 0;
    }
}

and this input matrix
   5
3MXE_A|PDB 
3MXE_B|PDB 0.0  
3PJ6_A|PDB 0.14  0.14  
3QIN_A|PDB 0.95  0.95  0.95  
3QIO_A|PDB 0.95  0.95  0.95  0.0  
