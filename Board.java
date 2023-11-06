import java.lang.Math;
import java.util.Scanner;

//define enums that will track the board states and provide useful info for calculations and displays
enum HitState {
    BLANK('.'), MISS('*'), HIT('X'), SINK('#');
    private char icon;
    HitState(char icon) { this.icon = icon; }
    public char getIcon() { return icon; }
}
enum ShipType {
    NONE(0, '.', "None"),
    SHIP2(2, '2', "Patroller"),
    SHIP3A(3, '3', "Submarine"),
    SHIP3B(3, '3', "Destroyer"),
    SHIP4(4, '4', "Battleship"),
    SHIP5(5, '5', "Aircraft Carrier");
    private int size;
    private char icon;
    private String name;
    ShipType(int size, char icon, String name) { this.size = size; this.icon = icon; this.name = name; }
    public int getSize() { return size; }
    public char getIcon() { return icon; }
    public String getName() { return name; }
}

public class Board {
    Scanner scan = new Scanner(System.in);

    //define arrays
    HitState[][] hitArray;
    ShipType[][] shipArray;

    String displayname;

    Board(int xsize, int ysize, String displayname) {
        //create null arrays of xsize by ysize
        hitArray = new HitState[xsize][ysize];
        shipArray = new ShipType[xsize][ysize];

        this.displayname = displayname;

        //initialize hitArray to all blank
        for(int i=0; i<xsize; i++) {
            for(int j=0; j<ysize; j++) {
                hitArray[i][j] = HitState.BLANK;
            }
        }
        //initialize shipArray to all none
        for(int i=0; i<xsize; i++) {
            for(int j=0; j<ysize; j++) {
                shipArray[i][j] = ShipType.NONE;
            }
        }
    }

    //display an array as a board
    public void printBoard(boolean showShips) {
        String line;
        int spacenum = Math.max(Integer.toString(shipArray.length).length(), displayname.length());
        if(showShips) {
            line = displayname + " ".repeat(spacenum-displayname.length()) + " | ";
            for(int i=0; i<shipArray.length; i++) {
                line += (char)('A'+i) + " ";
            }
            System.out.println(line);
            line = "-".repeat(spacenum) + "-+-";
            for(int i=0; i<shipArray.length; i++) {
                line += "--";
            }
            System.out.println(line);
            line = "";
            for(int i=0; i<shipArray[0].length; i++) {
                line = Integer.toString(i+1) + " ".repeat(spacenum-Integer.toString(i+1).length()+1) + "| ";
                for(int j=0; j<shipArray.length; j++) {
                    if(hitArray[j][i]==HitState.BLANK) {
                        line += shipArray[j][i].getIcon() + " ";
                    } else {
                        line += hitArray[j][i].getIcon() + " ";
                    }
                }
                System.out.println(line);
                line = "";
            }
        } else {
            line = displayname + " ".repeat(spacenum-displayname.length()) + " | ";
            for(int i=0; i<hitArray.length; i++) {
                line += (char)('A'+i) + " ";
            }
            System.out.println(line);
            line = "-".repeat(spacenum) + "-+-";
            for(int i=0; i<hitArray.length; i++) {
                line += "--";
            }
            System.out.println(line);
            line = "";
            for(int i=0; i<hitArray[0].length; i++) {
                line = Integer.toString(i+1) + " ".repeat(spacenum-Integer.toString(i+1).length()+1) + "| ";
                for(int j=0; j<hitArray.length; j++) {
                    line += hitArray[j][i].getIcon() + " ";
                }
                System.out.println(line);
                line = "";
            }
        }
    }

    //place a ship
    public void placeShip(ShipType ship) {
        //initialize variables
        int x, y;
        int dir = -1;
        Integer[] coordArray;
        int size = ship.getSize();
        System.out.println(ship.getName() + " (" + Integer.toString(ship.getSize()) + " cells)");
        System.out.println("Input where one end of the ship will be: ");
        coordArray = getCoords();
        x = coordArray[0];  //get individual ints from array
        y = coordArray[1];
        System.out.println("Input the direction the ship faces (right=0, down=1, left=2, up=3):");
        try { dir = scan.nextInt(); }
        catch(final Exception e) {
            dir = -1;
            scan.nextLine();
        }
        while(!(dir == 0 || dir == 1 || dir == 2 || dir == 3)) {
            System.out.println("Direction must be 0, 1, 2, or 3!");
            try { dir = scan.nextInt(); }
            catch(final Exception e) {
                dir = -1;
                scan.nextLine();
            }
        }
        scan.nextLine();
        //make sure the ship can be placed, redo inputs if it can't
        while(!checkPlace(x,y,dir,size)) {
            System.out.println("The ship must be completely on the board and not overlapping other ships!");
            Game.delay(500);
            System.out.println("Input where one end of the ship will be: ");
            coordArray = getCoords();
            x = coordArray[0];
            y = coordArray[1];
            System.out.println("Input the direction the ship faces (right=0, down=1, left=2, up=3): ");
            try { dir = scan.nextInt(); }
            catch(final Exception e) {
                dir = -1;
                scan.nextLine();
            }
            while(!(dir == 0 || dir == 1 || dir == 2 || dir == 3)) {
                System.out.println("Direction must be 0, 1, 2, or 3!");
                try { dir = scan.nextInt(); }
                catch(final Exception e) {
                    dir = -1;
                    scan.nextLine();
                }
            }
            scan.nextLine();
        }
        switch(dir) {
            //0=right, 1=down, 2=left, 3=up
            case 0:
                for(int i=0; i<size; i++) {
                    shipArray[x+i][y] = ship;
                }
                break;
            case 1:
                for(int i=0; i<size; i++) {
                    shipArray[x][y+i] = ship;
                }
                break;
            case 2:
                for(int i=0; i<size; i++) {
                    shipArray[x-i][y] = ship;
                }
                break;
            case 3:
                for(int i=0; i<size; i++) {
                    shipArray[x][y-i] = ship;
                }
                break;
        }
    }

    public void placeAllShips() {
        for(ShipType ship : ShipType.values()) {
            if(ship!=ShipType.NONE) {
                Game.clearScreen();
                printBoard(true);
                Game.delay(500);
                placeShip(ship);
            }
        }
        Game.clearScreen();
        printBoard(true);
        Game.delay(1000);
    }

    //Character.isDigit(char), Character.isLetter(char)
    //get coords from a string, output to int array. ex.: "B4" --> [1,3]
    public Integer[] getCoords() {
        String inputCoords = "";
        char xname;
        int xpos, ypos;
        boolean validFormat = false;
        boolean onBoard = false;
        inputCoords = scan.nextLine();
        if(inputCoords.length() < 2){
            validFormat = false;
        } else if(!Character.isLetter(inputCoords.charAt(0)) || !Character.isDigit(inputCoords.charAt(1))) {
            validFormat = false;
        } else {
            validFormat = true;
            for(int i=0; i < inputCoords.substring(1).length(); i++) {
                if(!Character.isDigit(inputCoords.substring(1).charAt(i))) {
                    validFormat = false;
                }
            }
            if(validFormat) {
                xname = Character.toLowerCase(inputCoords.charAt(0));
                xpos = xname-'a';
                ypos = Integer.parseInt(inputCoords.substring(1))-1;
                if(xpos < 0 || ypos < 0 || xpos >= hitArray.length || ypos >= hitArray[0].length) {
                    onBoard = false;
                } else {
                    onBoard = true;
                }
            }
        }
        while(!validFormat || !onBoard) {
            System.out.println("Please enter a coordinate that is on the board and in letter, number format. Ex.: A1");
            inputCoords = scan.nextLine();
            if(inputCoords.length() < 2){
                validFormat = false;
            } else if(!Character.isLetter(inputCoords.charAt(0)) || !Character.isDigit(inputCoords.charAt(1))) {
                validFormat = false;
            } else {
                validFormat = true;
                for(int i=0; i < inputCoords.substring(1).length(); i++) {
                    if(!Character.isDigit(inputCoords.substring(1).charAt(i))) {
                        validFormat = false;
                    }
                }
                if(validFormat) {
                    xname = Character.toLowerCase(inputCoords.charAt(0));
                    xpos = xname-'a';
                    ypos = Integer.parseInt(inputCoords.substring(1))-1;
                    if(xpos < 0 || ypos < 0 || xpos >= hitArray.length || ypos >= hitArray[0].length) {
                        onBoard = false;
                    } else {
                        onBoard = true;
                    }
                }
            }
        }
        xname = Character.toLowerCase(inputCoords.charAt(0));
        xpos = xname-'a';
        ypos = Integer.parseInt(inputCoords.substring(1))-1;
        return new Integer[]{xpos,ypos};
    }

    //check if a ship can be placed somewhere
    public boolean checkPlace(int x, int y, int dir, int dist) {
        switch(dir) {
            //0=right, 1=down, 2=left, 3=up
            case 0:
                if(x+dist-1>=shipArray.length) { return false; }
                for(int i=0; i<dist; i++) {
                    if(shipArray[x+i][y] != ShipType.NONE) { return false; }
                }
                break;
            case 1:
                if(y+dist-1>=shipArray[0].length) { return false; }
                for(int i=0; i<dist; i++) {
                    if(shipArray[x][y+i] != ShipType.NONE) { return false; }
                }
                break;
            case 2:
                if(x<dist-1) { return false; }
                for(int i=0; i<dist; i++) {
                    if(shipArray[x-i][y] != ShipType.NONE) { return false; }
                }
                break;
            case 3:
                if(y<dist-1) { return false; }
                for(int i=0; i<dist; i++) {
                    if(shipArray[x][y-i] != ShipType.NONE) { return false; }
                }
                break;
        }
        return true;
    }

    //get the HitState of a specific point
    public HitState getHitState(int x, int y) {
        return hitArray[x][y];
    }

    //get the ShipType at a specific point
    public ShipType getShipType(int x, int y) {
        return shipArray[x][y];
    }

    public void setHitState(int x, int y, HitState state) {
        hitArray[x][y] = state;
    }

    public void setShipType(int x, int y, ShipType ship) {
        shipArray[x][y] = ship;
    }

    public HitState checkHit(int x, int y) {
        if(hitArray[x][y] == HitState.BLANK) {
            if(shipArray[x][y] == ShipType.NONE) {
                return HitState.MISS;
            } else {
                return HitState.HIT;
            }
        } else {
            return HitState.BLANK;
        }
    }

    public void doHit() {
        System.out.println("Input space to fire at: ");
        Integer[] coordArray = getCoords();
        int x = coordArray[0];
        int y = coordArray[1];
        HitState hit = checkHit(x, y);
        while(hit == HitState.BLANK) {
            System.out.println("You've already fired there!");
            Game.delay(500);
            System.out.println("Input space to fire at: ");
            coordArray = getCoords();
            x = coordArray[0];
            y = coordArray[1];
            hit = checkHit(x, y);
        }
        switch(hit) {
            case BLANK:
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAA");  //this shouldn't happen
                break;
            case MISS:
                hitArray[x][y] = HitState.MISS;
                Game.delay(1000);
                System.out.println("You missed.");
                break;
            case HIT:
                hitArray[x][y] = HitState.HIT;
                Game.delay(1000);
                System.out.println("You hit something!");
                break;
            case SINK:
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAA");  //this shouldn't happen
        }
        checkLose();
        if(hitArray[x][y]==HitState.SINK) {
            Game.delay(1000);
            System.out.println("You sunk your opponent's " + shipArray[x][y].getName() + "!");
        }
        Game.delay(500);
        printBoard(false);
        Game.delay(1000);
    }

    //check if a ship has been sunken yet
    public boolean checkSink(ShipType ship) {
        for(int i=0; i<shipArray.length; i++) {
            for(int j=0; j<shipArray[0].length; j++) {
                if(shipArray[i][j]==ship&&!(hitArray[i][j]==HitState.HIT||hitArray[i][j]==HitState.SINK)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void sinkShip(ShipType ship) {
        for(int i=0; i<shipArray.length; i++) {
            for(int j=0; j<shipArray[0].length; j++) {
                if(shipArray[i][j]==ship) {
                    hitArray[i][j] = HitState.SINK;
                }
            }
        }
    }

    public boolean checkLose() {
        boolean lost = true;
        for(ShipType ship : ShipType.values()) {
            if(ship!=ShipType.NONE && checkSink(ship)) {
                sinkShip(ship);
            }
            if(ship!=ShipType.NONE && !checkSink(ship)) {
                lost = false;
            }
        }
        return lost;
    }
}