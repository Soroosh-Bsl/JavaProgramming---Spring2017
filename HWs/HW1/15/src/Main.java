import java.util.ArrayList;
import java.util.Scanner;

class Site
{
    double x = 0., y = 0.;

}

class Elephant
{
    double a = 0.;
    double b = 0.;
    double c = 0.;
}
public class Main
{

    public static void main(String[] args)
    {
        ArrayList<Integer> seenOnes = new ArrayList<Integer>();
        int i = 0, j =0, k = 0, areas = 0;
        boolean test1 = false, test2 = false, flag = false;
        Scanner Scan = new Scanner(System.in);

        double radius = Scan.nextDouble();
        int numOfElephs = Scan.nextInt();
        int numOfSites = Scan.nextInt();

        Site[] sites = new Site[numOfSites];
        for (i = 0; i < numOfSites; i++)
        {
            sites[i] = new Site();
            sites[i].x = Scan.nextDouble();
            sites[i].y = Scan.nextDouble();
        }

        Elephant[] elephants = new Elephant[numOfElephs];
        for (i = 0; i < numOfElephs; i++)
        {
            elephants[i] = new Elephant();
            elephants[i].a = Scan.nextDouble();
            elephants[i].b = Scan.nextDouble();
            elephants[i].c = Scan.nextDouble();
        }

        for (i = 0; i < numOfSites; i++)
        {
            if (!seenOnes.contains(new Integer(i)))
            {
                areas += 1;
                for (j = i + 1; j < numOfSites; j++)
                {
                    flag = false;
                    for (k = 0; k < numOfElephs; k++)
                    {
                        if ((elephants[k].b * elephants[k].b) > 0.00001 && sites[i].y < (elephants[k].a * sites[i].x + elephants[k].c)/(-elephants[k].b))
                        {
                            test1 = false;
                        }
                        else if ((elephants[k].b * elephants[k].b) < 0.00001)
                        {
                            if (sites[i].x > elephants[k].c/(-1.0 * elephants[k].a))
                            {
                                test1 = true;
                            }
                            else
                            {
                                test1 = false;
                            }
                        }
                        else
                        {
                            test1 = true;
                        }

                        if ((elephants[k].b * elephants[k].b) > 0.00001 && sites[j].y < (elephants[k].a * sites[j].x + elephants[k].c)/(-1. * elephants[k].b))
                        {
                            test2 = false;
                        }
                        else if ((elephants[k].b * elephants[k].b) < 0.00001)
                        {
                            if (sites[j].x > elephants[k].c/(-1. * elephants[k].b))
                            {
                                test2 = true;
                            }
                            else
                            {
                                test2 = false;
                            }
                        }
                        else
                        {
                            test2 = true;
                        }

                        if ((test1 && !test2) || (!test1 && test2))
                        {
                            flag = true;
                        }
                        else
                        {
                            if (k == numOfElephs - 1 && !flag)
                            {
                                seenOnes.add(new Integer(j));
                            }
                        }
                    }
                }
            }
        }
        if (areas == numOfSites)
            System.out.println("Alright");
        else
            System.out.println("Risk of being detected");
        System.out.print(areas);
    }
}
