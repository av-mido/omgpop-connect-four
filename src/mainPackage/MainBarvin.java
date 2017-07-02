package mainPackage;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;




public class MainBarvin {
	public static final int EXCLAMATIONMARK_X = 1414;
	public static final int EXCLAMATIONMARK_Y = 57;

	public static void runBot(){
		Main main = new Main();
		Board oldState = new Board();
		Board newState;
		while(true){
			if(Main.isAGameGoingOn()){
				System.out.println("game is going on");
				if(Main.isItMyTurn()){
					Color myColor = Main.getMyColor();
					newState = Main.getScreenshotBoardState(false, false);
					BoardPosition lastOppMove = Board.subtractStates(newState, oldState);
					//if new game, opp hasn't moved --> lastOppMove == null
					assert(lastOppMove.color!=myColor);
					if(lastOppMove == null){
						//new game, make program go first
						BoardPosition programMove = getProgramsMove(newState, true);
						//then transfer move from program to omgpop
						Main.performMove(programMove.x);
						//update oldstate here?
						newState.gameboard[programMove.y][programMove.x] = programMove.color;
						oldState = newState;
					} else {
						//existing game, input lastOppMove to program,
						inputMoveToProgram(lastOppMove);
						//then copy from program to omgpop
						BoardPosition programMove = getProgramsMove(newState, false); //while loop
						//then transfer move from program to omgpop
						Main.performMove(programMove.x);
						//update oldstate here?
						newState.gameboard[programMove.y][programMove.x] = programMove.color;
						oldState = newState;
					}
					
					
					//update oldstate to newState plus the move just made.
				}
				Thread.sleep(3000);
			} else {
				Thread.sleep(3000);
			}
		}
	}
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
//		runBot();
		System.out.println("running");
		clickExclamationMark();
	}
	
	public static BoardPosition getProgramsMove(Board state, boolean hitExclamationMark){
		if(hitExclamationMark){
			clickExclamationMark();
		}
		BoardPosition programMove = null;
		while(programMove == null){
			Board newProgramState = getProgramState();
			programMove = Board.subtractStates(newProgramState, state);
		}
		return programMove;
	}
	
	public static void clickExclamationMark(){
		try {
			Robot robot = new Robot();
			robot.delay(1000);
			robot.mouseMove(EXCLAMATIONMARK_X, EXCLAMATIONMARK_Y);
			robot.delay(800);
			robot.mousePress(InputEvent.BUTTON1_MASK);
			robot.delay(1000);
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
			
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
