package mainPackage;

public class BoardPosition {

	/**
	 * 0-5
	 */
	int y;
	/**
	 * 0-6
	 */
	int x;
	Color color;
	public BoardPosition(int y,int x){
		this.y = y;
		this.x = x;
	}
	public BoardPosition(){
		this.y=0;
		this.x=0;
	}
	public BoardPosition(int y,int x, Color color){
		this.y = y;
		this.x = x;
		this.color = color;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
