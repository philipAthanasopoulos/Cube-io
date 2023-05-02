public class Table{
    //constructor
    public Table(){
        //TODO
    }

    //methods

    public void printStartScreen() {
        //TODO
        //asnsi reset color code
        final String ANSI_RESET = "\u001B[0m";
        //ansi green color code
        final String ANSI_GREEN = "\u001B[32m";
        System.out.println(ANSI_GREEN);

        System.out.println("      ___           ___                         ___                       ___     ");
        System.out.println("     /  /\\         /__/\\         _____         /  /\\        ___          /  /\\    ");
        System.out.println("    /  /:/         \\  \\:\\       /  /::\\       /  /:/_      /  /\\        /  /::\\   ");
        System.out.println("   /  /:/           \\  \\:\\     /  /:/\\:\\     /  /:/ /\\    /  /:/       /  /:/\\:\\  ");
        System.out.println("  /  /:/  ___   ___  \\  \\:\\   /  /:/~/::\\   /  /:/ /:/_  /__/::\\      /  /:/  \\:\\");
        System.out.println(" /__/:/  /  /\\ /__/\\  \\__\\:\\ /__/:/ /:/\\:| /__/:/ /:/ /\\ \\__\\/\\:\\__  /__/:/ \\__\\:\\");
        System.out.println(" \\  \\:\\ /  /:/ \\  \\:\\ /  /:/ \\  \\:\\/:/~/:/ \\  \\:\\/:/ /:/    \\  \\:\\/\\ \\  \\:\\ /  /:/");
        System.out.println("  \\  \\:\\  /:/   \\  \\:\\  /:/   \\  \\::/ /:/   \\  \\::/ /:/      \\__\\::/  \\  \\:\\  /:/ ");
        System.out.println("   \\  \\:\\/:/     \\  \\:\\/:/     \\  \\:\\/:/     \\  \\:\\/:/       /__/:/    \\  \\:\\/:/  ");
        System.out.println("    \\  \\::/       \\  \\::/       \\  \\::/       \\  \\::/        \\__\\/      \\  \\::/   ");
        System.out.println("     \\__\\/         \\__\\/         \\__\\/         \\__\\/                     \\__\\/    ");

        System.out.println(ANSI_RESET);
         
    }


    public static void main(String[] args) {
        
        Table table = new Table();
        table.printStartScreen();

        CubeManager cubeManager = new CubeManager();
        cubeManager.requestCubeMatrix();
        cubeManager.printCubeMatrix();
        //move cube 1 to a free position
        cubeManager.moveCube(cubeManager.getCube(1), 5, 2);
        cubeManager.printCubeMatrix();

        
    }

}