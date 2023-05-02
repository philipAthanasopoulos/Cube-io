
public class CubeLine {
    private Cube[] cubes;

    public CubeLine(int numOfCubes) {
        this.cubes = new Cube[numOfCubes];
        for (int i = 0; i < cubes.length; i++) {
            cubes[i] = new Cube(i + 1);
        }
    }


    public boolean isInOrder() {
        return false;
    }

    public void printCubeLine() {
        //print top line
        for (int i = 0; i < cubes.length; i++) {
            System.out.print("┌───┐");
        }
        System.out.println();
        //print middle parts
        for(int i = 0 ; i < cubes.length ; i++){
            System.out.print("│ " + cubes[i].getCubeNumber() + " │");
        }
        System.out.println();
        //print bottom line
        for (int i = 0; i < cubes.length; i++) {
            System.out.print("└───┘");
        }
        System.out.println();

    }

    public static void main(String[] args) {
        CubeLine cubeLine = new CubeLine(5);
        cubeLine.printCubeLine();
    }


    
    
}
