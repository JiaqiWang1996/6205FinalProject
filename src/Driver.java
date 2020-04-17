import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ListSelectionModel;
import javax.swing.text.StyledEditorKit.ForegroundAction;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

//import org.apache.commons.csv.CSVFormat;
//import org.apache.commons.csv.CSVPrinter;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

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
		List<String> matches = new ArrayList<String>();
		for (int i = 0; i < teamNum; i++) {
			for (int j = i + 1; j < teamNum; j++) {

				matches.add(allteam.get(i));// array a[i];List<String> a.get(i)
				matches.add(allteam.get(j));
				// all match pairs
			}
		}
		int matchPairNum = matches.size();

		// set basic attributes
		String team1 = "";
		String team2 = "";

		int g1 = 0;
		int g2 = 0;
		int matchCount = 0;

//record the score within a single pair
		for (String team : matches) {
			for (int i = 0; i < matches.size();) {
				team1 = matches.get(i);
				team2 = matches.get(i + 1);
				i = i + 2;
//				System.out.println(team1 + " " + team2);
			}

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
			}
		}

		// set the possibility
		for (String match : matches) {
			double lamda1 = g1 / matchCount;
			double lamda2 = g2 / matchCount;
			double[] probabilitydensity = PoissonRandom(lamda1, lamda2);
			double team1winprobability = probabilitydensity[0];
			double team2winprobability = probabilitydensity[1];
			double teamtieprobability = probabilitydensity[2];

		}
		// create the ranking list
		List<String> rankingList = new ArrayList<String>();

		int allwin = 0, alllose = 0, alltie = 0;
		for (int i = 0; i < teamNum; i++) {
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
			rankingList.add(allteam.get(i));
			rankingList.add(String.valueOf(allwin));
			rankingList.add(String.valueOf(alllose));
			rankingList.add(String.valueOf(alltie));
			rankingList.add(String.valueOf(allwin * 2 + alltie));
//			writeCSVFile(allteam, "./rankingoutcome.csv", new String[] { "TeamName", "allWin", "allTie", "allLose",
//					"weighted-score(allwin*2+alltie*1+alllose*0)" });

		}
	}


	private static void writeCSVFile(List<String> rankingList, String FILE_NAME, String[] FILE_HEADER) {

		CSVFormat format = CSVFormat.DEFAULT.withHeader(FILE_HEADER);

		try (Writer out = new FileWriter(FILE_NAME); CSVPrinter printer = new CSVPrinter(out, format)) {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// output all possible pairs

	// define poisson distribution
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
		double p1 = tw1 / 10000;
		double p2 = tw2 / 10000;
		double pd = td / 10000;
		return new double[] { p1, p2, pd };
	}

	private static int getPossionVariable(double lamda) {// define Possion distribution
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
