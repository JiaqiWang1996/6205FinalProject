import com.sun.org.apache.bcel.internal.generic.RETURN;

public class Ranking {
	 public static double[] PosionRandom(double lamda1, double lamda2) {

	       
	       
	        int tg1=0,tg2=0,tw1=0,tw2=0,td=0;//tg=team goal,tw=team wins,td=team draw;
	       for(int i=0;i<10000;i++)  // go on with the matched 10000times
	       {
	           
	           tg1 = getPossionVariable(lamda1);
	           tg2 = getPossionVariable(lamda2);
	            if (tg1< tg2) {//team 2 goal larger 
	                tw2++;//team2 wins,counter+1;
	            }
	           if (tg1 > tg2) {
	                tw1++;//team1 wins,counter+1;
	            }
	           else {td++;}
	    }
	        double p1 = tw1 / 10000;
	        double p2 = tw2 / 10000 ;
	        double pd=td/10000; 
	        return new double[] { p1, p2,pd };
} 
	 private static int getPossionVariable(double lamda){//define Possion distribution
	        int x = 0;
	        double y = Math.random(), cdf = getPossionProbability(x, lamda);
	        while (cdf < y) {
	            x++;
	            cdf += getPossionProbability(x, lamda);
	        }
	        return x;
	    }

	    private static double getPossionProbability(int k, double lamda) {
	        double c = Math.exp(-lamda), sum = 1;
	        for (int i = 1; i <= k; i++) {
	            sum *= lamda / i;
	        }
	        return sum * c;
	    }
}