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
}