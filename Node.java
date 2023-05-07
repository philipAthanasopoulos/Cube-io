import java.util.ArrayList;

public class Node {
    
    private ArrayList<Node> children;
    private Node parent;
    private double cost;
    private double totalCost;
    private CubeMatrix cubeMatrix;
    

    public Node() {
        this.children = new ArrayList<Node>();
        this.parent = null;
        this.cost = 0;
        this.totalCost = 0;
    }

    public Node(CubeMatrix cubeMatrix) {
        this.children = new ArrayList<Node>();
        this.parent = null;
        this.cost = 0;
        this.cubeMatrix = cubeMatrix;
        this.totalCost = 0;

    }

    public Node(Node parent) {
        this.children = new ArrayList<Node>();
        this.parent = parent;
        this.cost = 0;
        this.totalCost = 0;

    }


    public ArrayList<Node> getChildren() {
        return this.children;
    }


    public Node getParent() {
        return this.parent;
    }


    public double getCost() {
        return this.cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void addChild(Node child) {
        this.children.add(child);
    }

    public void removeChild(Node child) {
        this.children.remove(child);
    }


    public void setChildren(ArrayList<Node> children) {
        this.children = children;
    }
    public void setParent(Node parent) {
        this.parent = parent;
    }

    public CubeMatrix getCubeMatrix() {
        return this.cubeMatrix;
    }

    public void setCubeMatrix(CubeMatrix cubeMatrix) {
        this.cubeMatrix = cubeMatrix;
    }



    public double getTotalCost() {
        return this.totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    





    public void printTree() {
        printTreeHelper(this, "", true);
    }
    
    private void printTreeHelper(Node node, String prefix, boolean isLast) {
        System.out.print(prefix);
        System.out.print(isLast ? "└── " : "├── ");
        System.out.println(node.getCost());
        
        for (int i = 0; i < node.getChildren().size(); i++) {
            Node child = node.getChildren().get(i);
            boolean childIsLast = i == node.getChildren().size() - 1;
            String newPrefix = prefix + (isLast ? "    " : "│   ");
            printTreeHelper(child, newPrefix, childIsLast);
        }
    }

    
    
    
    
    


   public static void main(String[] args) {


    Node root = new Node();

    Node child1 = new Node();
    Node child2 = new Node();
    Node child3 = new Node();
    Node child4 = new Node();
    
    root.addChild(child1);
    root.addChild(child2);
    root.addChild(child3);
    root.addChild(child4);

    child1.addChild(child2);
    child1.addChild(child4);


    root.printTree();

   }




    
}
