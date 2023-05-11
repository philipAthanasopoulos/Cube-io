import java.util.ArrayList;
import java.util.Collections;

public class Botaki {
    private CubeMatrix cubeMatrix;
    private double finalCost = 0;
    
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
            System.out.println("Press enter to sort the next cube");
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
            return;
        }

        expandTreeWithBFS(root);

    }

    public double calculateHeuristicCost(Cube cube , CubeMatrix cubeMatrix){
        //TODO
        //Find cost to free cube and cost to free its final position
        double costToFreeCube = 0;
        double costToFreeFinalPosition = 0;

        if(cubeMatrix.isMoveable(cube)) costToFreeCube = 0;
        //calculate cost to free cube
        else{
            //find all cubes above the cube
            ArrayList<Cube> cubesAbove = new ArrayList<Cube>();
            Cube pivot = cubeMatrix.getAboveCube(cube);
            while(pivot.getCubeNumber() != 0){
                cubesAbove.add(pivot);
                pivot = cubeMatrix.getAboveCube(pivot);
            }

            //get all free positions to move to and remove positions on same y axis as cube
            ArrayList<Cube> freePositions = cubeMatrix.getFreePositionsToMoveTo();
            for(Cube freePosition : freePositions){
                if(freePosition.getYPos() == cubeMatrix.getFinalPositionOfCube(cube).get(1)) freePositions.remove(freePosition);
            }
            
            //reverse cubesAbove list (to work from bottom to top)
            Collections.reverse(cubesAbove);
            for(Cube cubeAbove : cubesAbove){
                //get a free position to move to
                Cube freePosition = freePositions.get(0);
                //move cube on that position
                cubeMatrix.moveCube(cubeAbove, freePosition);
                //add cost of move 
                costToFreeCube += calculateCost(cubeAbove, freePosition);
                //pop free position from list
                freePositions.remove(0);

            }
        }

        //calculate cost to free final position

        //find all cubes on and above the final position
        ArrayList<Cube> cubesAboveFinalPosition = new ArrayList<Cube>();
        ArrayList<Integer> finalPositionCordinates = cubeMatrix.getFinalPositionOfCube(cube);
        Cube pivot = cubeMatrix.getCube(finalPositionCordinates.get(0), finalPositionCordinates.get(1));
        while(pivot.getCubeNumber() != 0){
            cubesAboveFinalPosition.add(pivot);
            pivot = cubeMatrix.getAboveCube(pivot);
        }

        //get all free positions to move to and remove positions on same y axis as cube
        ArrayList<Cube> freePositions = cubeMatrix.getFreePositionsToMoveTo();
        for(Cube freePosition : freePositions){
            if(freePosition.getYPos() == cube.getYPos()) freePositions.remove(freePosition);
        }

        //reverse cubesAboveFinalPosition list (to work from bottom to top)
        Collections.reverse(cubesAboveFinalPosition);

        for(Cube cubeAbove : cubesAboveFinalPosition){
            //get a free position to move to
            Cube freePosition = freePositions.get(0);
            //move cube on that position
            cubeMatrix.moveCube(cubeAbove, freePosition);
            //add cost of move 
            costToFreeFinalPosition += calculateCost(cubeAbove, freePosition);
            //pop free position from list
            freePositions.remove(0);
        }

        //return sum of costs
        return costToFreeCube + costToFreeFinalPosition;
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

        botaki.sortCubesWithUCS(result);        
    }    
}
