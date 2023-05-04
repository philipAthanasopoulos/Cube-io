import java.util.ArrayList;
import java.util.Collections;

public class CubeLine {
    
    private ArrayList<Cube> cubes;

    public CubeLine(int numOfCubes) {
        this.cubes = new ArrayList<Cube>();
    }

    public CubeLine(int start  , int numOfCubesPerLine , int size) {


        this.cubes = new ArrayList<Cube>();

        for(int i = 0 ; i < numOfCubesPerLine ; i++){
            this.cubes.add(new Cube(i + start));
        }

        //shuffle the Cubes
        Collections.shuffle(this.cubes);

        //initialize the rest as invisible cubes
        for(int i = numOfCubesPerLine ; i < size ; i++){
            this.cubes.add(new Cube(0));
        }
    }


    public CubeLine() {
    }

    public boolean isInOrder() {
        int lastCubeNumber = 0;
        for(Cube cube : cubes){
            if(cube.getCubeNumber() == 0) continue;
            if(cube.getCubeNumber() < lastCubeNumber) return false;
            lastCubeNumber = cube.getCubeNumber();
        }
        return true;
    }

   


    public void printCubeLineWithInvisibleCubes() {

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


    public void printCubeLine(){
        //print top line
        for (Cube cube : cubes) {
            if(cube.getCubeNumber() == 0) System.out.print("     ");
            else System.out.print("┌───┐");
        }
        System.out.println();
        //print middle parts
        for(Cube cube : cubes){
             if(cube.getCubeNumber() == 0) System.out.print("     ");

             else System.out.print("│ " + cube.getCubeNumber() + " │");
        }
        System.out.println();



        //print bottom line
        for (Cube cube : cubes) {
            if(cube.getCubeNumber() == 0) System.out.print("     ");

             else System.out.print("└───┘");
        }
        System.out.println();

    }


    // public void printCubeLine() {
    //     //print top line
    //     for (Cube cube : cubes) {
    //         if (cube.getCubeNumber() == 0) {
    //             System.out.print("          ");
    //         } else {
    //             System.out.print("┌────────┐");
    //         }
    //     }
    //     System.out.println();
    
    //     //print middle parts
    //     for (Cube cube : cubes) {
    //         if (cube.getCubeNumber() == 0) {
    //             System.out.print("    ");
    //         } else {
    //             int cubeNumber = cube.getCubeNumber();
    //             if (cubeNumber < 10) {
    //                 System.out.print("│   " + cubeNumber + "    │");
    //             } else {
    //                 System.out.print("│   " + cubeNumber + "   │");
    //             }
    //         }
    //     }
    //     System.out.println();
    
    //     //print bottom line
    //     for (Cube cube : cubes) {
    //         if (cube.getCubeNumber() == 0) {
    //             System.out.print("          ");
    //         } else {
    //             System.out.print("└────────┘");
    //         }
    //     }
    //     System.out.println();
    // }
    
    

    public void moveCube(int i, int j) {
        Cube temp = this.cubes.get(i);
        this.cubes.set(i, this.cubes.get(j));
        this.cubes.set(j, temp);
    }
    
    public ArrayList<Cube> getCubes() {
        return this.cubes;
    }
    public static void main(String[] args) {
        CubeLine cubeLine = new CubeLine(1,3 ,5);
        cubeLine.printCubeLineWithInvisibleCubes();
        //print cubeline size
        System.out.println(cubeLine.getCubes().size());

    }


 
}
