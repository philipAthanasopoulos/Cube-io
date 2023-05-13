import java.util.ArrayList;
import java.util.Collections;

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
            Node newNode = new Node();
            newNode.setCost(calculateCost(cube , cubeToMoveTo));
            newNode.setParent(parent);
            parent.addChild(newNode);

            //create a new cubeMatrix for each possible move
            CubeMatrix newCubeMatrix = parentCubeMatrix.copy();
            
            //get the cube that is to be moved
            Cube cubeToMove = newCubeMatrix.getCube(cube.getCubeNumber());

            //move the cube
            newCubeMatrix.moveCube(cubeToMove , cubeToMoveTo.getXPos() , cubeToMoveTo.getYPos() );
            newNode.setCubeMatrix(newCubeMatrix);
            //calculate heuristic cost
            // newNode.setHeuristicCost(calculateHeuristicCost(cubeToMove, newCubeMatrix));
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
                    root.printPathToChildNode(child);
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

            //find the value of the smallest heuristic cost
            double minHeuristicCost = Double.MAX_VALUE;
            ArrayList<Node> nodesToExpand = new ArrayList<Node>();
            for(Node child : root.getDeepestChildren()){
                double heuristicCost = calculateHeuristicCost(child.getCubeMatrix().getCube(numOfCubeToSort) , child.getCubeMatrix());
                child.setHeuristicCost(heuristicCost);
                if(heuristicCost < minHeuristicCost) minHeuristicCost = heuristicCost;
            }

            //find all nodes with the smallest heuristic cost and add them to the nodesToExpand list
            for(Node child : root.getDeepestChildren()){
                if(child.getCubeMatrix().cubeIsInFinalPosition(numOfCubeToSort)){
                    //change roots cube matrix to the final cube matrix
                    root.printPathToChildNode(child);
                    root.setCubeMatrix(child.getCubeMatrix());
                    //clear the children of the root
                    root.getChildren().clear();
                    return;
                }
                if(child.getHeuristicCost() == minHeuristicCost) nodesToExpand.add(child);
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
        //TODO
        //Find cost to free cube and cost to free its final position
        double costToFreeCube = 0;
        double costToFreeFinalPosition = 0;
        double totalCost = 0;

       ArrayList<Cube> cubesThatBlock =new ArrayList<Cube>();

       //add cubes that block the cube to correct
       //add all cubes above cube to correct
       cubesThatBlock.addAll(cubeMatrix.getCubesAbove(cube));

       //add all cubes that block final position
       cubesThatBlock.addAll(cubeMatrix.getCubesThatBlockFinalPosition(cube));

       //remove cubes that block one by one
       while(true){
            if(cubesThatBlock.size() == 0 ) break;
            //find all movable cubes
            ArrayList<Cube> movableCubes = new ArrayList<Cube>();
            for(Cube cubeThatBlocks : cubesThatBlock){
                if(cubeMatrix.isMoveable(cubeThatBlocks)){
                    movableCubes.add(cubeThatBlocks);
                }
            }

            //create all possible moves and choose the one with the least cost
            ArrayList<Node> possibleMoves = new ArrayList<Node>();
            for(Cube movableCube : movableCubes){
                Node nodeOfCube = new Node(cubeMatrix);
                calculateAllPossibleMovesForCube(cube, nodeOfCube);
                possibleMoves.addAll(nodeOfCube.getChildren());
            }
            //choose move with smallest cost
            Node bestMove = new Node(cubeMatrix);
            double minCost = Double.MAX_VALUE;
            for(Node move : possibleMoves){
                if(move.getTotalCost() < minCost){
                    minCost = move.getTotalCost();
                }
            }
            
            for(Node move  : possibleMoves){
                if(move.getCost() == minCost){
                    bestMove = move; // move found
                }
            }

            cubeMatrix = bestMove.getCubeMatrix();
            totalCost += bestMove.getCost();
            cubesThatBlock.remove(bestMove.getCubeMatrix().getCube(cube.getCubeNumber()));
            
       }
       //move cube to correct position
       ArrayList<Integer> finalCordinates = cubeMatrix.getFinalPositionOfCube(cube);
       Cube finalPosition = cubeMatrix.getCube(finalCordinates.get(0) , finalCordinates.get(1));
       cubeMatrix.moveCube(cube , finalPosition);

       return totalCost;

       

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
        // System.out.println(botaki.calculateHeuristicCost(result.getCubeMatrix().getCube(1) , result.getCubeMatrix()));

        botaki.sortCubesWithUCS(result);        
    }    
}
