import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class CubeMatrix {

    private ArrayList<CubeLine> cubeLines;


    public CubeMatrix(int numOfCubesPerLine) {
        this.cubeLines = new ArrayList<CubeLine>();
        int start = 1;
        //create 3 CubeLines from 1 to 3*numOfCubesPerLine
        for(int i = 0 ; i < 3 ; i++){
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

    public void moveCube(Cube cube, int xNext, int yNext) {
        Cube targetCube = getCube(xNext , yNext);
        targetCube.setCubeNumber(cube.getCubeNumber());
        cube.setCubeNumber(0);
    }

    public ArrayList<CubeLine> getCubeLines() {
        return this.cubeLines;
    }

    public Cube getAboveCube(Cube cube) {
        if(cube.getYPos() == 0) return new Cube(0);
        return cubeLines.get(cube.getYPos() -1).getCubes().get(cube.getXPos());
    }

    public Cube getCube(int xPos, int yPos) {
        return cubeLines.get(yPos).getCubes().get(xPos);
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

    

    public void printCubeMatrix() {
        //TODO
        for(CubeLine cubeLine : cubeLines){
            cubeLine.printCubeLine();
        }
        System.out.println("================================================");

    }
    public static void main(String[] args) {
        CubeMatrix cubeMatrix = new CubeMatrix(2);
        cubeMatrix.printCubeMatrix();
        System.out.println(cubeMatrix.isInOrder());
         
    }


    public CubeLine getCubeLine(int yNext) {
        return cubeLines.get(yNext);   
    }
}
