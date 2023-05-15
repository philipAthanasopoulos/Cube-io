import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Botaki {
    private CubeMatrix cubeMatrix;
    private double finalCost = 0;
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";
    
    public Botaki(){
        //TODO
    }
    
    
    public double calculateCost(Cube cube , Cube target) {
        double cost = 0;
        int yNext = target.getYPos();
    
        if(cube.getYPos() == yNext) cost += 0.75;
        //if cube goes on a line of higher index cost is 0.5*(curY - nextY)
        else if(cube.getYPos() < yNext) cost += 0.5*(yNext - cube.getYPos());
        //if cube goes on a line of lower index cost is (nextY - curY)
        else cost += (cube.getYPos() - yNext);
        return cost;
    }

    public void calculateAllPossibleMovesForCube(Cube cube , Node parent){
        //TODO

        CubeMatrix parentCubeMatrix = parent.getCubeMatrix();
        //find all free positions to move to
        ArrayList<Cube> possibleMoves = new ArrayList<Cube>();
        for(CubeLine line : parentCubeMatrix.getCubeLines()){
            for(Cube cubeToCheck : line.getCubes()){
                boolean isRightAbove = parentCubeMatrix.getBelowCube(cubeToCheck).getCubeNumber() == cube.getCubeNumber();
                if(parentCubeMatrix.positionIsFreeToMoveTo(cubeToCheck) && !isRightAbove) possibleMoves.add(cubeToCheck);
            }
        }

        //create a new Node for each possible move
        for(Cube cubeToMoveTo : possibleMoves){
            Node newNode = new Node(parent);
            newNode.setCost(calculateCost(cube , cubeToMoveTo));
            parent.addChild(newNode);

            //create a new cubeMatrix for each possible move
            CubeMatrix newCubeMatrix = parentCubeMatrix.copy();
            
            //get the cube that is to be moved
            Cube cubeToMove = newCubeMatrix.getCube(cube.getCubeNumber());

            //move the cube
            newCubeMatrix.moveCube(cubeToMove , cubeToMoveTo.getXPos() , cubeToMoveTo.getYPos() );
            newNode.setCubeMatrix(newCubeMatrix);
            //calculate heuristic cost
            newNode.setHeuristicCost(calculateHeuristicCost(newCubeMatrix));
            newNode.setTotalCost(parent.getTotalCost() + newNode.getCost());
        }
    }


    public void expandNode(Node node){
        //TODO
        CubeMatrix matrix = node.getCubeMatrix();
        for(CubeLine line : matrix.getCubeLines()){
            for(Cube cube : line.getCubes()){
                if(cube.getCubeNumber() != 0 && matrix.isMoveable(cube)) calculateAllPossibleMovesForCube(cube ,node);
            }
        }   
    }


    public void expandTreeWithBFS(Node root){
        //TODO
        if(root.getChildren().size() == 0) expandNode(root);
        else for(Node node : root.getDeepestChildren()) expandNode(node);  
    }


    public void expandTreeWithBFS(ArrayList<Node> nodesToExpand){
        //TODO
        for(Node node : nodesToExpand){
            expandNode(node); 
        }
    }


    public void UCS(Node root , Cube cubeToSort){
        //TODO

        

        //if cube is in correct position return
        if(root.getCubeMatrix().cubeIsInFinalPosition(cubeToSort.getCubeNumber())){
            System.out.println("Cube " + cubeToSort.getCubeNumber() + " is in final position");
            System.out.println("Total cost is " + root.getTotalCost());
            return;
        }
        expandTreeWithBFS(root);

        int numOfCubeToSort = cubeToSort.getCubeNumber();
        while(true){
            root.cleanTree();

            //find the value of the smallest total cost
            double minTotalCost = Double.MAX_VALUE;
            ArrayList<Node> nodesToExpand = new ArrayList<Node>();
            for(Node child : root.getDeepestChildren()){
                if(child.getTotalCost() < minTotalCost) minTotalCost = child.getTotalCost();
                root.setTotalCost(minTotalCost);
            }

            //find all nodes with the smallest total cost and add them to the nodesToExpand list
            for(Node child : root.getDeepestChildren()){
                if(child.getCubeMatrix().cubeIsInFinalPosition(numOfCubeToSort)){
                    //change roots cube matrix to the final cube matrix
                    //print all moves from history
                    System.out.println("Cube " + numOfCubeToSort + " is in final position");
                    System.out.println("Total cost is " + child.getTotalCost());
                    System.out.println("Moves to sort cube " + numOfCubeToSort + " are: ");
                    for(CubeMatrix move : child.getHistoryOfMoves()){
                        move.printCubeMatrix();
                    }
                    root.setCubeMatrix(child.getCubeMatrix());
                    //clear the children of the root
                    root.getChildren().clear();
                    return;
                }
                if(child.getTotalCost() == minTotalCost) nodesToExpand.add(child);
            }
            expandTreeWithBFS(nodesToExpand);
        }
    }


    public void sortCubesWithUCS(Node root){
        //TODO
        CubeMatrix matrix = root.getCubeMatrix();

        int cubeToSort = 1;
        while(cubeToSort <= matrix.getNonZeroCubes().size()){
            System.out.println("Sorting cube " + cubeToSort);
            //ask user to press enter to continue
            System.out.println("Press"+ ANSI_GREEN + " ENTER" + ANSI_RESET + " to sort the next cube");
            try{
                System.in.read();
            }
            catch(Exception e){
                System.out.println(e);
            }

            UCS(root, matrix.getCube(cubeToSort));
            cubeToSort++;
            //print total cost
            double finalCost = Double.MAX_VALUE;
            if(root.getChildren().size() == 0) finalCost = root.getTotalCost();
            else{
                for(Node child : root.getDeepestChildren()){
                    if(child.getTotalCost() < finalCost) finalCost = child.getTotalCost();
                }
            }
        }

        System.out.println("All cubes are in order");
        System.out.println("Total cost is " + root.getTotalCost());
    }


    public void AStar(Node root , Cube cubeToSort){
        //TODO
        //if cube is in correct position return
        if(root.getCubeMatrix().cubeIsInFinalPosition(cubeToSort.getCubeNumber())){
            System.out.println("Cube " + cubeToSort.getCubeNumber() + " is in final position");
            System.out.println("Total cost is " + root.getTotalCost());
            return;
        }

        expandTreeWithBFS(root);


        int numOfCubeToSort = cubeToSort.getCubeNumber();
        while(true){

            //find the value of the smallest sum of total cost and heuristic cost
            double minTotalCost = Double.MAX_VALUE;
            ArrayList<Node> nodesToExpand = new ArrayList<Node>();
            for(Node child : root.getDeepestChildren()){
                if(child.getTotalCost() + child.getHeuristicCost() < minTotalCost) minTotalCost = child.getTotalCost() + child.getHeuristicCost();
                root.setTotalCost(minTotalCost);
            }


            //find all nodes with the smallest sum of total cost and heuristic cost and add them to the nodesToExpand list
            for(Node child : root.getDeepestChildren()){
                if(child.getCubeMatrix().cubeIsInFinalPosition(numOfCubeToSort)){
                    //change roots cube matrix to the final cube matrix
                    root.printPathToChildNode(child);
                    root.setCubeMatrix(child.getCubeMatrix());
                    //clear the children of the root
                    root.getChildren().clear();
                    return;
                }
                if(child.getTotalCost() + child.getHeuristicCost() == minTotalCost) nodesToExpand.add(child);
            }
            expandTreeWithBFS(nodesToExpand);
        }
    }


    public void sortCubesWithAStar(Node root){
        //TODO
        CubeMatrix matrix = root.getCubeMatrix();

        int cubeToSort = 1;
        while(cubeToSort <= matrix.getNonZeroCubes().size()){
            System.out.println("Sorting cube " + cubeToSort);
            //ask user to press enter to continue
            System.out.println("Press enter to sort the next cube");
            try{
                System.in.read();
            }
            catch(Exception e){
                System.out.println(e);
            }

            AStar(root, matrix.getCube(cubeToSort));
            cubeToSort++;
            //print total cost
            double finalCost = Double.MAX_VALUE;
            if(root.getChildren().size() == 0) finalCost = root.getTotalCost();
            else{
                for(Node child : root.getDeepestChildren()){
                    if(child.getTotalCost() < finalCost) finalCost = child.getTotalCost();
                }
            }
        }

        System.out.println("All cubes are in order");
        System.out.println("Total cost is " + root.getTotalCost());
    }



    public double calculateHeuristicCost(Cube cube , CubeMatrix cubeMatrix){
        double heuristicCost = 0;
        //heuristic cost is sum of blocking cubes , numOfCubesNotInFinalPosition and manhattan distance
        int numofCubesThatBlockFinalPosition = cubeMatrix.getCubesThatBlockFinalPosition(cube).size();
        int numofCubesThatBlockCubeToSort = cubeMatrix.getCubesAbove(cube).size();
        int manhattanDistance = cubeMatrix.getManhattanDistanceFromFinalPosition(cube);
        int numOfCubesNotInFinalPosition = 3*cubeMatrix.getNumOfCubesPerLine()  -  cubeMatrix.getNumOfCubesInFinalPosition();
        int numOfBlockedCubes = cubeMatrix.getNumOfBlockedCubes();

        int correctlyStackedCubes = 0;
        CubeLine bottomLine = cubeMatrix.getCubeLines().get(2);
        for(Cube cubeToCheck : bottomLine.getCubes()){
            //if cube is in correct position continue searching above it until you find a cube that is not in correct position
            if(cubeMatrix.cubeIsInFinalPosition(cubeToCheck)){
                correctlyStackedCubes++;
                ArrayList<Cube> cubesAbove = cubeMatrix.getCubesAbove(cubeToCheck);
                //reverse cubes above
                Collections.reverse(cubesAbove);
                for(Cube cubeAbove : cubesAbove){
                    if(!cubeMatrix.cubeIsInFinalPosition(cubeAbove)){
                        break;
                    }
                    else{
                        correctlyStackedCubes++;
                    }
                }
            }
        }
        int numOfCubesNotCorrectlyStacked = 3*cubeMatrix.getNumOfCubesPerLine() - correctlyStackedCubes;

        heuristicCost = 0.5*numofCubesThatBlockFinalPosition + 0.5*numofCubesThatBlockCubeToSort + numOfCubesNotCorrectlyStacked;
        return heuristicCost;
    }

    public double calculateHeuristicCost(CubeMatrix cubematrix){
        int heuristicCost = 0;
        //heuristic cost is sum of blocking cubes , numOfCubesNotInFinalPosition and manhattan distance
        
        int correctlyStackedCubes = 0;
        CubeLine bottomLine = cubematrix.getCubeLines().get(2);
        for(Cube cube : bottomLine.getCubes()){
            //if cube is in correct position continue searching above it until you find a cube that is not in correct position
            if(cubematrix.cubeIsInFinalPosition(cube)){
                correctlyStackedCubes++;
                ArrayList<Cube> cubesAbove = cubematrix.getCubesAbove(cube);
                //reverse cubes above
                // Collections.reverse(cubesAbove);
                for(Cube cubeAbove : cubesAbove){
                    if(!cubematrix.cubeIsInFinalPosition(cubeAbove)){
                        break;
                    }
                    else{
                        correctlyStackedCubes++;
                    }
                }
            }
        }

        int numOfCubesThatBlockCubesNotInFinalPosition = 0;
        for(Cube cube : cubematrix.getNonZeroCubes()){
            if(!cubematrix.cubeIsInFinalPosition(cube)){
                numOfCubesThatBlockCubesNotInFinalPosition += cubematrix.getCubesThatBlockFinalPosition(cube).size();
            }
        }

        int numOfCubesThatBlockCubeNotInFinalPosition = 0;
        for(Cube cube : cubematrix.getNonZeroCubes()){
            if(!cubematrix.cubeIsInFinalPosition(cube)){
                numOfCubesThatBlockCubeNotInFinalPosition += cubematrix.getCubesAbove(cube).size();
            }
        }


        int sumOfAllManhattanDistances = 0;
        for(Cube cube : cubematrix.getNonZeroCubes()){
            sumOfAllManhattanDistances += cubematrix.getManhattanDistanceFromFinalPosition(cube);
        }

        int numOfCubesNotInFinalPosition = 3*cubematrix.getNumOfCubesPerLine()  -  cubematrix.getNumOfCubesInFinalPosition();
            


        heuristicCost =  3*cubematrix.getNumOfCubesPerLine() - correctlyStackedCubes + numOfCubesThatBlockCubesNotInFinalPosition  ;
        return heuristicCost*1.0;

    }

    public void AStar2(Node root){
        //priority queue of type node
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(node -> node.getTotalCost() + node.getHeuristicCost()));
        queue.add(root);


       
        while (true) {
            if(root.getCubeMatrix().isInOrder()) break;
            Node currentNode = queue.poll();
    
            if (currentNode.getCubeMatrix().isInOrder()) {
                System.out.println("All cubes are in order");
                currentNode.printHistoryOfMoves();
                return;
            }
            double minCost = Double.MAX_VALUE;
            for(Node child : root.getDeepestChildren()){
                if(child.getTotalCost() + child.getHeuristicCost() < minCost){
                    minCost = child.getTotalCost() + child.getHeuristicCost();
                }
            }
    
            expandTreeWithBFS(currentNode);
    
            for (Node child : currentNode.getDeepestChildren()) {
                queue.add(child);
            }
            
        }
        System.out.println(root.getTotalCost());
 
    }

    public Node AStar3(Node root){
        //priority queue of type node
        PriorityQueue<Node> queue = new PriorityQueue<Node>(new Comparator<Node>(){
            @Override
            public int compare(Node node1 , Node node2){
                if(node1.getTotalCost() + node1.getHeuristicCost() > node2.getTotalCost() + node2.getHeuristicCost()) return 1;
                else if(node1.getTotalCost() + node1.getHeuristicCost() < node2.getTotalCost() + node2.getHeuristicCost()) return -1;
                else return 0;
            }
        });

        queue.add(root);

        Node result;

        
        while(true){
            //check if cubes have been sorted
            for(Node nodeInQueue : queue){
                if( nodeInQueue.getCubeMatrix().isInOrder()){
                    result = nodeInQueue;
                    break;
                }
            }

            //if not expand the nodes with the least sum of totalCost and Heuristic cost

            // export the min value element from queue and expand it
            expandNode(queue.poll());
            


        }

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
        Botaki botaki = new Botaki();
        CubeManager cubeManager = new CubeManager();
        cubeManager.requestCubeMatrix();
        botaki.setCubeMatrix(cubeManager.getCubeMatrix());
        
        cubeManager.printCubeLinesWithInvisibleCubes();
        Node result  = new Node(cubeManager.getCubeMatrix());
        result.setParent(null);

        botaki.AStar2(result);        
    }    
}
