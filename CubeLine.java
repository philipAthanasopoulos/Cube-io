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

        final String ANSI_YELLOW = "\u001B[33m";
        final String ANSI_RESET = "\u001B[0m";

        

        //print top line
        for (Cube cube : cubes) {
            System.out.print("┌───┐");
        }
        System.out.println();
        //print middle parts
        for(Cube cube : cubes){
             System.out.print("│ " + ANSI_YELLOW +  cube.getCubeNumber() + ANSI_RESET + " │");
        }
        System.out.println();



        //print bottom line
        for (Cube cube : cubes) {
             System.out.print("└───┘");
        }
        System.out.println();

    }


    public void printCubeLine(){
        //ansi yellow color
        final String ANSI_YELLOW = "\u001B[33m";
        final String ANSI_RESET = "\u001B[0m";
        // ansi bold code
        final String ANSI_BOLD = "\u001B[3m";
        
    

        //print top line
        for (Cube cube : cubes) {
            if(cube.getCubeNumber() == 0) System.out.print("     ");
            else System.out.print("┌───┐");
        }
        System.out.println();

        //print middle parts
        for(Cube cube : cubes){
             if(cube.getCubeNumber() == 0) System.out.print("     ");

             else System.out.print("│ " + ANSI_YELLOW + ANSI_BOLD + cube.getCubeNumber() + ANSI_RESET + " │");
        }
        System.out.println();



        //print bottom line
        for (Cube cube : cubes) {
            if(cube.getCubeNumber() == 0) System.out.print("     ");

             else System.out.print("└───┘");
        }
        System.out.println();

    }

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
        cubeLine.printCubeLine();
        //print cubeline size
        System.out.println(cubeLine.getCubes().size());

    }



    public CubeLine copy() {
        CubeLine copy = new CubeLine();
        copy.cubes = new ArrayList<Cube>();
        for(Cube cube : this.cubes){
            copy.cubes.add(cube.copy());
        }
        return copy;
    }


 
}
