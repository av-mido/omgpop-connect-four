package mainPackage;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import importedScore4.*;
import importedScore4.Score4.Mycell;


public class Score4MainLoop {
	public static final int SQUARE_W = 55;
	public static final int SQUARE_H = 55;
	public static final int RED_R = 241;
	public static final int RED_G = 27;
	public static final int RED_B = 27;
	public static final int BLACK_R = 27;
	public static final int BLACK_G = 27;
	public static final int BLACK_B = 27;
	public static final int EMPTY_R = 35; //varies - 0~68
	public static final int EMPTY_G = 100; //varies - 93~109
	public static final int EMPTY_B = 110;
	public static final int YELLOWBOARD_R = 242;
	public static final int YELLOWBOARD_G = 237;
	public static final int YELLOWBOARD_B = 21;
	public static final int GREYTURNBAR_X = 350; //58;
	public static final int GREYTURNBAR_Y = 962;// 863;
	public static final int GREYTURNBAR_RGB = 136;
	public static final int PIECECOLOR_X = 412;// 120;
	public static final int PIECECOLOR_Y = 905;// 805;
	public static final int top_left_corner_of_yellow_board_x = 595;
	public static final int top_left_corner_of_yellow_board_y = 383;
	public static final int BOTTOM_LEFT_SQUARE_X = 532;
	public static final int BOTTOM_LEFT_SQUARE_Y = 913;
	public static final int WIDTH_OF_A_SQUARE = 121; //was 110 on old monitor
	public static final int HEIGHT_OF_A_SQUARE = 120;

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		AlphaBetaSearch aiAgent;
		
		while(true){
			if(isAGameGoingOn()){
				System.out.println("game is going on");
				if(isItMyTurn()){
					System.out.println("...it is my turn");
//					System.out.println(getMyColor());
					Score4.Mycell myColor = getMyColor();
					//aiAgent = new AlphaBetaSearch(myColor, getScreenshotBoardState(false, false));
					Score4.ScoreMoveTuple move;
					if(args != null && args.length != 0 && args[0].equals("-alphabeta")){
						System.out.println("-alphabeta");
						long startTime = System.currentTimeMillis();
						move = NonImportedAlphaBeta.maxValue(getScreenshotBoardState(false, false),
								                             -10000099, 10000099,
								                             Score4.g_maxDepth,
								                             myColor == Mycell.Orange ? true : false);
						long endtime = System.currentTimeMillis();
						System.out.println("total time for one move: " +  String.valueOf(endtime-startTime));
					} else {
						long startTime = System.currentTimeMillis();
						if(myColor.equals(Mycell.Orange))
							move = 
								Score4.abMinimax(true, Mycell.Orange, Score4.g_maxDepth, getScreenshotBoardState(false, false));
						else //only the true false value changes, not the color value -.-
							move = 
								Score4.abMinimax(false, Mycell.Orange, Score4.g_maxDepth, getScreenshotBoardState(false, false));
						long endtime = System.currentTimeMillis();
						System.out.println("total time for one move: " +  String.valueOf(endtime-startTime));
					}
//					int move = aiAgent.getMove();
					performMove(move.move);
				}else{
					System.out.println("...NOT my turn");
				}
				Thread.sleep(3000);
			} else{
				Thread.sleep(5000);
				System.out.println("not started yet...");
			}
//			Board newState = getScreenshotBoardState(false, false);
			//		Board newState = getScreenshotBoardState(true, false);
		}
	}
	
	public static void performMove(int move){ //move goes from 0-6
		Robot robot;
		try {
			robot = new Robot();
			robot.mouseMove(BOTTOM_LEFT_SQUARE_X + move*WIDTH_OF_A_SQUARE, BOTTOM_LEFT_SQUARE_Y); //244+move*110, 277);
			robot.delay(200);
			robot.mousePress(InputEvent.BUTTON1_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Score4.Mycell getMyColor(){
		try {
			Robot robot = new Robot();
			Rectangle colorbit = new Rectangle(PIECECOLOR_X,PIECECOLOR_Y,1,1);
			BufferedImage bufferedImage;
			bufferedImage = robot.createScreenCapture(colorbit);
			int rgb = bufferedImage.getRGB(0, 0);
			int  red = (rgb & 0x00ff0000) >> 16;
			int  green = (rgb & 0x0000ff00) >> 8;
			int  blue = rgb & 0x000000ff;
			if(Math.sqrt(Math.pow(red - RED_R, 2) + Math.pow(green - RED_G, 2) + Math.pow(blue - RED_B, 2)) < 
					Math.sqrt(Math.pow(red - BLACK_R, 2) + Math.pow(green - BLACK_G, 2) + Math.pow(blue - BLACK_B, 2))){
				return Score4.Mycell.Orange;
			}else{
				return Score4.Mycell.Yellow;
			}
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean isItMyTurn(){
		try {
			Robot robot = new Robot();
			Rectangle bit = new Rectangle(GREYTURNBAR_X,GREYTURNBAR_Y,1,1);
			BufferedImage bufferedImage;
			bufferedImage = robot.createScreenCapture(bit);
			int rgb = bufferedImage.getRGB(0, 0);
			int  red = (rgb & 0x00ff0000) >> 16;
			int  green = (rgb & 0x0000ff00) >> 8;
			int  blue = rgb & 0x000000ff;
			if(Math.sqrt(Math.pow(red - GREYTURNBAR_RGB, 2) + Math.pow(green - GREYTURNBAR_RGB, 2) + Math.pow(blue - GREYTURNBAR_RGB, 2)) < 20){
				return true;
			}
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isAGameGoingOn(){
		try {
			Robot robot = new Robot();
			Rectangle bit = new Rectangle(0,0,1,1);
			BufferedImage bufferedImage;
			boolean yellowSoFar = true;
			for(int i=0; i<6;i++){
				bit.x = top_left_corner_of_yellow_board_x +  i*WIDTH_OF_A_SQUARE; //298+i*11-
				for(int j=0; j<4;j++){
					bit.y = top_left_corner_of_yellow_board_y +j*HEIGHT_OF_A_SQUARE; //340+j*110
					bufferedImage = robot.createScreenCapture(bit);
					int rgb = bufferedImage.getRGB(0, 0);
					int  red = (rgb & 0x00ff0000) >> 16;
					int  green = (rgb & 0x0000ff00) >> 8;
					int  blue = rgb & 0x000000ff;
					if(!(Math.sqrt(Math.pow(red - YELLOWBOARD_R, 2) + Math.pow(green - YELLOWBOARD_G, 2) + Math.pow(blue - YELLOWBOARD_B, 2)) < 20)){
						yellowSoFar = false;
					}
				}
				if(yellowSoFar)
					return true;
			}
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static Score4.Board getScreenshotBoardState(boolean testing_screenshots_already_exist, boolean writeOutJPGsreens){
//		Board newBoard = new Board();
		Score4.Board newBoard = new Score4.Board(7,6);
		try{
			Robot robot = new Robot();
			Rectangle oneSquare = new Rectangle(0, 0, SQUARE_W, SQUARE_H);
			for(int i=0; i<6; i++){
				for(int j=0; j<7; j++){
					BufferedImage bufferedImage;
					File f = new File(i+" "+j+".jpg");
					try {
						if(testing_screenshots_already_exist){
							bufferedImage = ImageIO.read(f);
						} else{
							oneSquare.x = BOTTOM_LEFT_SQUARE_X + j*WIDTH_OF_A_SQUARE; //213 + j*110;
							oneSquare.y = BOTTOM_LEFT_SQUARE_Y - i*HEIGHT_OF_A_SQUARE; //806 - i*110;
							bufferedImage = robot.createScreenCapture(oneSquare);
							if(writeOutJPGsreens && !testing_screenshots_already_exist){
								ImageIO.write(bufferedImage, "jpg", f);
							}
						}
						int[] pixels = new int[SQUARE_W*SQUARE_H]; //SAME AS RECTANGLE DIMENSIONS
						bufferedImage.getRGB(0, 0, SQUARE_W, SQUARE_H, pixels, 0, 55);
						double sumred = 0;
						double sumgreen = 0;
						double sumblue = 0;
						double avgred;
						double avggreen;
						double avgblue;
						int numPixels = 0;
						for(int pixel : pixels){
							int  red = (pixel & 0x00ff0000) >> 16;
							int  green = (pixel & 0x0000ff00) >> 8;
							int  blue = pixel & 0x000000ff;
							//if not white
							if(Math.sqrt(Math.pow(red - RED_R, 2) + Math.pow(green - RED_G, 2) + Math.pow(blue - RED_B, 2)) < 50
									|| Math.sqrt(Math.pow(red - BLACK_R, 2) + Math.pow(green - BLACK_G, 2) + Math.pow(blue - BLACK_B, 2)) < 50
									|| Math.sqrt(Math.pow(red - EMPTY_R, 2) + Math.pow(green - EMPTY_G, 2) + Math.pow(blue - EMPTY_B, 2)) < 50){
								sumred += red;
								sumgreen += green;
								sumblue += blue;
								numPixels += 1;
							}
						}
						avgred = sumred/numPixels;
						avggreen = sumgreen/numPixels;
						avgblue = sumblue/numPixels;
//						System.out.println(i+" "+j+": "+(int)avgred+" "+(int)avggreen+" "+(int)avgblue);
						double distToRed = Math.sqrt(Math.pow(avgred - RED_R, 2) + Math.pow(avggreen - RED_G, 2) + Math.pow(avgblue - RED_B, 2));
						double distToBlack = Math.sqrt(Math.pow(avgred - BLACK_R, 2) + Math.pow(avggreen - BLACK_G, 2) + Math.pow(avgblue - BLACK_B, 2));
						double distToEmpty = Math.sqrt(Math.pow(avgred - EMPTY_R, 2) + Math.pow(avggreen - EMPTY_G, 2) + Math.pow(avgblue - EMPTY_B, 2));
						double minDist = Math.min(distToRed, Math.min(distToBlack, distToEmpty));
						//todo: fix colors. Enemies can have custom pieces that are not pure black or red :(
						if(minDist == distToRed)
							newBoard.slots[Score4.height - 1 - i][j] = Score4.Mycell.Orange;
						else if(minDist == distToEmpty)
							newBoard.slots[Score4.height - 1 - i][j] = Score4.Mycell.Barren;
						else 
							newBoard.slots[Score4.height - 1 - i][j] = Score4.Mycell.Yellow;

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
//			newBoard.printBoard();
			return newBoard;
		} catch(AWTException e){
			System.err.println("robot failed");
			return null;
		}
	}
}
