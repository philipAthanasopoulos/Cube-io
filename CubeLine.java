import java.util.Collections;

public class CubeLine {
    private FixedSizeArrayList<Cube> cubes;

    public CubeLine(int numOfCubes) {
        this.cubes = new FixedSizeArrayList<Cube>(numOfCubes);
    }

    public CubeLine(int start  , int numOfCubesPerLine) {
        this.cubes = new FixedSizeArrayList<Cube>(4*numOfCubesPerLine);
        for(int i = start ; i <= numOfCubesPerLine ; i++){
            this.cubes.add(new Cube(i));
        }

        //shuffle the Cubes
        Collections.shuffle(this.cubes);

        //initialize the rst as null
        for(int i = numOfCubesPerLine + 1 ; i <= 4*numOfCubesPerLine ; i++){
            this.cubes.add(null);
        }
    }


    public boolean isInOrder() {
        return false;
    }

    public void printCubeLine() {
        


        //print top line
        for (Cube cube : cubes) {
            if(cube == null) continue;
            System.out.print("┌───┐"); 
        }
        System.out.println();



        //print middle parts
        for(Cube cube : cubes){
            if(cube == null) continue;
            System.out.print("│ " + cube.getCubeNumber() + " │");
        }
        System.out.println();


        
        //print bottom line
        for (Cube cube : cubes) {
            if(cube == null) continue;
            System.out.print("└───┘");
        }
        System.out.println();

    }
    
    public FixedSizeArrayList<Cube> getCubes() {
        return this.cubes;
    }
    public static void main(String[] args) {
        CubeLine cubeLine = new CubeLine(1,3);
        cubeLine.printCubeLine();

        //move cube 1 to index 5
        cubeLine.getCubes().move(0, 4);
        
        

    }



    
    
}
