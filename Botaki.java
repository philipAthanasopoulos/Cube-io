import java.util.ArrayList;

public class Botaki {
    private CubeMatrix cubeMatrix;
    
    public Botaki(){
        //TODO
    }
    
    
    public int calculateCost(Cube cube , Cube target) {
        int cost = 0;
        int yNext = target.getYPos();
    
        if(cube.getYPos() == yNext) cost += 0.75;
        //if cube goes on a line of higher index cost is 0.5*(curY - nextY)
        else if(cube.getYPos() < yNext) cost += 0.5*(yNext - cube.getYPos());
        //if cube goes on a line of lower index cost is (nextY - curY)
        else cost += (cube.getYPos() - yNext);
    
        return cost;
    }

    public Node calculateAllPossibleMovesForCube(Cube cube , Node parent){
        //TODO
        //find all free positions to move to
        ArrayList<Cube> possibleMoves = new ArrayList<Cube>();
        for(CubeLine line : cubeMatrix.getCubeLines()){
            for(Cube cubeToCheck : line.getCubes()){
                if(cubeMatrix.positionIsFreeToMoveTo(cubeToCheck)) possibleMoves.add(cubeToCheck);
            }
        }

        //create a new Node for each possible move
        
        for(Cube cubeToMoveTo : possibleMoves){
            Node newNode = new Node();
            newNode.setCost(calculateCost(cube , cubeToMoveTo));
            newNode.setParent(parent);
            parent.addChild(newNode);
            newNode.setCubeMatrix(cubeMatrix.copy());
            newNode.setTotalCost(parent.getTotalCost() + newNode.getCost());
        }

        return parent;

    }


    // public Node createTreeWithAllMoves(CubeMatrix cubeMatrix , Node parent ,Cube cubeToSort){
        

    //     System.out.println("Creating tree with all possible moves");

    //     for(CubeLine line : cubeMatrix.getCubeLines()){
    //         for(Cube cube : line.getCubes()){
    //             Node newNode = calculateAllPossibleMovesForCube(cube ,parent);
    //             parent.addChild(newNode);
    //         }
    //     }
        
    //     int lowestCost = Integer.MAX_VALUE;
    //     Node lowestCostNode = null;
    //     for(Node child : parent.getChildren()){
    //         if(cubeMatrix.cubeIsInFinalPosition(cubeToSort)) return parent;
    //         else if (child.getCost() < lowestCost) {
    //             lowestCost = child.getTotalCost();
    //             lowestCostNode = child;
    //         }
    //     }

    //     createTreeWithAllMoves(lowestCostNode.getCubeMatrix() , lowestCostNode , cubeToSort);

    //     return parent;
    // }

    

    public void expandNode(Node node){
        //TODO
        for(CubeLine line : node.getCubeMatrix().getCubeLines()){
            for(Cube cube : line.getCubes()){
                Node newNode = calculateAllPossibleMovesForCube(cube ,node);
                node.addChild(newNode);
            }
        }   
    }

    public void expandTreeWithBFS(ArrayList<Node> nodesToExpand){
        //TODO
        
        ArrayList<Node> newNodesToExpand = new ArrayList<Node>();
        for(Node node : nodesToExpand){
            expandNode(node);
            for(Node child : node.getChildren()){
                newNodesToExpand.add(child);
            }
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

        ArrayList<Node> nodesToExpand = new ArrayList<Node>();
        nodesToExpand.add(result);
        result.printTree();

        botaki.expandTreeWithBFS(nodesToExpand);
        result.printTree();
        

       
        
    }



    
}
