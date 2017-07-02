package mainPackage;

import java.util.Random;

public class AlphaBetaSearch {
	Board board;
	int depth = 0;
	Random rand;
	int maxDepth = 6;
	public AlphaBetaSearch(Color myColor, Board board){
		this.board = board;
		board.myColor = myColor;
		rand = new Random();
//		rand.setSeed(1234);
	}
	
	public int getMove(){
		return rootMaxValue();
	}
	private int rootMaxValue(){
		board.printBoard();
		int bestMove = 2;
		double alpha = Integer.MIN_VALUE;
		double beta = Integer.MAX_VALUE;
		double v = Integer.MIN_VALUE;
		for(int x=0;x<7;x++){
			if(board.gameboard[5][x] == Color.EMPTY){
				//do move i
				board.lastmoveX = x;
				for(int y=0; y<6;y++){
					if(board.gameboard[y][x] == Color.EMPTY){
						board.lastmoveY = y;
						board.gameboard[y][x] = board.myColor;
						break;
					}
				}
				double newV = Math.max(v, minValue(alpha, beta));
				//compare v's, update bestMove if v changed
				if(newV > v){
					bestMove = x;
				}
				v = newV;
				//undo move i
				board.gameboard[board.lastmoveY][board.lastmoveX] = Color.EMPTY;
				board.lastmoveX=-1; board.lastmoveY=-1;
				//update alpha
				alpha = Math.max(alpha, v);
			}
		}
		System.out.println("best v: "+ v);
		if(v==-1){
			do{
				bestMove = (int) Math.floor(rand.nextGaussian()*2.0 + 3);
				if(bestMove<0){bestMove=0;}
				if(bestMove>6){bestMove=6;}
			} while(board.gameboard[5][bestMove] != Color.EMPTY);
			System.out.println("new random bestMove: "+bestMove);
		}
		return bestMove;
	}
	private double minValue(double alpha, double beta){
//		board.printBoard();
		String tmpstr = "";
		for(int i=0;i<depth;i++){
			tmpstr += "  ";
		}
//		System.out.println(tmpstr+"minValue called, depth "+depth);
		this.depth+=1;
		if(terminalTest()){
			this.depth-=1;
			return utility();
		}
		double v = Integer.MAX_VALUE;
		int previousMoveX=-1;
		int previousMoveY=-1;
		for(int x=0;x<7;x++){
			if(board.gameboard[5][x] == Color.EMPTY){
				//do move i
				previousMoveX = board.lastmoveX;
				board.lastmoveX = x;
				for(int y=0; y<6;y++){
					if(board.gameboard[y][x] == Color.EMPTY){
						previousMoveY = board.lastmoveY;
						board.lastmoveY = y;
						if(board.myColor==Color.RED)
							board.gameboard[y][x] = Color.BLACK;
						else
							board.gameboard[y][x] = Color.RED;
						break;
					}
				}
				v = Math.min(v, maxValue(alpha, beta));
				if(v <= alpha) {
					//undo depth
					this.depth-=1;
					//undo last move
					board.gameboard[board.lastmoveY][board.lastmoveX] = Color.EMPTY;
					board.lastmoveX=previousMoveX; board.lastmoveY=previousMoveY;
					return v;
				}
				beta = Math.min(beta, v);
				//undo last move
				board.gameboard[board.lastmoveY][board.lastmoveX] = Color.EMPTY;
				board.lastmoveX=previousMoveX; board.lastmoveY=previousMoveY;
			}
		}
		//undo depth
		this.depth-=1;
		
		return v;
	}
	
	private double maxValue(double alpha, double beta){
//		board.printBoard();
		String tmpstr = "";
		for(int i=0;i<depth;i++){
			tmpstr += "  ";
		}
//		System.out.println(tmpstr+"maxValue called, depth "+depth);
		if(terminalTest())
			return utility();
		double v = Integer.MIN_VALUE;
		int previousMoveX=-1;
		int previousMoveY=-1;
		for(int x=0;x<7;x++){
			if(board.gameboard[5][x] == Color.EMPTY){
				//do move i
				previousMoveX = board.lastmoveX;
				board.lastmoveX = x;
				for(int y=0; y<6;y++){
					if(board.gameboard[y][x] == Color.EMPTY){
						previousMoveY = board.lastmoveY;
						board.lastmoveY = y;
						board.gameboard[y][x] = board.myColor;
						break;
					}
				}
				v = Math.max(v, minValue(alpha, beta));
				if(v >= beta) { 
					//undo last move
					board.gameboard[board.lastmoveY][board.lastmoveX] = Color.EMPTY;
					board.lastmoveX=previousMoveX; board.lastmoveY=previousMoveY;
					return v; 
				}
				alpha = Math.max(alpha, v);
				//undo last move
				board.gameboard[board.lastmoveY][board.lastmoveX] = Color.EMPTY;
				board.lastmoveX=previousMoveX; board.lastmoveY=previousMoveY;
			}
		}
		return v;
	}
	
	private double utility(){
		Color lastMovedColor = board.gameboard[board.lastmoveY][board.lastmoveX];
		int returnvalue = 0;
		//check horizontal
		int tmp=0;
		for(int x=0;x<7;x++){
			if(board.gameboard[board.lastmoveY][x]==lastMovedColor){
				tmp++;
			} else { tmp = 0;}
			if(tmp==4){
				if(lastMovedColor == board.myColor){ returnvalue = 1; }
				else {returnvalue = -1;}
			}
		}
		//check vertical
		tmp=0;
		for(int y=0;y<6;y++){
			if(board.gameboard[y][board.lastmoveX]==lastMovedColor){
				tmp++;
			}else{ tmp=0;}
			if(tmp==4){
				if(lastMovedColor == board.myColor){ returnvalue = 1; }
				else {returnvalue = -1;}
			}
		}
		//check \ diagonal
		tmp=0;
		int yDiff = 5-board.lastmoveY;
		int xDiff = board.lastmoveX;
		int sy = board.lastmoveY + Math.min(yDiff, xDiff);
		int sx = board.lastmoveX - Math.min(yDiff, xDiff);
		while(sy >= 0 && sx < 7) {
			if(board.gameboard[sy][sx]==lastMovedColor){
				tmp++;
			}else{ tmp=0;}
			if(tmp==4){
				if(lastMovedColor == board.myColor){ returnvalue = 1; }
				else {returnvalue = -1;}
			}
			sy--; sx++;
		}
		//check / diagonal
		tmp=0;
		sy = board.lastmoveY - Math.min(board.lastmoveY, board.lastmoveX);
		sx = board.lastmoveX - Math.min(board.lastmoveY, board.lastmoveX);
		while(sy < 6 && sx < 7){
			if(board.gameboard[sy][sx]==lastMovedColor){
				tmp++;
			}else{ tmp=0;}
			if(tmp==4){
				if(lastMovedColor == board.myColor){ returnvalue = 1; }
				else {returnvalue = -1;}
			}
			sy++; sx++;
		}
//		System.out.println("utility: "+returnvalue);
		return returnvalue;
	}
	
	private boolean terminalTest(){
		double util = utility();
		if(this.depth >= this.maxDepth || util==1 || util==-1){
			return true;
		}else{ 
			return false;
		}
	}
	
	public static void main(String[] args) {
		Board testBoard = new Board();
//		testBoard.lastmoveX = 1;
//		testBoard.lastmoveY = 1;
		testBoard.gameboard[0][0] = Color.RED;
		testBoard.gameboard[1][0] = Color.RED;
		testBoard.gameboard[2][0] = Color.RED;
		testBoard.gameboard[3][0] = Color.BLACK;
		testBoard.gameboard[4][0] = Color.RED;
		testBoard.gameboard[5][0] = Color.RED;
		testBoard.gameboard[0][2] = Color.RED;
		testBoard.gameboard[1][2] = Color.BLACK;
		testBoard.gameboard[2][2] = Color.BLACK;
		testBoard.gameboard[3][2] = Color.BLACK;
		testBoard.gameboard[0][3] = Color.BLACK;
		testBoard.gameboard[1][3] = Color.BLACK;
		testBoard.gameboard[0][4] = Color.BLACK;

		testBoard.printBoard();
		AlphaBetaSearch search = new AlphaBetaSearch(Color.RED, testBoard);
//		System.out.println(search.utility());
		System.out.println(search.rootMaxValue());
	}
}
