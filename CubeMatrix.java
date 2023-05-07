import java.util.ArrayList;
import java.util.Collections;

public class CubeMatrix {

    private ArrayList<CubeLine> cubeLines;
    private int numOfCubesPerLine;


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
        return positionIsFree(cube) && !positionBelowIsFree(cube) ? true : false;
        
    }

    public Cube findFreePosition(){
        for(Cube cube : getCubeLines().get(2).getCubes()){
            if(cube.getCubeNumber() == 0) return cube;
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
        int temp = cube.getCubeNumber();
        targetCube.setCubeNumber(temp);
        cube.setCubeNumber(0);
        setPositionsForAllCubes();
    }

    public ArrayList<CubeLine> getCubeLines() {
        return this.cubeLines;
    }

    public Cube getAboveCube(Cube cube) {
        if(cube.getYPos() == 0) return new Cube(-1);
        try {
            return cubeLines.get(cube.getYPos() - 1).getCubes().get(cube.getXPos());
        } catch (Exception e) {
            return new Cube(-1);
        }
    }


    public Cube getBelowCube(Cube cube) {
        if(cube.getYPos() == 2) return new Cube(-1);
        try {
            return cubeLines.get(cube.getYPos() + 1).getCubes().get(cube.getXPos());
        } catch (Exception e) {
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

        //put all cubes in a line form buttom to top , check if line is in order
        CubeLine lineToCheck = new CubeLine(0);

        for(int i = cubeLines.size() - 1 ; i >= 0 ; i--){
            CubeLine line = cubeLines.get(i);
            for(Cube cube : line.getCubes()){
                if(cube.getCubeNumber() == 0) continue;
                lineToCheck.getCubes().add(cube);
            }
        }

        return lineToCheck.isInOrder();
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

    

    public void printCubeMatrix() {
        //TODO
        for(CubeLine cubeLine : cubeLines){
            cubeLine.printCubeLine();
        }
        System.out.println("================================================");

    }



    public CubeMatrix copy() {
        CubeMatrix copy = new CubeMatrix(0);
        for(CubeLine cubeLine : cubeLines){
            copy.getCubeLines().add(cubeLine.copy());
        }
        return copy;
    }
    public boolean cubeIsInFinalPosition(Cube cube) {
        int finalPosition = (cube.getYPos())*numOfCubesPerLine + cube.getXPos() + 1;
        return cube.getCubeNumber() == finalPosition ? true : false;
    }


    public static void main(String[] args) {
        CubeMatrix cubeMatrix = new CubeMatrix(1);
        cubeMatrix.printCubeLinesWithInvisibleCubes();

        cubeMatrix.getCube(1,2).printCube();
        

        
    }


}
