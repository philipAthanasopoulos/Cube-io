import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class CubeMatrix {

    private ArrayList<CubeLine> cubeLines;
    private int numOfCubesPerLine;
    private double costOfMove = 0;


    public CubeMatrix(int numOfCubesPerLine) {
        this.cubeLines = new ArrayList<CubeLine>();
        this.numOfCubesPerLine = numOfCubesPerLine;
        int start = 1;
        //create 3 CubeLines from 1 to 3*numOfCubesPerLine
        CubeLine cubeLine1 = new CubeLine(start , numOfCubesPerLine , numOfCubesPerLine);
        CubeLine cubeLine2 = new CubeLine(start + numOfCubesPerLine , numOfCubesPerLine , numOfCubesPerLine);
        CubeLine cubeLine3 = new CubeLine(start + 2*numOfCubesPerLine , numOfCubesPerLine , numOfCubesPerLine);

        //add the 3 CubeLines to the CubeMatrix
        cubeLines.add(cubeLine1);
        cubeLines.add(cubeLine2);
        cubeLines.add(cubeLine3);

        Collections.shuffle(cubeLines);
        //add 3*k free posiotions on line with index 2
        for(int i = 0 ; i < 3*numOfCubesPerLine ; i++){
            cubeLines.get(2).getCubes().add(new Cube(0));
        }

        setPositionsForAllCubes();
    }
    

    public CubeMatrix() {
        this.cubeLines = new ArrayList<CubeLine>();
    }


    public boolean isMoveable(Cube cube) {
        return getAboveCube(cube).getCubeNumber() == 0 ? true : false;
    }

    public Cube findSmallestMovableCube(){
        Cube result = new Cube(Integer.MAX_VALUE);
        for(CubeLine cubeLine : cubeLines){
            for(Cube cube : cubeLine.getCubes()){
                if(cube.getCubeNumber() == 0) continue;
                if(isMoveable(cube) && cube.getCubeNumber() < result.getCubeNumber()) result = cube;
            }
        }
        return result;
    }

    public boolean positionIsFree(int xNext, int yNext) {
        return getCube(xNext , yNext).getCubeNumber() == 0 ? true : false;
    }

    public boolean positionIsFree(Cube cube){
        return cube.getCubeNumber() == 0 ? true : false;
    }

    public boolean positionBelowIsFree(Cube cube){
        return getBelowCube(cube).getCubeNumber() == 0 ? true : false;
    }

    public boolean positionIsFreeToMoveTo(Cube cube) {
        return positionIsFree(cube) && !positionBelowIsFree(cube)  ? true : false;
        
    }

    public ArrayList<Cube> getFreePositionsToMoveTo(){
        ArrayList<Cube> result = new ArrayList<Cube>();
        for(Cube cube : getCubeLines().get(2).getCubes()){
            if(positionIsFreeToMoveTo(cube)) result.add(cube);
        }
        return result;
        
    }

    public Cube findFreePosition(){
        for(Cube cube : getCubeLines().get(2).getCubes()){
            if(this.positionIsFree(cube)) return cube;
        }
        //will never happen
        return null;
    }

    

    public void moveCube(Cube cube, int xNext, int yNext) {
        int temp = cube.getCubeNumber();
        getCube(xNext , yNext).setCubeNumber(temp);
        cube.setCubeNumber(0);
        setPositionsForAllCubes();
    }

    public void moveCube(Cube cube, Cube targetCube) {
        moveCube(cube, targetCube.getXPos(), targetCube.getYPos());
    }

    public ArrayList<CubeLine> getCubeLines() {
        return this.cubeLines;
    }

    public Cube getAboveCube(Cube cube) {
        if(cube.getYPos() == 0) return new Cube(0);
        try {
            return cubeLines.get(cube.getYPos() - 1).getCubes().get(cube.getXPos());
        } catch (IndexOutOfBoundsException e) {
            return new Cube(0);
        }
    }

    public ArrayList<Cube> getCubesAbove(Cube cube){
        ArrayList<Cube> result = new ArrayList<Cube>();
        Cube pivot = getAboveCube(cube);
        while(pivot.getCubeNumber() != 0){
            result.add(pivot);
            pivot = getAboveCube(pivot);
        }
        return result;
    }


    public Cube getBelowCube(Cube cube) {
        if(cube.getYPos() == 2) return new Cube(-1);
        try {
            return cubeLines.get(cube.getYPos() + 1).getCubes().get(cube.getXPos());
        } catch (IndexOutOfBoundsException e) {
            return new Cube(-1);
        }
        
    }

    public Cube getCube(int xPos, int yPos) {
        return this.cubeLines.get(yPos).getCubes().get(xPos);
    }

    public Cube getCube(int cubeNumber) {
        for(CubeLine cubeLine : cubeLines){
            for(Cube cube : cubeLine.getCubes()){
                if(cube == null) continue;
                if(cube.getCubeNumber() == cubeNumber) return cube;
            }
        }
        return null;
    }

    public Cube getCube(Cube cube){
        return getCube(cube.getXPos() , cube.getYPos());
    }

    public void setPositionsForAllCubes() {
        for(CubeLine line : cubeLines){
            for(Cube cube : line.getCubes()){
                cube.setYPos(cubeLines.indexOf(line));
                cube.setXPos(line.getCubes().indexOf(cube));
            }
        }
    }

    public void printCubeLinesWithInvisibleCubes() {
        for(CubeLine cubeLine : cubeLines){
            cubeLine.printCubeLineWithInvisibleCubes();
        }
        System.out.println("================================================");
    }
    

    public boolean isInOrder(){ 
        for(Cube cube : getCubes()){
            if(cube.getCubeNumber() == 0) continue;
            if (!cubeIsInFinalPosition(cube)) return false;
        }
        return true;
    }

    public CubeLine getCubeLine(int yNext) {
        return cubeLines.get(yNext);   
    }
    public void setCubeLines(ArrayList<CubeLine> cubeLines) {
        this.cubeLines = cubeLines;
    }

    public int getNumOfCubesPerLine() {
        return this.numOfCubesPerLine;
    }

    public void setNumOfCubesPerLine(int numOfCubesPerLine) {
        this.numOfCubesPerLine = numOfCubesPerLine;
    }


    public double getCostOfMove() {
        return this.costOfMove;
    }

    public void setCostOfMove(double costOfMove) {
        this.costOfMove = costOfMove;
    }


    

    public void printCubeMatrix() {
        //TODO
        for(CubeLine cubeLine : cubeLines){
            cubeLine.printCubeLine();
        }
        System.out.println("================================================");

    }



    public CubeMatrix copy() {
        CubeMatrix copy = new CubeMatrix();
        copy.setNumOfCubesPerLine(this.numOfCubesPerLine);
        for(CubeLine cubeLine : cubeLines){
            copy.getCubeLines().add(cubeLine.copy());
        }
        copy.setPositionsForAllCubes();
        return copy;
    }

    public ArrayList<Integer> getFinalPositionOfCube(Cube cube){
        ArrayList<Integer> result = new ArrayList<>();
        int counter = 0;
        int correctXPos = 0;
        int correctYPos = 0;

        for(int lineIndex = 2; lineIndex > -1 ; lineIndex--){
            for(int cubeIndex = 0 ; cubeIndex < getNumOfCubesPerLine(); cubeIndex++){
                counter++;
                if(counter == cube.getCubeNumber()){
                    correctXPos = cubeIndex;
                    correctYPos = lineIndex;
                    break;
                }
            }
        }
        result.add(correctXPos);
        result.add(correctYPos);
        return result;
    }

   
    



    public boolean cubeIsInFinalPosition(Cube cube) {
        
        ArrayList<Integer> finalPosition = getFinalPositionOfCube(cube);
        return cube.getXPos() == finalPosition.get(0) && cube.getYPos() == finalPosition.get(1) ? true : false;

    }


    public boolean cubeIsInFinalPosition(int cubeNumber) {
        Cube cube = getCube(cubeNumber);
        return cubeIsInFinalPosition(cube);
    }
    

    public ArrayList<Cube> getCubes() {
        ArrayList<Cube> cubes = new ArrayList<>();
        for(CubeLine cubeLine : cubeLines){
            for(Cube cube : cubeLine.getCubes()){
                cubes.add(cube);
            }
        }
        return cubes;
    }

    public ArrayList<Cube> getNonZeroCubes() {
        ArrayList<Cube> cubes = new ArrayList<>();
        for(CubeLine cubeLine : cubeLines){
            for(Cube cube : cubeLine.getCubes()){
                if(cube.getCubeNumber() == 0) continue;
                cubes.add(cube);
            }
        }
        return cubes;
    }

    public int getManhattanDistanceFromFinalPosition(Cube cube){
        ArrayList<Integer> finalPosition = getFinalPositionOfCube(cube);
        int xPos = cube.getXPos();
        int yPos = cube.getYPos();

        int xDistance = Math.abs(xPos - finalPosition.get(0));
        int yDistance = Math.abs(yPos - finalPosition.get(1));

        return xDistance + yDistance;

    }

    public boolean cubeIsBlocked(Cube cube) {
        //if cube is in final position it is not blocked
        return (cubeIsInFinalPosition(cube) || getCubesAbove(cube).size() != 0 || cube.getCubeNumber() == 0) ? false : true;
    }

    public int getNumOfBlockedCubes() {
        int numOfBlockedCubes = 0;
        for(Cube cube : getCubes()){
            if(cubeIsBlocked(cube)) numOfBlockedCubes++;
        }
        return numOfBlockedCubes;
    }



    public static void main(String[] args) {
        CubeMatrix cubeMatrix = new CubeMatrix(3);
        cubeMatrix.printCubeLinesWithInvisibleCubes();
        //print cordinates of free positions to move
        for(Cube freePosition : cubeMatrix.getFreePositionsToMoveTo()){
            System.out.println(freePosition.getXPos() + " " + freePosition.getYPos());
        }
        
    }


    public ArrayList<Cube> getCubesThatBlockFinalPosition(Cube cube) {
        ArrayList<Cube> result = new ArrayList<>();
        Cube cubeOnFinalPositon = getCube(getFinalPositionOfCube(cube).get(0), getFinalPositionOfCube(cube).get(1));
        result.add(cubeOnFinalPositon);
        ArrayList<Cube> cubesAbove = getCubesAbove(cube);
        for(Cube cubeAbove : cubesAbove){
            if(cubeAbove.getCubeNumber() == 0) continue;
            if(!cubeIsInFinalPosition(cubeAbove)) result.add(cubeAbove);
        }
        return result;
    }


    public int getNumOfCubesInFinalPosition() {
        int result = 0;
        for(Cube cube : getCubes()){
            if(cube.getCubeNumber() == 0) continue;
            if(cubeIsInFinalPosition(cube)) result++;
        }
        return result;
    }




}
