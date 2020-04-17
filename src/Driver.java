import java.util.ArrayList;
import java.util.List;

import javax.swing.ListSelectionModel;

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
				
				matches.add(allteam.get(i));//array  a[i];List<String>  a.get(i)
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
			for(int i=0;i<matches.size();) {
			team1 = matches.get(i);
			team2 = matches.get(i+1);
			i=i+2;
			System.out.println(team1 + " " + team2);}

			for (String[] Team : CSVdata.teamStrings) {

				if (team1.equals(Team[0]) && team2.equals(Team[1])) {//compare by row
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
	}
}
