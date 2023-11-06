import java.util.concurrent.TimeUnit;

public class Game{
    static Board board1 = new Board(10, 10, "P1");
    static Board board2 = new Board(10, 10, "P2");

    public static void main(String[] args) {
        boolean player1Turn = true;
        boolean gameEnd = false;
        board1.placeAllShips();
        board2.placeAllShips();
        while(!gameEnd) {
            gameEnd = doTurn(player1Turn);
            if(!gameEnd) {
                player1Turn = !player1Turn;
            }
        }
        clearScreen();
        if(player1Turn) {
            System.out.println("Player 1 won!");
        } else {
            System.out.println("Player 2 won!");
        }
        board1.printBoard(true);
        System.out.println("");
        board2.printBoard(true);
    }

    public static boolean doTurn(boolean isPlayer1) {
        boolean gameEnd = false;
        Board ownBoard;
        Board oppBoard;
        clearScreen();
        if(isPlayer1) {
            ownBoard = board1;
            oppBoard = board2;
            System.out.println("PLAYER 1 TURN:");
        } else {
            ownBoard = board2;
            oppBoard = board1;
            System.out.println("PLAYER 2 TURN:");
        }
        System.out.println("Opponent's board:");
        oppBoard.printBoard(false);
        System.out.println("Your board:");
        ownBoard.printBoard(true);
        oppBoard.doHit();
        gameEnd = oppBoard.checkLose();
        if(!gameEnd) {
            System.out.println("Switching to other player's board in 3...");
            delay(1000);
            System.out.println("                                     2...");
            delay(1000);
            System.out.println("                                     1...");
            delay(1000);
        }
        return gameEnd;
    }

    public static void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (final Exception e) {}
    }

    public static void delay(long delayms) {
        long speedFactor = 1;   //shorten delays for debugging quickly
        try {
            TimeUnit.MILLISECONDS.sleep(delayms/speedFactor);
        } catch (InterruptedException e) {}
    }
}