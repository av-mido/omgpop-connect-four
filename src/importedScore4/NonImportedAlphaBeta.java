package importedScore4;

import importedScore4.Score4.*;
import importedScore4.Score4.Mycell;
import importedScore4.Score4.ScoreMoveTuple;

public class NonImportedAlphaBeta {
	
	//note: amIOrange refers to whether the player is orange, not whether this max function needs to min or max...
	public static ScoreMoveTuple maxValue(Board board, int alpha, int beta, int depth, boolean amIOrange) {
		if( 0 == depth) {
			ScoreMoveTuple smt = new ScoreMoveTuple();
			int rawScore = Score4.scoreBoard(board);
			smt.score = amIOrange ? rawScore : -rawScore;
			smt.move = -1;
			return smt;
		} else {
			int v_bestScore = -10000099;
			int bestMove = -1;
			for(int column = 0; column < 7; column++) {
				if(board.slots[0][column] != Mycell.Barren) {
					continue;
				}
				int rowFilled = board.dropDisk(column, amIOrange ? Mycell.Orange : Mycell.Yellow);
				if(rowFilled == -1)
					continue;
				int raw_s = Score4.scoreBoard(board);
				int s = amIOrange ? raw_s : -raw_s;
				//check if win before proceeding further down search
				if(s == Score4.orangeWins) {
					bestMove = column;
					v_bestScore = s;
					board.slots[rowFilled][column] = Mycell.Barren;
					break;
				}
				Integer moveInner, scoreInner;
				ScoreMoveTuple inner = minValue(board, alpha, beta, depth - 1, amIOrange);
				assert(inner != null);
				moveInner = inner.move;
				scoreInner = inner.score; //minValue already returned a score with the correct sign based on amIOrange argument.
				board.slots[rowFilled][column] = Mycell.Barren;
				if(scoreInner >= v_bestScore) {
					v_bestScore = scoreInner;
					bestMove = column;
				}
				if(v_bestScore > beta) {
					ScoreMoveTuple smt = new ScoreMoveTuple();
					smt.score = v_bestScore;
					smt.move = column;
					return smt;
				}
				alpha = Math.max(alpha, v_bestScore);
			}
			ScoreMoveTuple smt = new ScoreMoveTuple();
			smt.score = v_bestScore;
			smt.move = bestMove;
			return smt;
		}
	}
	
	public static ScoreMoveTuple minValue(Board board, int alpha, int beta, int depth, boolean amIOrange) {
		if( 0 == depth) {
			ScoreMoveTuple smt = new ScoreMoveTuple();
			int rawScore = Score4.scoreBoard(board);
			smt.score = amIOrange ? rawScore : -rawScore;
			smt.move = -1;
			return smt;
		} else {
			int v_worstScore = +10000099;
			int worstMove = -1;
			for(int column = 0; column < 7; column++) {
				if(board.slots[0][column] != Mycell.Barren) {
					continue;
				}
				int rowFilled = board.dropDisk(column, amIOrange ? Mycell.Yellow : Mycell.Orange);
				if(rowFilled == -1)
					continue;
				int raw_s = Score4.scoreBoard(board);
				int s = amIOrange ? raw_s : -raw_s;
				//check if win before proceeding further down search
				if(s == Score4.yellowWins) {
					worstMove = column;
					v_worstScore = s;
					board.slots[rowFilled][column] = Mycell.Barren;
					break;
				}
				Integer moveInner, scoreInner;
				ScoreMoveTuple inner = maxValue(board, alpha, beta, depth - 1, amIOrange);
				assert(inner != null);
				moveInner = inner.move; //moveInner is never read on purpose. it's not needed.
				scoreInner = inner.score; //maxValue already returned a score with the correct sign based on amIOrange argument.
				board.slots[rowFilled][column] = Mycell.Barren;
				if(scoreInner <= v_worstScore) {
					v_worstScore = scoreInner;
					worstMove = column;
				}
				if(v_worstScore < alpha) {
					ScoreMoveTuple smt = new ScoreMoveTuple();
					smt.score = v_worstScore;
					smt.move = column;
					return smt;
				}
				beta = Math.min(beta, v_worstScore);
			}
			ScoreMoveTuple smt = new ScoreMoveTuple();
			smt.score = v_worstScore;
			smt.move = worstMove;
			return smt;
		}
	}

}
