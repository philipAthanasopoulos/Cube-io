
public class CubeLine {
    private FixedSizeArrayList<Cube> cubes;

    public CubeLine(int numOfCubes) {
        this.cubes = new FixedSizeArrayList<Cube>(numOfCubes);
    }

    public CubeLine(int start , int end) {
        this.cubes = new FixedSizeArrayList<Cube>(end - start + 1);
        for(int i = start ; i <= end ; i++){
            this.cubes.add(new Cube(i));
        }
    }


    public boolean isInOrder() {
        return false;
    }

    public void printCubeLine() {
        //print top line
        for (Cube cube : cubes) {
            System.out.print("┌───┐");
        }
        System.out.println();
        //print middle parts
        for(Cube cube : cubes){
            System.out.print("│ " + cube.getCubeNumber() + " │");
        }
        System.out.println();
        //print bottom line
        for (Cube cube : cubes) {
            System.out.print("└───┘");
        }
        System.out.println();

    }

    public static void main(String[] args) {
        CubeLine cubeLine = new CubeLine(1,5);
        cubeLine.printCubeLine();
    }

    public FixedSizeArrayList<Cube> getCubes() {
        return this.cubes;
    }


    
    
}
