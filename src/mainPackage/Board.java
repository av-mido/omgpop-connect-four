package mainPackage;

public class Board {
	public Color[][] gameboard;
	public int nextPlayer;
	public Color myColor;
	int lastmoveX;
	int lastmoveY;

	public Board() {
		gameboard = new Color[6][7];
		nextPlayer = 0;
		for(int i=5; i>=0; i--){
			for(int j=0; j<7; j++){
				gameboard[i][j] = Color.EMPTY;
			}
		}
	}
	public void printBoard(){
		System.out.println();
		for(int i=5; i>=0; i--){
			for(int j=0; j<7; j++){
				if(gameboard[i][j] == Color.EMPTY)
					System.out.print('0');
				if(gameboard[i][j] == Color.BLACK)
					System.out.print('B');
				if(gameboard[i][j] == Color.RED)
					System.out.print('R');
				if(j==6)
					System.out.println();
				
			}
		}
	}
	public static BoardPosition subtractStates(Board newState, Board oldState){
		for(int y=0;y<6;y++){
			for(int x=0;x<7;x++){
				if(newState.gameboard[y][x] != oldState.gameboard[y][x]){
					return new BoardPosition(y, x, newState.gameboard[y][x]);
				}
			}
		}
		return null;
	}
}
