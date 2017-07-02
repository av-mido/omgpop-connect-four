package mainPackage;

public class MiniMaxSearch {
	Board board;
	public MiniMaxSearch(Color myColor, Board board){
		this.board = board;
		board.myColor = myColor;
	}
	
	public int getMove(){
		return rootMaxValue();
	}
	
	public int rootMaxValue(){
		return 0;
	}
}
