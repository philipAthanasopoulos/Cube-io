import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class CubeMatrix {

    private ArrayList<CubeLine> cubeLines;


    public CubeMatrix(int numOfCubesPerLine) {
        this.cubeLines = new ArrayList<CubeLine>();
        int start = 1;
        //create 3 CubeLines from 1 to 3*numOfCubesPerLine
        for(int i = 0 ; i < numOfCubesPerLine ; i++){
            cubeLines.add(new CubeLine(start , numOfCubesPerLine));
            start += numOfCubesPerLine;
        }
        Collections.shuffle(cubeLines);
        setPositionsForAllCubes();
    }
    

    public boolean isMoveable(Cube cube) {
        return getAboveCube(cube).getCubeNumber() == 0 ? true : false;
    }

    public boolean positionIsFree(int xNext, int yNext) {
        return getCube(xNext , yNext).getCubeNumber() == 0 ? true : false;
    }

    // public void moveCube(Cube cube, int xNext, int yNext) {
        
    // }

    public ArrayList<CubeLine> getCubeLines() {
        return this.cubeLines;
    }

    public Cube getAboveCube(Cube cube) {
        if(cube.getYPos() == 0) return new Cube(0);
        return cubeLines.get(cube.getYPos() -1).getCubes().get(cube.getXPos());
    }

    public Cube getCube(int xPos, int yPos) {
        return cubeLines.get(xPos).getCubes().get(yPos);
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
                if(cube.getCubeNumber() == 0) continue;
                cube.setXPos(cubeLines.indexOf(line));
                cube.setYPos(line.getCubes().indexOf(cube));
            }
        }
    }

    

    public void printCubeMatrix() {
        //TODO
        for(CubeLine cubeLine : cubeLines){
            System.out.println("Line " + cubeLines.indexOf(cubeLine) + ":");
            cubeLine.printCubeLineWithInvisibleCubes();
        }
        System.out.println("================================================");

    }
    public static void main(String[] args) {
        CubeMatrix cubeMatrix = new CubeMatrix(3);
        cubeMatrix.printCubeMatrix();
        
        

        
    }


    public CubeLine getCubeLine(int yNext) {
        return cubeLines.get(yNext);   
    }
}
