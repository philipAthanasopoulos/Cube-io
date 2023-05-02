public class Cube {
    private int cubeNumber;

    public Cube(int cubeNumber) {
        this.cubeNumber = cubeNumber;
    }

    public int getCubeNumber() {
        return this.cubeNumber;
    }

    public void setCubeNumber(int cubeNumber) {
        this.cubeNumber = cubeNumber;
    }

    //print ascii square with number 3
    public void printCube() {
        System.out.println("┌───┐");
        System.out.println("│ " + this.cubeNumber + " │");
        System.out.println("└───┘");
    }


    
}
