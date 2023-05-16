import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Botaki20 {
    private CubeMatrix cubeMatrix;
    private double finalCost = 0;
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";
    
    public Botaki20(){
        //TODO
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
        //delete parent
        parent = null;

        return children;
    }


    public ArrayList<Node> expandNode(Node parent){
        //TODO
        ArrayList<Node> children = new ArrayList<Node>();

        CubeMatrix matrix = parent.getCubeMatrix();
        for(Cube cube : matrix.getCubes()){
            if(matrix.isMoveable(cube) && cube.getCubeNumber() != 0) children.addAll(calculateAllPossibleMovesForCube(cube, parent));
        }  

        return children;
    }

    public double calculateHeuristicCost(CubeMatrix cubematrix){
        //heuristic cost is sum of blocking cubes , numOfCubesNotInFinalPosition and manhattan distance
        
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


        // int sumOfAllManhattanDistances = 0;
        // for(Cube cube : cubematrix.getNonZeroCubes()){
        //     sumOfAllManhattanDistances += cubematrix.getManhattanDistanceFromFinalPosition(cube);
        // }

        int numOfCubesNotInFinalPosition = 3*cubematrix.getNumOfCubesPerLine()  -  cubematrix.getNumOfCubesInFinalPosition();
            

        int blocksNeededToSortNextLine = 0;
        for(int lineIndex = 2 ; lineIndex > -1 ; lineIndex--){
            CubeLine line = cubematrix.getCubeLine(lineIndex);
            if(line.isInOrder()) continue;
            else{
                for(Cube cube : line.getCubes()){
                    if(cubematrix.cubeIsInFinalPosition(cube)) blocksNeededToSortNextLine++; 
                }
                break;
            }
        }

        // double verticalManhattanDistances = 0;
        // double horizontalManhattanDistances = 0;

        // for(Cube cube : cubematrix.getNonZeroCubes()){
        //     verticalManhattanDistances += Math.abs(cube.getYPos() - cubematrix.getFinalPositionOfCube(cube).get(1));
        //     horizontalManhattanDistances += Math.abs(cube.getXPos() - cubematrix.getFinalPositionOfCube(cube).get(0));
        // }
        

        
        // double manhattanDistance = 1.125*verticalManhattanDistances + 0.75*horizontalManhattanDistances;


        int numOfBlockedCubesNotINFinalPosition = 0;
        for(Cube cube : cubematrix.getNonZeroCubes()){
            if(!cubematrix.cubeIsInFinalPosition(cube) && cubematrix.cubeIsBlocked(cube)){
                numOfBlockedCubesNotINFinalPosition++;
            }
                
            
        }

        int cubesInWrongPositionBottomToTop = 0;
        int weight = 1;
        for(CubeLine line : cubematrix.getCubeLines()){
            for(Cube cube : line.getCubes()){
                if(!cubematrix.cubeIsInFinalPosition(cube)){
                    cubesInWrongPositionBottomToTop += weight;
                }
            }
            weight += 2;
        }

        int cubesInWrongPositionLeftToRight = 0;
        weight = 1;
        for(int xIndex = 0 ; xIndex < cubematrix.getNumOfCubesPerLine() ; xIndex++){
            for(int yIndex = 0 ; yIndex < 3 ; yIndex++){
                Cube cube = cubematrix.getCube(xIndex , yIndex);
                if(!cubematrix.cubeIsInFinalPosition(cube) && cube.getCubeNumber() != 0){
                    cubesInWrongPositionBottomToTop += weight;
                }
            }
            weight += 2;
        }

        //find the verical distance of each cube from its final position
        double verticalCosts = 0;
        for(Cube cube : cubematrix.getNonZeroCubes()){
            int verticalDistance = cube.getYPos() - cubematrix.getFinalPositionOfCube(cube).get(1);
            if(verticalDistance < 0) verticalCosts += verticalDistance;
            if(verticalDistance == 0) verticalCosts += 0.75;
            if(verticalDistance > 0) verticalCosts += 0.5*verticalDistance;

        }

            
        

        

        
        double heuristicCost = notCorrectlyStackedCubes + numOfCubesThatBlockCubesNotInFinalPosition ;
        return heuristicCost;
    }
    


    public void UCS(Node root){
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(node -> node.getTotalCost()));


        queue.add(root);
        Node result = null;
        
        search :{
            while(true){
                //check if cubes have been sorted
                for(Node nodeInQueue : queue){
                    if( nodeInQueue.getCubeMatrix().isInOrder()){
                        result = nodeInQueue;
                        break search;
                    }
                }
                //if not export the min value element from queue and expand it
                ArrayList<Node> newNodesForQueue = expandNode(queue.poll());
                queue.addAll(newNodesForQueue);
    
            }

        }

        
        result.printHistoryOfMoves();
        result.getCubeMatrix().printCubeMatrix();   
        System.out.println(ANSI_GREEN + "Total cost is :" + result.getTotalCost() + ANSI_RESET);


    }



    public void AStar(Node root){
        //ask user to press ENTER
        System.out.println("Press enter to sort the cube matrix");
        try{
            System.in.read();
        }
        catch(Exception e){
            System.out.println(e);
        }

        //priority queue of type node
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(node -> node.getTotalCost() + node.getHeuristicCost()));


        //add root to queue
        queue.add(root);
        Node result = null;

        search :{
            while(true){
                //check if cubes have been sorted
                for(Node nodeInQueue : queue){
                    if( nodeInQueue.getCubeMatrix().isInOrder()){
                        result = nodeInQueue;
                        break search;
                    }
                }
                //if not export the min value element from queue and expand it
                ArrayList<Node> newNodesForQueue = expandNode(queue.poll());
                queue.addAll(newNodesForQueue);
    
            }

        }

        
        result.printHistoryOfMoves();
        result.getCubeMatrix().printCubeMatrix();   
        System.out.println(ANSI_GREEN + "Total cost is :" + result.getTotalCost() + ANSI_RESET);


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

