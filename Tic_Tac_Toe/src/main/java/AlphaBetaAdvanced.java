

/**
 * Uses the Alpha-Beta Pruning algorithm to play a move in a game of Tic Tac Toe
 * but includes depth in the evaluation function.
 *
 * The vanilla MiniMax algorithm plays perfectly but it may occasionally
 * decide to make a move that will results in a slower victory or a faster loss.
 * For example, playing the move 0, 1, and then 7 gives the AI the opportunity
 * to play a move at index 6. This would result in a victory on the diagonal.
 * But the AI does not choose this move, instead it chooses another one. It
 * still wins inevitably, but it chooses a longer route. By adding the depth
 * into the evaluation function, it allows the AI to pick the move that would
 * make it win as soon as possible.
 */
class AlphaBetaAdvanced {

    private static int maxPly = 5;

    /**
     * AlphaBetaAdvanced cannot be instantiated.
     */
    private AlphaBetaAdvanced() {}

    /**
     * Execute the algorithm.
     * @param player        the player that the AI will identify as
     * @param board         the Tic Tac Toe board to play on
     */
    static int run (Board.State player, Board board) {

        int returnNum = 0;
        if (maxPly < 1) {
            throw new IllegalArgumentException("Maximum depth must be greater than 0.");
        }
        
        int width = board.getBoardWidth();

        if(board.getAvailableMoves().size() == width * width) {
        	if(width % 2 == 1) {
                board.move(width * width / 2);
                board.setPreMove(width * width / 2);
            }
            else {
                board.move(width * width / 2 - width / 2 - 1);
                board.setPreMove(width * width / 2 - width / 2 - 1);
            }
        }
        else {
        	 returnNum = alphaBetaPruning(player, board, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
        }
        return returnNum;
    }

    /**
     * The meat of the algorithm.
     * @param player        the player that the AI will identify as
     * @param board         the Tic Tac Toe board to play on
     * @param alpha         the alpha value
     * @param beta          the beta value
     * @param currentPly    the current depth
     * @return              the score of the board
     */
    private static int alphaBetaPruning (Board.State player, Board board, double alpha, double beta, int currentPly) {
    	
        if (currentPly++ == maxPly || board.isGameOver()) {
        	return evaluate(player, board, currentPly);
        }
        
        if (board.getTurn() == Board.State.O) {
            return getMax(player, board, alpha, beta, currentPly);
        } else {
            return getMin(player, board, alpha, beta, currentPly);
        }
    }

    /**
     * Play the move with the highest score.
     * @param player        the player that the AI will identify as
     * @param board         the Tic Tac Toe board to play on
     * @param alpha         the alpha value
     * @param beta          the beta value
     * @param currentPly    the current depth
     * @return              the score of the board
     */
    private static int getMax (Board.State player, Board board, double alpha, double beta, int currentPly) {
        int indexOfBestMove = -1;
        
        for (Integer theMove : board.getAvailableMoves()) {
            Board modifiedBoard = board.getDeepCopy();
            
            if(modifiedBoard.isUseless(theMove)) continue;
            
            modifiedBoard.move(theMove);
            int score = alphaBetaPruning(player, modifiedBoard, alpha, beta, currentPly);
            
            if (score > alpha) {
                alpha = score;
                indexOfBestMove = theMove;
            }

            if (alpha >= beta) {
                break;
            }
        }
        
        if (indexOfBestMove != -1) {
            board.move(indexOfBestMove);
            if(currentPly == 1) {
            	System.out.println("Player O moves at (" + indexOfBestMove / board.getBoardWidth() + "," + indexOfBestMove % board.getBoardWidth() + "), alpha = " + alpha + ",beta = " + beta);
            	board.setPreMove(indexOfBestMove);
            }
        }
        
        //return (int)alpha;
        return indexOfBestMove;
    }

    /**
     * Play the move with the lowest score.
     * @param player        the player that the AI will identify as
     * @param board         the Tic Tac Toe board to play on
     * @param alpha         the alpha value
     * @param beta          the beta value
     * @param currentPly    the current depth
     * @return              the score of the board
     */
    private static int getMin (Board.State player, Board board, double alpha, double beta, int currentPly) {
        int indexOfBestMove = -1;
        
        for (Integer theMove : board.getAvailableMoves()) {

            Board modifiedBoard = board.getDeepCopy();

            if (modifiedBoard.isUseless(theMove)) continue;

            modifiedBoard.move(theMove);
            int score = alphaBetaPruning(player, modifiedBoard, alpha, beta, currentPly);

            //System.out.println("Player X moves at (" + theMove / board.getBoardWidth() + "," + theMove % board.getBoardWidth() + "), score = " + score);

            if (score < beta) {
                beta = score;
                indexOfBestMove = theMove;
            }

            if (alpha >= beta) {
                break;
            }
        }

        if (indexOfBestMove != -1) {
            board.move(indexOfBestMove);
            if(currentPly == 1) {
            	System.out.println("Player X moves at (" + indexOfBestMove / board.getBoardWidth() + "," + indexOfBestMove % board.getBoardWidth() + "), alpha = " + alpha + ",beta = " + beta);
            	board.setPreMove(indexOfBestMove);
            }
        }
        //return (int)beta;
        return indexOfBestMove;
    }

    /**
     * Get the score of the board. Takes depth into account.
     * @param player        the play that the AI will identify as
     * @param board         the Tic Tac Toe board to play on
     * @param currentPly    the current depth
     * @return              the score of the board
     */
    private static int evaluate (Board.State player, Board board, int currentPly) {

        if (player == Board.State.Blank) {
            throw new IllegalArgumentException("Player must be X or O.");
        }
        
        if (board.isGameOver() && board.getWinner() == Board.State.O) {
            return Integer.MAX_VALUE - currentPly;
        } else if (board.isGameOver() && board.getWinner() == Board.State.X) {
            return Integer.MIN_VALUE + currentPly;
        } else {
        	return board.getScoreO() - board.getScoreX();
        }
    }

}
