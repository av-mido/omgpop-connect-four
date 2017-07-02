package mainPackage;

import java.util.Random;

public class Test1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Random rand = new Random();
		rand.setSeed(1234);
		int tmp = -1;
		int count=0;
		while(tmp!=6){
			tmp = (int) Math.floor(rand.nextGaussian()*2.0 + 3);
			if(tmp<0){tmp=0;}
			if(tmp>6){tmp=6;}
			System.out.println(tmp);
			count++;
		}
		System.out.println(count);
	}

}
