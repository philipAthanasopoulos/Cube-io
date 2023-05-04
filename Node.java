import java.util.ArrayList;

public class Node {
    
    private ArrayList<Node> children;
    private Node parent;
    private int cost;
    

    public Node() {
        this.children = new ArrayList<Node>();
        this.parent = null;
        this.cost = 0;
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


    public void createTree(){

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
    
    Node root = new Node();
    Node child1 = new Node(root);
    Node child2 = new Node(root);
    Node child3 = new Node(root);


    root.addChild(child1);
    root.addChild(child2);
    root.addChild(child3);

    child1.addChild(child3);
    child2.addChild(child3);

    child2.addChild(child3);
    child2.addChild(child3);
    child2.addChild(child3);
    child2.addChild(child3);


    root.printTree();


   }




    
}
