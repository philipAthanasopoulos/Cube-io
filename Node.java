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
        printTreeHelper(this, "", "");
    }
    
    private void printTreeHelper(Node node, String prefix, String childPrefix) {
        // Print the current node
        System.out.println(prefix + childPrefix + " Node (cost=" + node.getCost() + ")");
    
        // Recursively print the children
        for (int i = 0; i < node.getChildren().size(); i++) {
            Node child = node.getChildren().get(i);
            String newPrefix = prefix + childPrefix.replaceAll("[^|]", " ") + " ";
            String newChildPrefix = i == node.getChildren().size() - 1 ? "└── " : "├── ";
            printTreeHelper(child, newPrefix, newChildPrefix);
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

    root.printTree();


   }




    
}
