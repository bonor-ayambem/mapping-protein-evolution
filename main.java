import java.io.*;
import java.util.*;

public class main {
    public static void main(String[] args){
        List<Node> upgmaTree = new ArrayList<Node>();
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

        System.out.println("Generating UPGMA Tree...");
        upgmaTree = generateUPGMA(sequences, matrix);
        System.out.println(upgmaTree.size());
    }

    public static List<Node> generateUPGMA(List<String> sequences, double[][] matrix){
        List<String> clusters = new ArrayList<String>();
        List<Node> tree = new ArrayList<Node>();
        int nodeNumber = 1;

        // assign each Xi to its own cluster Ci
        for(int i = 0; i < sequences.size(); i++){
            // List<String> clust = new ArrayList<String>();
            clusters.add(sequences.get(i));
            // clusters.add(clust);

            // define one leaf per sequence, each at height 0
            tree.add(new Node(sequences.get(i), 0));
        }

        while(clusters.size() > 1){
            // find two clusters Ci and Cj such that di,j is minimized
            String cluster1 = "";
            String cluster2 = "";
            double dMin = Double.MAX_VALUE;
            int pos1 = -1, pos2 = -1;
            for(int i = 0; i < clusters.size(); i++){
                for(int j = i+1; j < clusters.size(); j++){
                    System.out.println("new compare");
                    double d = evaluateDist(matrix, sequences, clusters.get(i), clusters.get(j));
                    if(d < dMin){
                        System.out.println(d);
                        dMin = d; pos1 = i; pos2 = j;
                        cluster1 = clusters.get(i);
                        cluster2 = clusters.get(j);
                    }
                }
            }
            System.out.println("end of comparisons " + cluster1 + " " + cluster2);

            String newNode = cluster1+cluster2;
            clusters.add(newNode);
            clusters.remove(cluster1);
            clusters.remove(cluster2);

            Node vertex = new Node(newNode, dMin/2);
            vertex.addChild(tree.get(pos1));
            vertex.addChild(tree.get(pos2));

            ListIterator<Node> iter = tree.listIterator();
            while (iter.hasNext()) {
                Node node = iter.next();
                if (node.getName().contains(cluster1) || node.getName().contains(cluster2)) {
                    iter.remove();
                }
            }
            tree.add(vertex);
            // tree.remove(pos1);
            // tree.remove(pos2-1);

            System.out.println(clusters);
        }

        return tree;
    }

    public static double evaluateDist(double[][] matrix, List<String> sequences, String cluster1, String cluster2){
        // int numClust1 = 0;
        // int numClust2 = 0;
        double sum = 0;

        String[] nodes1 = cluster1.split("\\*");
        String[] nodes2 = cluster2.split("\\*");

        // System.out.println(cluster1 + " and " + cluster2);

        for(int i = 0; i < nodes1.length; i++){
            for(int j = 0; j < nodes2.length; j++){
                System.out.println(sequences + " " + nodes1[i] + " " + nodes2[j]);
                sum += matrix[sequences.indexOf(nodes1[i]+"*")][sequences.indexOf(nodes2[j]+"*")];
            }
        }
        System.out.println("d evaluated: sum = " + sum + " |Ci| = " + nodes1.length + " |Cj| = " + nodes2.length);

        return sum/(nodes1.length*nodes2.length);        
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
