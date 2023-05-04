import java.util.ArrayList;

public class Node {
    
    private ArrayList<Node> children;
    private Node parent;
    private int cost;
    private CubeMatrix cubeMatrix;
    

    public Node() {
        this.children = new ArrayList<Node>();
        this.parent = null;
        this.cost = 0;
    }

    public Node(CubeMatrix cubeMatrix) {
        this.children = new ArrayList<Node>();
        this.parent = null;
        this.cost = 0;
        this.cubeMatrix = cubeMatrix;
    }

    public Node(Node parent) {
        this.children = new ArrayList<Node>();
        this.parent = parent;
        this.cost = 0;
    }


    public ArrayList<Node> getChildren() {
        return this.children;
    }


    public Node getParent() {
        return this.parent;
    }


    public int getCost() {
        return this.cost;
    }

    public void setCost(int cost) {
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



    public void createTree(){
        //TODO
        Cube cubeToMove = this.cubeMatrix.findSmallestMovableCube();


    }

    public void printTree() {
        printTreeHelper(this, "", true);
    }
    
    private void printTreeHelper(Node node, String prefix, boolean isLast) {
        System.out.print(prefix);
        System.out.print(isLast ? "└── " : "├── ");
        System.out.println("Node (cost=" + node.getCost() + ")");
        
        for (int i = 0; i < node.getChildren().size(); i++) {
            Node child = node.getChildren().get(i);
            boolean childIsLast = i == node.getChildren().size() - 1;
            String newPrefix = prefix + (isLast ? "    " : "│   ");
            printTreeHelper(child, newPrefix, childIsLast);
        }
    }
    
    
    
    


   public static void main(String[] args) {

    CubeMatrix cubeMatrix = new CubeMatrix(4);

    
    Node root = new Node(cubeMatrix);


   }




    
}
