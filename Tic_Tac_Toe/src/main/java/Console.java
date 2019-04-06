import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * For playing Tic Tac Toe in the console.
 */
public class Console {

    private Board board;
    private Scanner sc = new Scanner(System.in);

    /**
     * Construct Console.
     */
    private Console() {
        board = new Board();
    }

    /**
     * Begin the game.
     */
    private void play () {

        System.out.println("Starting a new game.");

        while (true) {
            printGameStatus();
            //playMove();

            if (board.isGameOver()) {
                printWinner();

                if (!tryAgain()) {
                    break;
                }
            }
        }
    }

    /**
     * Handle the move to be played, either by the player or the AI.
     */
//    private void playMove () {
//    	
//        if (board.getTurn() == Board.State.X) {
//        	Algorithms.alphaBetaAdvanced(Board.State.X, board);
//
//        } else {
//        	getPlayerMove();
//        }
//    }

    /**
     * Print out the board and the player who's turn it is.
     */
    private void printGameStatus () {
        System.out.println("\n" + board + "\n");
        System.out.println(board.getTurn().name() + "'s turn.");
    }

    /**
     * For reading in and interpreting the move that the user types into the console.
     */
    private void getPlayerMove (int move_row, int move_col) {
//        System.out.print("Row of the move: ");
//        int move_row = sc.nextInt();
//
//        System.out.print("Col of the move: ");
//        int move_col = sc.nextInt();
        int move = move_row * Board.BOARD_WIDTH + move_col;
        

        if (move < 0 || move >= Board.BOARD_WIDTH* Board.BOARD_WIDTH) {
            System.out.println("\nInvalid move.");
            System.out.println("\nThe index of the move must be between 0 and "
                    + (Board.BOARD_WIDTH * Board.BOARD_WIDTH - 1) + ", inclusive.");
        } else if (!board.move(move)) {
            System.out.println("\nInvalid move.");
            System.out.println("\nThe selected index must be blank.");
        }
    }

    /**
     * Print out the winner of the game.
     */
    private void printWinner () {
        Board.State winner = board.getWinner();

        System.out.println("\n" + board + "\n");

        if (winner == Board.State.Blank) {
            System.out.println("The TicTacToe is a Draw.");
        } else {
            System.out.println("Player " + winner.toString() + " wins!");
        }
    }

    /**
     * Reset the game if the player wants to play again.
     * @return      true if the player wants to play again
     */
    private boolean tryAgain () {
        if (promptTryAgain()) {
            board.reset();
            System.out.println("Started new game.");
            System.out.println("X's turn.");
            return true;
        }

        return false;
    }

    /**
     * Ask the player if they want to play again.
     * @return      true if the player wants to play again
     */
    private boolean promptTryAgain () {
        while (true) {
            System.out.print("Would you like to start a new game? (Y/N): ");
            String response = sc.next();
            if (response.equalsIgnoreCase("y")) {
                return true;
            } else if (response.equalsIgnoreCase("n")) {
                return false;
            }
            System.out.println("Invalid input.");
        }
    }
	
	public List<Integer> httprequest (int x1, int y1, HttpRequest1 httpRequest1){
        int x2 = 0;
        int y2 = 0;
		Point point = new Point();
		point.setLocation(x1,y1);
        httpRequest1.make_move(point);
        while(true) {
            String result = httpRequest1.get_moves(6);
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Jsontrans jsontrans = objectMapper.readValue(result, Jsontrans.class);
                List<Move> list = jsontrans.getMoves();
                System.out.println("waiting for other player");
                if (!(list.get(0).getTeamId().equals("1133")) ) {
                    System.out.println("results:" + result);
                    x2 = Integer.parseInt(list.get(0).getMoveX());
                    y2 = Integer.parseInt(list.get(0).getMoveY());
                    List<Integer> listx = new ArrayList();
                    listx.add(x2);
                    listx.add(y2);
                    return listx;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

    public static void main(String[] args) {
        Console ticTacToe = new Console();
        int x2 = 0;
        int y2 = 0;
        int returnNum = 0;
        int x1;
        int y1;
        HttpRequest1 httpRequest1 = new HttpRequest1();
        httpRequest1.create_game("1170",15,5);// if you want to change the boardsize and target, you should also change the parameters BOARD_WIDTH and M in the Board.java document
        System.out.println("Starting a new game.");
        while(true){
            ticTacToe.printGameStatus();
            if (ticTacToe.board.getTurn() == Board.State.X) {
                returnNum =  Algorithms.alphaBetaAdvanced(Board.State.X, ticTacToe.board);
                int returnX = returnNum / ticTacToe.board.getBoardWidth();
                int returnY = returnNum % ticTacToe.board.getBoardWidth();
                System.out.println("Return X is" + returnX + ", return Y is" + returnY);
                x1 = returnX;
                y1 = returnY;
                List<Integer> listx = ticTacToe.httprequest(x1,y1,httpRequest1);
                x2 = listx.get(0);
                y2 = listx.get(1);
            } else {
                ticTacToe.getPlayerMove(x2, y2);
            }
            if (ticTacToe.board.isGameOver()) {
                ticTacToe.printWinner();

                if (!ticTacToe.tryAgain()) {
                    break;
                }
            }
        }
    }

}