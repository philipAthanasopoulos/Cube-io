import java.util.InputMismatchException;
import java.util.Scanner;

public class CubeManager {
    private CubeMatrix cubeMatrix;
    private double cost = 0;
    private Scanner input;
    //ansi red color code
    public static final String ANSI_RED = "\u001B[31m";
    //ansi reset color code
    public static final String ANSI_RESET = "\u001B[0m";


    public CubeManager() {
        
    }
    
    public void createCubeMatrix(int numOfCubesPerLine) {
        this.cubeMatrix = new CubeMatrix(numOfCubesPerLine);
    }

    public void requestCubeMatrix() {
        this.input = new Scanner(System.in);

        try {
            System.out.println("Enter the number of cubes : ");
            int numOfCubesPerLine = input.nextInt();
            if(numOfCubesPerLine < 1) throw new NullPointerException();
            createCubeMatrix(numOfCubesPerLine);
        } catch (NullPointerException | NumberFormatException | InputMismatchException e ) {
            // TODO: handle exception
            System.out.println(ANSI_RED + "You typed an invalid character. Please try again." + ANSI_RESET);
            requestCubeMatrix();
        }
    }

    public void moveCube(Cube cube , int xNext , int yNext){
        if(cubeMatrix.isMoveable(cube) && cubeMatrix.positionIsFree(xNext, yNext)){
            calculateAndIncreaseCost(cube , xNext , yNext);
            cubeMatrix.moveCube(cube, xNext, yNext);
            cubeMatrix.setPositionsForAllCubes();
            //increase cost
        }else{
            System.out.println(ANSI_RED + "You can't move the cube to that position. Please try again." + ANSI_RESET);
        }
    }

    private void calculateAndIncreaseCost(Cube cube, int xNext, int yNext) {
        //if cube moves on the same line cost is 0.75
        if(cube.getYPos() == yNext) cost += 0.75;
        //if cube goes on a line of higher index cost is 0.5*(curY - nextY)
        else if(cube.getYPos() < yNext) cost += 0.5*(yNext - cube.getYPos());
        //if cube goes on a line of lower index cost is (nextY - curY)
        else cost += (cube.getYPos() - yNext);
    }

    public void increaseCost(float costToAdd){
        this.cost += costToAdd;
    }
    public void calculationOfMove(int curY, int nextY){
        
            if(nextY > curY) cost =  (nextY - curY);
            else if (nextY < curY) cost =  0.5*(curY - nextY);
            else cost = 0.75;

    }
    public boolean cubesAreInOrder(){
        for(CubeLine cubeLine : cubeMatrix.getCubeLines()){
            if(!cubeLine.isInOrder()){
                return false;
            }
        }
        return true;
    }

    public void printCubeMatrix(){
        
        System.out.println("\u001B[33m");

        this.cubeMatrix.printCubeMatrix();

        System.out.println("\u001B[0m");
    }
    
    public CubeMatrix getCubeMatrix() {
        return this.cubeMatrix;
    }

    public void setCubeMatrix(CubeMatrix cubeMatrix) {
        this.cubeMatrix = cubeMatrix;
    }

    public double getCost() {
        return this.cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public Scanner getInput() {
        return this.input;
    }

    public void setInput(Scanner input) {
        this.input = input;
    }

    public Cube getCube(int number) {
        for(CubeLine cubeLine : cubeMatrix.getCubeLines()){
            for(Cube cube : cubeLine.getCubes()){
                if(cube.getCubeNumber() == 0) continue;
                if(cube.getCubeNumber() == number) return cube;
            }
        }
        return null;
    }

    public void printCubeLinesWithInvisibleCubes(){
        this.cubeMatrix.printCubeLinesWithInvisibleCubes();
    }

    public static void main(String[] args) {
        CubeManager cubeManager = new CubeManager();
        cubeManager.createCubeMatrix(2);
        cubeManager.printCubeMatrix();
        cubeManager.moveCube(cubeManager.getCube(1), 3, 2);
        cubeManager.printCubeLinesWithInvisibleCubes();

        cubeManager.moveCube(cubeManager.getCube(1), 0, 0);
        cubeManager.printCubeLinesWithInvisibleCubes();
        //print cost
        System.out.println(cubeManager.getCost());
        System.out.println(cubeManager.getCubeMatrix().cubeIsInFinalPosition(cubeManager.getCube(2)));


    }


}
