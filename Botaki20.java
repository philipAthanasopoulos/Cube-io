import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Botaki20 {
    private CubeMatrix cubeMatrix;
    private double finalCost = 0;
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";
    
    public Botaki20(){
        
    }
    
    public double calculateCostOfMove(Cube cube , Cube target) {
        double cost = 0;
        int yNext = target.getYPos();
    
        if(cube.getYPos() == yNext) cost += 0.75;
        //if cube goes on a line of higher index cost is 0.5*(curY - nextY)
        else if(cube.getYPos() < yNext) cost += 0.5*(yNext - cube.getYPos());
        //if cube goes on a line of lower index cost is (nextY - curY)
        else cost += (cube.getYPos() - yNext);
        return cost;
    }

    public ArrayList<Node> calculateAllPossibleMovesForCube(Cube cube , Node parent){
        //TODO
        ArrayList<Node> children = new ArrayList<Node>();

        CubeMatrix parentCubeMatrix = parent.getCubeMatrix();
        //find all free positions to move to
        ArrayList<Cube> possibleMoves = new ArrayList<Cube>();
        for(CubeLine line : parentCubeMatrix.getCubeLines()){
            for(Cube cubeToCheck : line.getCubes()){
                boolean isRightAbove = parentCubeMatrix.getBelowCube(cubeToCheck).getCubeNumber() == cube.getCubeNumber();
                if(parentCubeMatrix.positionIsFreeToMoveTo(cubeToCheck) && !isRightAbove) possibleMoves.add(cubeToCheck);
            }
        }

        //find moves in bottom line from k to 3k and only keep one
        ArrayList<Cube> cubesToKeep = new ArrayList<Cube>();
        boolean foundEmptyPosition = false;
        for(Cube cubeToCheck : possibleMoves){
            if(cubeToCheck.getXPos() > parentCubeMatrix.getNumOfCubesPerLine() - 1){
                if(!foundEmptyPosition){
                    cubesToKeep.add(cubeToCheck);
                    foundEmptyPosition = true;
                }
            }
            else{
                cubesToKeep.add(cubeToCheck);
            }
        }

        possibleMoves = cubesToKeep;

        //create a new Node for each possible move
        for(Cube cubeToMoveTo : possibleMoves){
            Node newNode = new Node(parent);
            double costOfMove = calculateCostOfMove(cube , cubeToMoveTo);
            newNode.setCost(costOfMove);

            //create a new cubeMatrix for each possible move
            CubeMatrix newCubeMatrix = parentCubeMatrix.copy();
            
            //get the cube that is to be moved
            Cube cubeToMove = newCubeMatrix.getCube(cube.getCubeNumber());
            
            //move the cube
            newCubeMatrix.moveCube(cubeToMove , cubeToMoveTo.getXPos() , cubeToMoveTo.getYPos() );
            newNode.setCubeMatrix(newCubeMatrix);
            newCubeMatrix.setCostOfMove(newNode.getCost());
            
            //calculate heuristic cost
            newNode.setHeuristicCost(calculateHeuristicCost(newCubeMatrix));
            newNode.setTotalCost(parent.getTotalCost() + newNode.getCost());

            //finally add to children array list
            children.add(newNode);
            //print for DEBUG
            // newNode.getCubeMatrix().printCubeMatrix();
            
        }
        

        return children;
    }


    public ArrayList<Node> expandNode(Node parent){
        ArrayList<Node> children = new ArrayList<Node>();
        CubeMatrix matrix = parent.getCubeMatrix();

        for(Cube cube : matrix.getCubes()){
            if(matrix.isMoveable(cube) && cube.getCubeNumber() != 0) children.addAll(calculateAllPossibleMovesForCube(cube, parent));
        }  
        return children;
    }

    public double calculateHeuristicCost(CubeMatrix cubematrix){
        int correctlyStackedCubes = 0;
        CubeLine bottomLine = cubematrix.getCubeLines().get(2);
        for(Cube cube : bottomLine.getCubes()){
            //if cube is in correct position continue searching above it until you find a cube that is not in correct position
            if(cubematrix.cubeIsInFinalPosition(cube)){
                correctlyStackedCubes++;
                ArrayList<Cube> cubesAbove = cubematrix.getCubesAbove(cube);
                for(Cube cubeAbove : cubesAbove){
                    if(!cubematrix.cubeIsInFinalPosition(cubeAbove)){
                        continue;
                    }
                    else{
                        correctlyStackedCubes++;
                    }
                }
            }
        }
        int notCorrectlyStackedCubes = 3*cubematrix.getNumOfCubesPerLine() - correctlyStackedCubes;

        int numOfCubesThatBlockCubesNotInFinalPosition = 0;
        for(Cube cube : cubematrix.getNonZeroCubes()){
            if(!cubematrix.cubeIsInFinalPosition(cube)){
                numOfCubesThatBlockCubesNotInFinalPosition += cubeMatrix.getCubesAbove(cube).size();
            }
        }

        double heuristicCost = 2*notCorrectlyStackedCubes + 1*numOfCubesThatBlockCubesNotInFinalPosition ;
        return heuristicCost;
    }
    


    public void UCS(Node root){
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(node -> node.getTotalCost()));
        int numberOfNodesExpanded = 0;
        queue.add(root);
        Node result = null;
        search :{
            while(true){
                // System.out.println("Number of nodes expanded : " + numberOfNodesExpanded + "\r");
                System.out.print("Current total cost : " + queue.peek().getTotalCost()  + "\r");
                //if the node with the smallest cost is in order then we have found the solution
                if(queue.peek().getCubeMatrix().isInOrder()){
                    result = queue.peek();
                    break search;
                }
                //if not export the min value element from queue and expand it
                else{
                    ArrayList<Node> newNodesForQueue = expandNode(queue.poll());
                    queue.addAll(newNodesForQueue);
                    numberOfNodesExpanded ++;
                    newNodesForQueue.clear();
                }
            }
        }
        result.printPathFromRoot();
        System.out.println(ANSI_GREEN + "Total cost is :" + result.getTotalCost() + ANSI_RESET);
        System.out.println("Number of nodes expanded : " + numberOfNodesExpanded);
    }



    public void AStar(Node root){
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(node -> node.getTotalCost() + node.getHeuristicCost()));
        int numberOfNodesExpanded = 0;
        //ask user to press ENTER
        System.out.println("Press enter to sort the cube matrix");
        try{
            System.in.read();
        }
        catch(Exception e){
            System.out.println(e);
        }
        //add root to queue
        queue.add(root);
        Node result = null;
        search :{
            while(true){  
                System.out.print("Number of nodes expanded : " + numberOfNodesExpanded + "\r");
                if(queue.peek().getCubeMatrix().isInOrder()){
                    result = queue.peek();
                    break search;
                }
                else{
                    ArrayList<Node> newNodesForQueue = expandNode(queue.poll());
                    queue.addAll(newNodesForQueue);
                    numberOfNodesExpanded ++;
                    newNodesForQueue.clear();
                }
            }
        }
        result.printPathFromRoot();
        System.out.println(ANSI_GREEN + "Total cost is :" + result.getTotalCost() + ANSI_RESET);
        System.out.println("Number of nodes expanded : " + numberOfNodesExpanded);
    }



    public CubeMatrix getCubeMatrix() {
        return this.cubeMatrix;
    }


    public void setCubeMatrix(CubeMatrix cubeMatrix) {
        this.cubeMatrix = cubeMatrix;
    }


    public double getFinalCost() {
        return this.finalCost;
    }


    public void setFinalCost(double finalCost) {
        this.finalCost = finalCost;
    }
    


    public static void main(String[] args) {
        //TODO
        Botaki20 botaki = new Botaki20();
        CubeManager cubeManager = new CubeManager();
        cubeManager.requestCubeMatrix();
        botaki.setCubeMatrix(cubeManager.getCubeMatrix());
        
        cubeManager.printCubeLinesWithInvisibleCubes();
        Node root  = new Node(cubeManager.getCubeMatrix());
        root.setParent(null);

        botaki.UCS(root);
    }    
}

