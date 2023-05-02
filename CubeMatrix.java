
public class CubeMatrix {

    private CubeLine[] cubeLines;


    public CubeMatrix(int numOfCubesPerLine) {
        this.cubeLines = new CubeLine[3];
        for (int i = 0; i < cubeLines.length; i++) {
            cubeLines[i] = new CubeLine(numOfCubesPerLine);
        }
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
