import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;
import java.util.List;

import javax.swing.ListSelectionModel;
import javax.swing.text.StyledEditorKit.ForegroundAction;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.sun.jndi.url.iiopname.iiopnameURLContextFactory;
import com.sun.org.apache.bcel.internal.generic.AllocationInstruction;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import com.sun.org.apache.bcel.internal.generic.SWAP;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import com.sun.org.apache.xerces.internal.impl.xs.identity.Selector.Matcher;

public class Driver {

	public static void main(String[] args) {
		System.out.println("system starts...");
		demo();
	}

	public static void demo() {
		CSVdata.loadData();
		List<String> allteam = new ArrayList<String>();
		for (String[] team : CSVdata.teamStrings) {
			if (!allteam.contains(team[0])) {
				allteam.add(team[0]);
			}
		}
		int teamNum = allteam.size();

//present all possible match pairs

		List<List<String>> matches = new ArrayList<List<String>>();// list of Lists
		for (int i = 0; i < teamNum; i++) {
			for (int j = i + 1; j < teamNum; j++) {
				List<String> match = new ArrayList<String>(Arrays.asList(allteam.get(i), allteam.get(j)));

				// all pairs Lists
				matches.add(match);// nested
			}

		}

		int matchPairNum = matches.size();

//record the score within a single pair
		List<List<String>> poissonparameter = new ArrayList<List<String>>();
		for (int i = 0; i < matchPairNum; i++) {
			// set basic attributes
			String team1 = "";
			String team2 = "";

			int g1 = 0;
			int g2 = 0;
			int matchCount = 0;

			List<String> team = matches.get(i);

			System.out.println("");
			team1 = ((List<String>) team).get(0);
			team2 = ((List<String>) team).get(1);
			for (String[] Team : CSVdata.teamStrings) {

				if (team1.equals(Team[0]) && team2.equals(Team[1])) {// compare by row
					g1 += Integer.valueOf(Team[2]);
					g2 += Integer.valueOf(Team[3]);

					matchCount++;

				}
				if (team2.equals(Team[0]) && team1.equals(Team[1])) {
					g2 += Integer.valueOf(Team[2]);
					g1 += Integer.valueOf(Team[3]);

					matchCount++;
				}
			} // set the possibility
			for (List<String> match : matches) {
				double lamda1 = g1 / (double) matchCount;
				double lamda2 = g2 / (double) matchCount;

				List<String> singleparameter = new ArrayList<String>(
						Arrays.asList(match.get(0), match.get(1), String.valueOf(lamda1), String.valueOf(lamda2)));
				poissonparameter.add(singleparameter);
			}
			for (List<String> tmp : poissonparameter) {
				
				double[] result = PoissonRandom(Double.valueOf(tmp.get(2)), Double.valueOf(tmp.get(3)));
				System.out.println(tmp.get(0) + "" + tmp.get(1) + ""
						+ String.valueOf(result[0])+ String.valueOf(result[1])+ String.valueOf(result[2]));
			}
		}

		// create the TotalScoreList;

		List<List<String>> TotalScoreList = new ArrayList<List<String>>();
		for (int i = 0; i < teamNum; i++) {
			int allwin = 0, alllose = 0, alltie = 0;
			for (String[] match : CSVdata.teamStrings) {

				if (allteam.get(i).equals(match[0])) {
					if (Integer.valueOf(match[2]) > Integer.valueOf(match[3])) {
						allwin++;
					}
					if (Integer.valueOf(match[2]) < Integer.valueOf(match[3])) {
						alllose++;
					} else {
						alltie++;

					}
				}
				if (allteam.get(i).equals(match[1])) {
					if (Integer.valueOf(match[2]) > Integer.valueOf(match[3])) {
						alllose++;
					}
					if (Integer.valueOf(match[2]) < Integer.valueOf(match[3])) {
						allwin++;
					} else {
						alltie++;

					}
				}
			}

			int weightedScore = allwin * 2 + alltie * 1 + alllose * 0;
			List<String> SingleScore = new ArrayList<String>(Arrays.asList(allteam.get(i), String.valueOf(allwin),
					String.valueOf(alllose), String.valueOf(alltie), String.valueOf(weightedScore)));
			TotalScoreList.add(SingleScore);
			for (int i1 = 0; i1 < teamNum; i1++) {
				for (int j = i1 + 1; j < teamNum; j++) {
					if (Integer.valueOf(TotalScoreList.get(i).get(4)) < Integer.valueOf(TotalScoreList.get(j).get(4))) {
						String exc1 = "", exc2 = "", exc3 = "", exc4 = "", exc5 = "";
						exc1 = TotalScoreList.get(i1).get(0);
						exc2 = TotalScoreList.get(i1).get(1);
						exc3 = TotalScoreList.get(i1).get(2);
						exc4 = TotalScoreList.get(i1).get(3);
						exc5 = TotalScoreList.get(i1).get(4);

						TotalScoreList.get(i1).set(0, TotalScoreList.get(j).get(0));
						TotalScoreList.get(i1).set(1, TotalScoreList.get(j).get(1));
						TotalScoreList.get(i1).set(2, TotalScoreList.get(j).get(2));
						TotalScoreList.get(i1).set(3, TotalScoreList.get(j).get(3));
						TotalScoreList.get(i1).set(4, TotalScoreList.get(j).get(4));
						TotalScoreList.get(i1).set(0, exc1);
						TotalScoreList.get(i1).set(1, exc2);
						TotalScoreList.get(i1).set(2, exc2);
						TotalScoreList.get(i1).set(3, exc3);
						TotalScoreList.get(i1).set(4, exc4);// set(int,string) get()is not variable

					}
				}
			}

			for (List<String> tmp : TotalScoreList) {
				System.out.println(tmp);
			}
		}
	}

	// define poisson distribution
	private double average2;
	private double average1;
	double[] probabilitydensity = PoissonRandom((double) average1, (double) average2);
	double team1winprobability = probabilitydensity[0];
	double team2winprobability = probabilitydensity[1];
	double teamtieprobability = probabilitydensity[2];

	public static double[] PoissonRandom(double lamda1, double lamda2) {

		int tg1 = 0, tg2 = 0, tw1 = 0, tw2 = 0, td = 0;// tg=team goal,tw=team wins,td=team draw;
		for (int i = 0; i < 10000; i++) // go on with the matched 10000times
		{

			tg1 = getPossionVariable(lamda1);
			tg2 = getPossionVariable(lamda2);
			if (tg1 < tg2) {// team 2 goal larger
				tw2++;// team2 wins,counter+1;
			}
			if (tg1 > tg2) {
				tw1++;// team1 wins,counter+1;
			} else {
				td++;
			}
		}
		double p1 = tw1 / 10000.0;
		double p2 = tw2 / 10000.0;
		double pd = td / 10000.0;//  /1000.0  not /1000
		return new double[] { p1, p2, pd };
	}

	private static int getPossionVariable(double lamda) {// define Poisson distribution
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
