import java.util.Arrays;
import java.util.Scanner;

class Team implements Comparable<Team>
{
    String name = new String();
    int points = 0;
    int goalAverage = 0;

    public int compareTo(Team other)
    {
        if (this.points > other.points)
            return -1;
        if (this.points == other.points)
        {
            if (this.goalAverage > other.goalAverage)
                return -1;
            return 1;
        }
        return 1;
    }
}
public class Main {

    public static void main(String[] args)
    {
        Scanner Scan = new Scanner(System.in);
        String x = new String();
//        String y = new String();
        String enteghal = new String();
        x = Scan.nextLine();
        int i = 0, j = 0, k = 0, q = 0;
        int numOfTeams = 0;
        String[] temp = new String[1000];
        String[] tempo = new String[1000000];
        for (String transmission: x.split(" "))
        {
            temp[i] = transmission;
            i++;
        }
        numOfTeams = i;
        //System.out.println(numOfTeams);

        Team[] teams = new Team[numOfTeams];
        for (i = 0; i < numOfTeams; i++)
        {
            teams[i] = new Team();
            teams[i].name = temp[i];
        }

        int numOfMatches = (numOfTeams * (numOfTeams - 1))/2;
        //System.out.println(numOfMatches);
        String[] y = new String[1000000];
//        System.out.println(")))" + numOfMatches);
        for (q = 0; q < numOfMatches; q++)
        {
//            System.out.println("$");
            y[q] = new String();
            if (Scan.hasNext())
                y[q] = Scan.nextLine();
            else
                continue;
            i = 0;
            for (String transmission: y[q].split(" "))
            {
                tempo[i] = transmission;
                //		System.out.println(tempo[i] + i);
                i++;
            }
            i--;
            if (i == 2)
            {
                for (j = 0; j < numOfTeams; j++) {
                    if (teams[j].name.equals(tempo[0])) {
                        for (k = 0; k < numOfTeams; k++) {
                            if (teams[k].name.equals(tempo[1])) {
                                teams[j].points += 1;
                                teams[k].points += 1;
                            }
                        }
                    }
                }
            }
            else if (i == 3)
            {
                for (j = 0; j < numOfTeams; j++)
                {
                    if (teams[j].name.equals(tempo[0]))
                    {
                        for (k = 0; k < numOfTeams; k++)
                        {
                            if (teams[k].name.equals(tempo[1]))
                            {
                                if (Integer.parseInt(tempo[2]) > Integer.parseInt(tempo[3]))
                                {
                                    teams[j].points += 3;
                                    teams[j].goalAverage += Integer.parseInt(tempo[2]) - Integer.parseInt(tempo[3]);
                                    teams[k].goalAverage += Integer.parseInt(tempo[3]) - Integer.parseInt(tempo[2]);
                                }
                                else
                                {
                                    teams[k].points += 3;
                                    teams[j].goalAverage += Integer.parseInt(tempo[2]) - Integer.parseInt(tempo[3]);
                                    teams[k].goalAverage += Integer.parseInt(tempo[3]) - Integer.parseInt(tempo[2]);
                                }
                            }

                        }

                    }
                }
            }


        }
        String radif = new String();
        String points = new String();
        Arrays.sort(teams);
        for (i = 0; i < numOfTeams; i++)
        {
            String name = new String();
            if (teams[i].name.length() > 20)
            {
                temp[i] = teams[i].name.substring(0, 17) + "...";
                name = temp[i];
            }
            else
                name = teams[i].name;
            if (i + 1 > 999)
            {
                temp[i] = Integer.toString(i + 1);
                radif = temp[i].charAt(0) + "...";
            }
            else
                radif = i + 1+"";
            if (teams[i].points > 9999)
            {
                temp[i] = Integer.toString(teams[i].points);
                radif = temp[i].charAt(0) + "...";
            }
            else
                points = Integer.toString(teams[i].points);;

            System.out.format("%-3s %-20s %-4s", radif, name, points);
            if (teams[i].goalAverage != 0)
                System.out.format(" %+d", teams[i].goalAverage);
            else
                System.out.print(" 0");
            if (i != numOfTeams - 1)
                System.out.println();
        }
    }

}


