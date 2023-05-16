public class Table{
    //asnsi reset color code
    static  final String ANSI_RESET = "\u001B[0m";
    //ansi green color code
    static final String ANSI_GREEN = "\u001B[32m";


    public Table(){
        
    }

    

    public void printStartScreen() {
        
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

        Botaki20 botaki = new Botaki20();
        botaki.setCubeMatrix(cubeManager.getCubeMatrix());
        Node root = new Node(cubeManager.getCubeMatrix());
        botaki.AStar(root);
        // root = new Node(cubeManager.getCubeMatrix());
        // botaki.AStar(root);
        
        
    }

}