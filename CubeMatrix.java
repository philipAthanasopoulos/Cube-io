import java.util.Collection;
import java.util.Collections;

public class CubeMatrix {

    private FixedSizeArrayList<CubeLine> cubeLines;


    public CubeMatrix(int numOfCubesPerLine) {
        this.cubeLines = new FixedSizeArrayList<CubeLine>(3);
        int start = 1;
        int end = numOfCubesPerLine;
        for(int i = 0 ; i < 3 ; i++){
            CubeLine newLine = new CubeLine(start , end);
            cubeLines.add(newLine);
            start = end + 1;
            end = start + numOfCubesPerLine - 1;
        }
        Collections.shuffle(cubeLines);
        setPositionsForAllCubes();
    }
    

    public boolean isMoveable(Cube cube) {
        return getAboveCube(cube) == null ? true : false;
    }

    public boolean positionIsFree(int xNext, int yNext) {
        return getCube(xNext , yNext) == null ? true : false;
    }

    public void moveCube(Cube cube, int xNext, int yNext) {
        
    }

    public CubeLine[] getCubeLines() {
        return null;
    }

    public Cube getAboveCube(Cube cube) {
        return cubeLines.get(cube.getXPos() - 1).getCubes().get(cube.getYPos());
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
                if(cube == null) continue;
                cube.setXPos(cubeLines.indexOf(line));
                cube.setYPos(line.getCubes().indexOf(cube));
            }
        }
    }

    

    public void printCubeMatrix() {
        //TODO
        for(CubeLine cubeLine : cubeLines){
            cubeLine.printCubeLine();
        }
        System.out.println("================================================");

    }
    public static void main(String[] args) {
        CubeMatrix cubeMatrix = new CubeMatrix(3);
        cubeMatrix.printCubeMatrix();
        
        //move cube 1 to a free position
        cubeMatrix.moveCube(cubeMatrix.getCube(1) , 0 , 3);

        
    }
}
