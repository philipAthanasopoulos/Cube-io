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
            Collections.shuffle(newLine.getCubes());
            cubeLines.add(newLine);
            start = end + 1;
            end = start + numOfCubesPerLine - 1;
        }
        Collections.shuffle(cubeLines);
    }
    

    public boolean isMoveable(Cube cube, int xNext, int yNext) {
        return false;
    }

    public void moveCube(Cube cube, int xNext, int yNext) {
    }

    public CubeLine[] getCubeLines() {
        return null;
    }

    public void printCubeMatrix() {
        //TODO
        for(CubeLine cubeLine : cubeLines){
            cubeLine.printCubeLine();
        }

    }


    public static void main(String[] args) {
        CubeMatrix cubeMatrix = new CubeMatrix(3);
        cubeMatrix.printCubeMatrix();
    }
}
