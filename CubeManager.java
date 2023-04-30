public class CubeManager {
    private CubeMatrix cubeMatrix;

    public CubeManager(CubeMatrix cubeMatrix) {
        this.cubeMatrix = cubeMatrix;
    }
    
    public void createCubeMaxtrix(int numOfCubesPerLine) {
        cubeMatrix = new CubeMatrix(numOfCubesPerLine);
        



    }
}
