import java.util.Scanner;

class Farm
{
    int m, n;
    Kart[][] karts;
    int numOfChide;
    Farm(int m, int n)
    {
        this.m = m;
        this.n = n;
        karts = new Kart[m][n];
    }

    void setSomaghs(int x, int y, int age)
    {
        karts[x -1][y - 1] = new Somagh(age);
        karts[x -1][y - 1].charkhe = 4;
        karts[x -1][y - 1].giah = true;
    }

    void setHarz(int x, int y, int age)
    {
        karts[x - 1][y - 1] = new Harz(age);
        karts[x -1][y - 1].charkhe = 5;
        karts[x -1][y - 1].giah = false;
    }

    void pass1Day()
    {
        bardasht();
        birthSomaghs();
        birthHarz();
        sampashi();
        koodpashi();
        agePlus();
    }

    void bardasht()
    {
        for (int i = 0; i < m; i++)
        {
            for (int j = 0; j < n; j++)
            {
                if (karts[i][j] != null && karts[i][j].giah && karts[i][j].age == 4)
                {
                    numOfChide++;
                }
            }
        }
    }

    void birthSomaghs()
    {
        for (int i = 0; i < m; i++)
        {
            for (int j = 0; j < n; j++)
            {
                if (karts[i][j] != null && karts[i][j].giah && karts[i][j].age == 4)
                {
                    karts[i][j].age = 0;
                    karts[i][j].timesBeenSampashed = 0;
                    for (int x = i - 1; x <= i + 1; x++)
                    {
                        if (x >= 0 && x < m && x != i)
                        {
                            if (karts[x][j] == null)
                            {
                                karts[x][j] = new Somagh(0);
                                karts[x][j].charkhe = 4;
                                karts[x][j].giah = true;
                            }
                        }
                    }

                    for (int y = j - 1; y <= j + 1; y++)
                    {
                        if (y >= 0 && y < n && y != j)
                        {
                            if (karts[i][y] == null)
                            {
                                karts[i][y] = new Somagh(0);
                                karts[i][y].charkhe = 4;
                                karts[i][y].giah = true;
                            }
                        }
                    }
                }
            }
        }
    }

    void birthHarz()
    {
        for (int i = 0; i < m; i++)
        {
            for (int j = 0; j < n; j++)
            {
                if (karts[i][j] != null && !karts[i][j].giah && karts[i][j].age == 5)
                {
                    karts[i][j].age = 0;
                    karts[i][j].timesBeenSampashed = 0;
                    for (int x = i - 1; x <= i + 1; x++)
                    {
                        if (x >= 0 && x < m && x != i)
                        {
                            if (karts[x][j] == null)
                            {
                                karts[x][j] = new Harz(0);
                                karts[x][j].charkhe = 5;
                                karts[x][j].giah = false;
                            }
                        }
                    }

                    for (int y = j - 1; y <= j + 1; y++)
                    {
                        if (y >= 0 && y < n && y != j)
                        {
                            if (karts[i][y] == null)
                            {
                                karts[i][y] = new Harz(0);
                                karts[i][y].charkhe = 5;
                                karts[i][y].giah = false;
                            }
                        }
                    }
                }
            }
        }

    }
    void sampashi()
    {
        int number = 0, maxNumber = -1, maxSatr = -1;
        for (int i = m - 1; i >= 0; i--)
        {
            for (int j = 0; j < n; j++)
            {
                if (karts[i][j] != null && !karts[i][j].giah)
                {
                    number++;
                }
            }
            if (number >= maxNumber)
            {
                maxNumber = number;
                maxSatr = i;
            }
            number = 0;
        }

        for (int j = 0; j < n; j++)
        {
            if (karts[maxSatr][j] != null && karts[maxSatr][j].giah)
            {
                karts[maxSatr][j] = null;
            }
            else if (karts[maxSatr][j] != null && !karts[maxSatr][j].giah)
            {
                if (karts[maxSatr][j].timesBeenSampashed == 0)
                {
                    karts[maxSatr][j].timesBeenSampashed++;
                }
                else if (karts[maxSatr][j].timesBeenSampashed == 1)
                {
                    karts[maxSatr][j] = null;
                }
            }
        }
    }

    void koodpashi()
    {
        int number = 0, maxNumber = 0, maxSotun = 0;
        for (int j = n - 1; j >= 0; j--)
        {
            for (int i = 0; i < m; i++)
            {
                if (karts[i][j] != null && karts[i][j].giah)
                {
                    number++;
                }
            }
            if (number >= maxNumber)
            {
                maxNumber = number;
                maxSotun = j;
            }
            number = 0;
        }
        for (int i = 0; i < m; i++)
        {
            if (karts[i][maxSotun] != null && karts[i][maxSotun].age != karts[i][maxSotun].charkhe - 1)
            {
                karts[i][maxSotun].agePlus();
            }
        }
    }

    void agePlus()
    {
        for (int i = 0; i < m; i++)
        {
            for (int j = 0; j < n; j++)
            {
                if (karts[i][j] != null)
                {
                    karts[i][j].agePlus();
                }
            }
        }
    }
}

class Kart
{
    int charkhe;
    boolean giah;
    int age;
    int timesBeenSampashed;
    void agePlus()
    {
        if (age != charkhe)
        {
            age++;
        }
        else
            age = 1;
    }
}

class Somagh extends Kart
{
    Somagh(int age)
    {
        this.age = age;
    }
}

class Harz extends Kart
{
    Harz(int age)
    {
        this.age = age;
    }
}

public class Main {

    public static void main(String[] args)
    {
        int m = 0, n =0;
        int numOfSomaghs = 0, numOfHarz = 0, tempX = 0, tempY = 0, tempAge = 0;
        Scanner scanner = new Scanner(System.in);

        m = scanner.nextInt();
        n = scanner.nextInt();
        Farm farm = new Farm(m, n);

        numOfSomaghs = scanner.nextInt();

        if (numOfSomaghs != 0)
        {
            for (int i = 0; i < numOfSomaghs; i++)
            {
                tempX = scanner.nextInt();
                tempY = scanner.nextInt();
                tempAge = scanner.nextInt();

                farm.setSomaghs(tempX, tempY, tempAge);
            }
        }

        numOfHarz = scanner.nextInt();

        if (numOfHarz != 0)
        {
            for (int i = 0; i < numOfHarz; i++)
            {
                tempX = scanner.nextInt();
                tempY = scanner.nextInt();
                tempAge = scanner.nextInt();

                farm.setHarz(tempX, tempY, tempAge);
            }
        }

        int theDay = scanner.nextInt();

        for (int i = 0; i <= theDay; i++)
        {
            farm.pass1Day();
        }

        System.out.print(farm.numOfChide);
    }
}