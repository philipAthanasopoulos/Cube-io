import java.sql.Array;
import java.util.ArrayList;

public class Botaki {
    private CubeMatrix cubeMatrix;
    
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
            System.out.println("Can move cube " + cubeToMove.getCubeNumber() + " to position :" + cubeToMoveTo.getXPos() + " , " + cubeToMoveTo.getYPos());
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
                // if(matrix.cubeIsInFinalPosition(cube)) continue;
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
            return;
        }
        expandTreeWithBFS(root);

        int numOfCubeToSort = cubeToSort.getCubeNumber();
        while(true){
            //ask user to press enter to continue
            System.out.println("Press enter to continue");
            try{
                System.in.read();
            }  
            catch(Exception e){
                System.out.println(e);
            }


            double minTotalCost = Double.MAX_VALUE;
            ArrayList<Node> nodesToExpand = new ArrayList<Node>();


            //find the value of the smallest total cost
            for(Node child : root.getDeepestChildren()){
                if(child.getTotalCost() < minTotalCost) minTotalCost = child.getTotalCost();
            }
            System.err.println("Min total cost is " + minTotalCost);

            //find all nodes with the smallest total cost and add them to the nodesToExpand list
            for(Node child : root.getDeepestChildren()){
                if(child.getCubeMatrix().cubeIsInFinalPosition(numOfCubeToSort)){
                    System.out.println("Cube " + numOfCubeToSort + " is in final position");
                    //print cube xpos and ypos
                    System.out.println("Cube " + numOfCubeToSort + " is in position " + child.getCubeMatrix().getCube(numOfCubeToSort).getXPos() + " , " + child.getCubeMatrix().getCube(numOfCubeToSort).getYPos());

                    System.err.println("This is the final cube matrix");
                    child.getCubeMatrix().printCubeLinesWithInvisibleCubes();
                    //change roots cube matrix to the final cube matrix
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
        //find smallest cube that is not in final position and sort it with ucs
        CubeMatrix matrix = root.getCubeMatrix();

        //
        int iterations = matrix.getCubes().size();

        for(int i = 0 ; i < iterations ; i++){
            //ask user to press enter to continue
            System.out.println("Press enter to continue");
            try{
                System.in.read();
            }
            catch(Exception e){
                System.out.println(e);
            }
            ArrayList<Cube> cubesToSort = matrix.getCubes();
            Cube smallestCubeToSort = new Cube(Integer.MAX_VALUE);
            for(Cube cube : cubesToSort){
                if(cube.getCubeNumber() < smallestCubeToSort.getCubeNumber() && !matrix.cubeIsInFinalPosition(cube)) smallestCubeToSort = cube;
            }
            System.out.println("Smallest cube to sort is " + smallestCubeToSort.getCubeNumber());
            UCS(root , smallestCubeToSort);
            cubesToSort.remove(smallestCubeToSort);
        }

    }




    public CubeMatrix getCubeMatrix() {
        return this.cubeMatrix;
    }

    public void setCubeMatrix(CubeMatrix cubeMatrix) {
        this.cubeMatrix = cubeMatrix;
    }

    public static void main(String[] args) {
        //TODO
        Botaki botaki = new Botaki();
        CubeManager cubeManager = new CubeManager();
        cubeManager.requestCubeMatrix();
        botaki.setCubeMatrix(cubeManager.getCubeMatrix());
        //get cube number 1
        Cube cube = cubeManager.getCube(1);
        
        cubeManager.printCubeLinesWithInvisibleCubes();
        Node result  = new Node(cubeManager.getCubeMatrix());
        Cube cubeToSort = cubeManager.getCube(1);
        

        botaki.UCS(result, cubeToSort);



        


        

        
        
        
       

        

        

       
        
    }



    
}
