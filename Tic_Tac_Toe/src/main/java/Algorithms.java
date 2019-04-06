
/**
 * Uses algorithms to play Tic Tac Toe.
 */
public class Algorithms {

    /**
     * Algorithms cannot be instantiated.
     */
    private Algorithms() {}

    /**
     * Play using the Alpha-Beta Pruning algorithm. Include depth in the
     * evaluation function and a depth limit.
     * @param board     the Tic Tac Toe board to play on
     */
    public static int alphaBetaAdvanced (Board.State player, Board board) {
        return AlphaBetaAdvanced.run(player, board);
    }

}
