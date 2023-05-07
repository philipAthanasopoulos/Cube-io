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

    public Node calculateAllPossibleMovesForCube(Cube cube){
        //TODO
        //find all free positions to move to
        ArrayList<Cube> possibleMoves = new ArrayList<Cube>();
        for(CubeLine line : cubeMatrix.getCubeLines()){
            for(Cube cubeToCheck : line.getCubes()){
                if(cubeMatrix.positionIsFreeToMoveTo(cubeToCheck)) possibleMoves.add(cubeToCheck);
            }
        }

        //create a new Node for each possible move
        Node result = new Node();
        for(Cube cubeToMoveTo : possibleMoves){
            Node newNode = new Node();
            newNode.setCost(calculateCost(cube , cubeToMoveTo));
            newNode.setParent(result);
            result.addChild(newNode);
            newNode.setCubeMatrix(cubeMatrix.copy());
        }

        return result;

    }


    public Node createTreeWithAllMoves(CubeMatrix cubeMatrix){
        Node result = new Node();

        System.out.println("Creating tree with all possible moves");

        for(CubeLine line : cubeMatrix.getCubeLines()){
            for(Cube cube : line.getCubes()){
                Node newNode = calculateAllPossibleMovesForCube(cube);
                result.addChild(newNode);
                CubeMatrix matrixOfChild = newNode.getCubeMatrix();
                if(newNode.getCost() != 0) createTreeWithAllMoves(matrixOfChild);
            }
        }

        return result;
    }

    public void moveCube() {
        
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
        Node result = botaki.createTreeWithAllMoves(botaki.getCubeMatrix());
        result.printTree();
        
    }



    
}
