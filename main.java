import java.io.*;
import java.util.*;

public class main {
    public static void main(String[] args){
        List<Node> upgmaTree = new ArrayList<Node>();
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

        System.out.println("\nGenerating UPGMA Tree...");
        upgmaTree = generateUPGMA(sequences, matrix);

        System.out.println("\nWriting Tree to file...");
        String upgmaString = generateUpgmaString(upgmaTree);
        try {
            FileWriter upgmaFile = new FileWriter(filename + "UPGMA");

            upgmaFile.write(upgmaString);
            upgmaFile.close();
        }
        catch (Exception e) {
            e.getStackTrace();
        }
        System.out.println("\nUPGMA Tree is contained in: " + filename + "UPGMA");

        System.out.println("\nGenerating Neighbor Joining Tree...");
        njTree = generateNJ(sequences, matrix);

        System.out.println("\nWriting Tree to file...");
        String njString = generateNjString(njTree);
        try {
            FileWriter njFile = new FileWriter(filename + "NJ");

            njFile.write(njString);
            njFile.close();
        }
        catch (Exception e) {
            e.getStackTrace();
        }
        System.out.println("\nNeighbor Joining Tree is contained in: " + filename + "NJ");
    }

    public static List<Node> generateUPGMA(List<String> sequences, double[][] matrix){
        List<String> clusters = new ArrayList<String>();
        List<Node> tree = new ArrayList<Node>();
        int nodeNumber = 1;

        // assign each Xi to its own cluster Ci
        for(int i = 0; i < sequences.size(); i++){
            clusters.add(sequences.get(i));

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
                    double d = evaluateDist(matrix, sequences, clusters.get(i), clusters.get(j));
                    if(d < dMin){
                        dMin = d; pos1 = i; pos2 = j;
                        cluster1 = clusters.get(i);
                        cluster2 = clusters.get(j);
                    }
                }
            }

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
        }

        return tree;
    }

    public static double evaluateDist(double[][] matrix, List<String> sequences, String cluster1, String cluster2){
        double sum = 0;

        String[] nodes1 = cluster1.split("\\*");
        String[] nodes2 = cluster2.split("\\*");

        for(int i = 0; i < nodes1.length; i++){
            for(int j = 0; j < nodes2.length; j++){
                sum += matrix[sequences.indexOf(nodes1[i]+"*")][sequences.indexOf(nodes2[j]+"*")];
            }
        }

        return sum/(nodes1.length*nodes2.length);        
    }

    public static String generateUpgmaString(List<Node> tree) {
        StringBuilder sb = new StringBuilder();
        generateUpgmaString(tree.get(tree.size()-1), sb);
        return sb.toString();
    }

    public static void generateUpgmaString(Node node, StringBuilder sb) {
        if (node.isLeaf()) {
            sb.append(node.getName()).append(":").append(node.getHeight());
        } 
        else {
            sb.append("(");
            for (int i = 0; i < node.getChildren().size(); i++) {
                Node child = node.getChildren().get(i);
                generateUpgmaString(child, sb);
                if (i < node.getChildren().size() - 1) {
                    sb.append(",");
                }
            }
            sb.append(")");
            sb.append(":").append(node.getHeight());
        }
    }

     public static List<Node> generateNJ(List<String> sequences, double[][] matrix) {
        List<Node> tree = new ArrayList<>();
        List<Node> nodes = new ArrayList<>();
        
        Map<String, Integer> map = new HashMap<>();
        int N = sequences.size();

        for(int i = 0; i < N; i++){
            Node node = new Node(sequences.get(i), 0);
            nodes.add(node);
            map.put(sequences.get(i), i);
        }

        while(nodes.size() > 2) {
            int[] indices = findLowestPair(nodes, matrix);
            int i = indices[0], j = indices[1];

            double dist = calcDistance(matrix, i, j, nodes.size());
            double dij = (getTotalDist(matrix, i) - getTotalDist(matrix, j)) / (nodes.size() - 2);
            double dji = dist - dij;

            String newNodeName = nodes.get(i).getName() + nodes.get(j).getName();
            Node newNode = new Node(newNodeName, dist/2);
            Node nodei = nodes.get(i);
            Node nodej = nodes.get(j);
            newNode.addChild(nodei);
            newNode.addChild(nodej);
            nodes.remove(nodei);
            nodes.remove(nodej);
            nodes.add(newNode);

            map.put(newNodeName, nodes.size() - 1);

            for(int k = 0; k < nodes.size(); k++) {
                if(k != i && k != j) {
                    // System.out.println(map + " " + nodes.get(k).getName() + " " + i + " " + j + " " + k);
                    int m = map.get(nodes.get(k).getName());
                    double dik = (matrix[i][m] + matrix[j][m] - dij - matrix[i][j]) / 2;
                    double djk = dist - dik;
                    matrix[k][nodes.size()-1] = djk;
                    matrix[nodes.size()-1][k] = djk;
                    matrix[k][i] = dik;
                    matrix[i][k] = dik;
                    matrix[k][j] = djk;
                    matrix[j][k] = djk;
                }
            }

        }

        Node root = new Node("root", 0);
        Node node1 = nodes.get(0);
        Node node2 = nodes.get(1);
        double dist = matrix[0][1];
        root.addChild(node1);
        root.addChild(node2);
        node1.setHeight(dist/2);
        node2.setHeight(dist/2);
        root.setHeight(dist/2);
        tree.add(root);

        return tree;
    }

    public static int[] findLowestPair(List<Node> nodes, double[][] matrix) {
        int N = nodes.size();
        int[] indices = new int[2];
        double min = Double.MAX_VALUE;

        for(int i = 0; i < N; i++) {
            for(int j = i+1; j < N; j++) {
                double dist = calcDistance(matrix, i, j, N);
                if(dist < min) {
                    min = dist;
                    indices[0] = i;
                    indices[1] = j;
                }
            }
        }

        return indices;
    }

    public static double calcDistance(double[][] matrix, int i, int j, int N) {
        double dist = 0;
        for(int k = 0; k < N; k++) {
            dist += matrix[i][k] + matrix[j][k];
        }
        dist = dist / (2 * (N-2));
        return dist;
    }

    public static double getTotalDist(double[][] matrix, int i) {
        double dist = 0;
        for(int k = 0; k < matrix.length; k++){
            dist += matrix[i][k];
        }
        return dist;
    }

    public static String generateNjString(List<Node> tree) {
        StringBuilder sb = new StringBuilder();

        for (Node node : tree) {
            if (node.getChildren().size() > 0) {
                sb.append(node.getName()).append(", ");
                for (Node child : node.getChildren()) {
                    double distance = child.getHeight() - node.getHeight();
                    sb.append(child.getName()).append(", ");
                    sb.append(String.format("%.1f", distance)).append("\n");
                }
            } else {
                sb.append(node.getName()).append("\n");
            }
        }

        return sb.toString();
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
