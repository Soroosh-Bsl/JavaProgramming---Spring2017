import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

class TurnTransfer
{
    int customerId;
    int accountId;
    int money;

    TurnTransfer(int customerId, int accountId, int money)
    {
        this.customerId = customerId;
        this.accountId = accountId;
        this.money = money;
    }
}

class Bank
{
    static double nerkhSood;
    ArrayList<Account> accounts = new ArrayList<Account>();
    ArrayList<TurnTransfer> turnTransfers = new ArrayList<>();
    Customer[] customers = new Customer[100000];
    ArrayList<Integer> winners = new ArrayList<Integer>();

    Account addNewAccount(int customerId, int money)
    {
        Account tempAcc = new Account(money, customerId);
        tempAcc.startingMoney = money;
        tempAcc.ID = accounts.size();
        tempAcc.maghadir.add(money);
        tempAcc.numOfTarakonesh++;
        accounts.add(tempAcc);

        return tempAcc;
    }

    boolean putMoney(int customerId, int accountId, int money)
    {
        boolean valid = false;
        int indexOfCustomer = -1;

        for (int i = 0; i < customers.length; i++)
        {
            if (customers[i] == null)
                break;
            if (customers[i].ID == customerId)
            {
                indexOfCustomer = i;
                break;
            }
        }
        if (indexOfCustomer >= 0 && customers[indexOfCustomer].putMoney(accountId, money, accounts.size(), accounts))
        {
            for (int i = 0; i < customers.length; i++)
            {
                if (customers[i] == null)
                    continue;
                if (customers[i].ID == customerId)
                {
                    if (customers[i].relAccs.contains(accounts.get(accountId)))
                        valid = true;
                    else
                        valid = false;
                }
            }
            if (valid)
            {
                accounts.get(accountId).putMoney(money);
                accounts.get(accountId).maghadir.add(accounts.get(accountId).money);
                accounts.get(accountId).numOfTarakonesh++;
                return true;

            }
        }
        return false;
    }

    boolean takeMoney(int customerId, int accountId, int money)
    {
        boolean valid = false, turnOn = false;
        int indexOfCustomer = -1;

        for (int i = 0; i < customers.length; i++)
        {
            if (customers[i] == null)
            {
                continue;
            }
            if (customers[i].ID == customerId && customers[i].relAccs.size() > 1)
            {
                turnOn = false;
                indexOfCustomer = i;
            }
            else if (customers[i].ID == customerId && customers[i].relAccs.size() == 1 && money > 0)
                turnOn = true;
        }
        if (turnOn)
        {
            TurnTransfer temp = new TurnTransfer(customerId, accountId, money);
            turnTransfers.add(temp);
        }

        else if (!turnOn && indexOfCustomer >= 0 && customers[indexOfCustomer].takeMoney(accountId, money, accounts.size(), accounts))
        {
            for (int i = 0; i < customers.length; i++)
            {
                if (customers[i] == null)
                    continue;
                if (customers[i].ID == customerId)
                {
                    if (customers[i].relAccs.contains(accounts.get(accountId)))
                        valid = true;
                    else
                        valid = false;
                }
            }

            if (valid)
            {
                accounts.get(accountId).takeMoney(money);
                accounts.get(accountId).maghadir.add(accounts.get(accountId).getMoney());
                accounts.get(accountId).numOfTarakonesh++;

                if (accounts.get(accountId).money == 0)
                {
                    closeAcc(accountId);
                }
                return true;
            }
        }

        return false;
    }

    void transferMoney(int customerId, int srcAccountId, int desAccountId, int money)
    {
        boolean valid = false;

        if (money > 0 && accounts.size() > srcAccountId && srcAccountId >= 0 && accounts.size() > desAccountId  && desAccountId  >= 0 && srcAccountId != desAccountId && accounts.get(srcAccountId).getMoney() - money /*int) Math.floor((accounts.get(srcAccountId).getMoney() - money) * 0.1)*/ > 10)
        {
            for (int i = 0; i < customers.length; i++)
            {
                if (customers[i] == null)
                    continue;
                if (customers[i].ID == customerId)
                {
                    if (customers[i].relAccs.contains(accounts.get(srcAccountId)) && accounts.get(srcAccountId).getMoney() - money -(int) Math.floor((accounts.get(srcAccountId).getMoney() - money) * 0.1) > 10)
                        valid = true;
                    else
                        valid = false;
                }
            }

            if (valid)
            {
                accounts.get(srcAccountId).money -= money;
                accounts.get(srcAccountId ).maghadir.add(accounts.get(srcAccountId).getMoney());
                accounts.get(srcAccountId).numOfTarakonesh++;
                accounts.get(srcAccountId).money -= (int) Math.floor(accounts.get(srcAccountId).money * 0.1);
                accounts.get(srcAccountId).maghadir.add(accounts.get(srcAccountId).getMoney());
                accounts.get(srcAccountId).numOfTarakonesh++;
                accounts.get(desAccountId ).money += money;
                accounts.get(desAccountId).maghadir.add(accounts.get(desAccountId).getMoney());
                accounts.get(desAccountId).numOfTarakonesh++;

            }
        }
    }

    void sood()
    {
        int i, kasrSood;
        for (i = 0; i < accounts.size(); i ++)
        {
            kasrSood = (int) Math.floor(nerkhSood * accounts.get(i).getAvgOfDay());

            if (accounts.get(i).money >= kasrSood)
            {
                accounts.get(i).money -= kasrSood;
                accounts.get(i).numOfTarakonesh++;
            }
            accounts.get(i).maghadir.add(accounts.get(i).getMoney());

            if (accounts.get(i).getMoney() == 0)
            {
                closeAcc(i);
            }
        }
    }

    void showIt()
    {
        int i;
        for (i = 0; i < accounts.size(); i++)
        {
            System.out.println(accounts.get(i).getMoney());
        }
    }

    void closeAcc(int accountId)
    {
        for (int i = 0; i < customers.length; i++)
        {
            if (customers[i] == null)
                continue;
            if (customers[i].ID == accounts.get(accountId).ownerId)
            {
                customers[i].relAccs.remove(accounts.get(accountId));
            }
        }
        accounts.remove(accounts.get(accountId));

        for (int i = 0; i < accounts.size(); i++)
        {
            accounts.get(i).ID = i;
        }
    }
    void lottery(int money)
    {
        if (money <= 0)
            return;
        int minId = -1, minRank = 1000000, minAcc = -1;
        boolean sbCanWin = false;
        rankTarakonesh();
        rankDays();
        rankAvg();
        rankNumber();

        for (int i = 0; i < customers.length; i++)
        {
            if (customers[i] == null)
            {
                continue;
            }
            if (customers[i].wonLottery || customers[i].relAccs.size() <= 0)
            {
                continue;
            }
            else
            {
                sbCanWin = true;
                break;
            }
        }

        if (!sbCanWin)
            return;

        for (int i = 0; i < customers.length; i++)
        {
            if (customers[i] == null)
            {
                continue;
            }
            if (customers[i].wonLottery)
            {
                continue;
            }

            if (customers[i].rank < minRank)
            {
                minRank = customers[i].rank;
                minId = i;
            }
            else if (customers[i].rank == minRank && customers[i].ID < customers[minId].ID)
            {
                minRank = customers[i].rank;
                minId = i;
            }
        }

        for (int i = 0; i < customers[minId].relAccs.size(); i++)
        {
            if (customers[minId].relAccs.get(i).money == customers[minId].getMin())
            {
                minAcc = i;
                break;
            }
        }
        customers[minId].wonLottery = true;
        putMoney(customers[minId].ID, accounts.indexOf(customers[minId].relAccs.get(minAcc)), money);
        zeroRanks();
    }

    void rankTarakonesh()
    {
        int counter = 1;
        ArrayList<Integer> numOfTarakonesh = new ArrayList<>();
        for (int i = 0; i < customers.length; i++)
        {
            if (customers[i] == null)
                continue;

            for (int j = 0; j < customers[i].relAccs.size(); j++)
            {
                customers[i].numOfTarakonesh += customers[i].relAccs.get(j).numOfTarakonesh;
            }
            numOfTarakonesh.add(customers[i].numOfTarakonesh);
        }

        Collections.sort(numOfTarakonesh);
        for (int i = numOfTarakonesh.size() - 1; i >= 0; i--)
        {
            if (i == 0 || numOfTarakonesh.get(i) != numOfTarakonesh.get(i - 1))
            {
                int ezaf = 0;
                for (int j = 0; j < customers.length; j++)
                {
                    if (customers[j] == null)
                        continue;
                    if (customers[j].numOfTarakonesh == numOfTarakonesh.get(i))
                    {
                        ezaf += 1;
                        customers[j].rank += counter;
                    }
                }
                counter += ezaf;
            }
        }
    }

    void rankDays()
    {
        int counter = 1;
        ArrayList<Integer> numOfDays = new ArrayList<>();
        for (int i = 0; i < customers.length; i++)
        {
            if (customers[i] == null)
                continue;
            numOfDays.add(customers[i].dayOfReg);
        }

        Collections.sort(numOfDays);
        for (int i = numOfDays.size() - 1; i >= 0; i--)
        {
            if (i == 0 || numOfDays.get(i) != numOfDays.get(i - 1))
            {
                int ezaf = 0;
                for (int j = 0; j < customers.length; j++)
                {
                    if (customers[j] == null)
                        continue;
                    if (customers[j].dayOfReg == numOfDays.get(i))
                    {
                        ezaf += 1;
                        customers[j].rank += counter;
                    }
                }
                counter += ezaf;
            }
        }
    }

    void rankAvg()
    {
        int counter = 1;
        ArrayList<Integer> averages = new ArrayList<>();
        for (int i = 0; i < customers.length; i++)
        {
            if (customers[i] == null)
                continue;
            customers[i].average = customers[i].getAverage();
            averages.add(customers[i].average);
        }

        Collections.sort(averages);
        for (int i = averages.size() - 1; i >= 0; i--)
        {
            if (i == 0 || averages.get(i) != averages.get(i - 1))
            {
                int ezaf = 0;
                for (int j = 0; j < customers.length; j++)
                {
                    if (customers[j] == null)
                        continue;
                    if (customers[j].average == averages.get(i))
                    {
                        ezaf += 1;
                        customers[j].rank += counter;
                    }
                }
                counter += ezaf;
            }
        }
    }

    void rankNumber()
    {
        int counter = 1;
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < customers.length; i++)
        {
            if (customers[i] == null)
                continue;
            numbers.add(customers[i].relAccs.size());
        }

        Collections.sort(numbers);
        for (int i = numbers.size() - 1; i >= 0; i--)
        {
            if (i == 0 || numbers.get(i) != numbers.get(i - 1))
            {
                int ezaf = 0;
                for (int j = 0; j < customers.length; j++)
                {
                    if (customers[j] == null)
                        continue;
                    if (customers[j].relAccs.size() == numbers.get(i))
                    {
                        ezaf += 1;
                        customers[j].rank += counter;
                    }
                }
                counter += ezaf;
            }
        }
    }

    void zeroRanks()
    {
        for (int i = 0; i < customers.length; i++)
        {
            if (customers[i] == null)
                continue;
            customers[i].numOfTarakonesh = 0;
            customers[i].rank = 1000;
        }
    }


    void checkTurns()
    {
        boolean flag = true;
        if (turnTransfers.size() != 0)
        {
            while (turnTransfers.size() > 0 && flag == true)
            {
                flag = false;
                for (int i = 0; i < customers.length; i++)
                {
                    if (customers[i] == null)
                        continue;
                    if (customers[i].ID == turnTransfers.get(0).customerId)
                    {
                        if (customers[i].relAccs.size() > 1)
                        {
                            flag = true;
                            takeMoney(turnTransfers.get(0).customerId, turnTransfers.get(0).accountId, turnTransfers.get(0).money);
                            turnTransfers.remove(turnTransfers.get(0));
                            break;
                        }
                    }
                }

            }
        }
    }

}

class Account
{
    int ID;
    int startingMoney;
    int ownerId;
    int money;
    int numOfTarakonesh;
    ArrayList<Integer> maghadir = new ArrayList<>();
    ArrayList<Integer> avgs = new ArrayList<>();

    Account(int money, int ownerId)
    {
        this.money = money;
        this.ownerId = ownerId;
    }

    int getMoney()
    {
        return money;
    }

    void putMoney(int money)
    {
        this.money += money;
    }

    boolean takeMoney(int money)
    {
        if (money <= this.money)
        {
            this.money -= money;
            return true;
        }
        return false;
    }

    int getAvgOfDay()
    {
        int sum = 0, size = 0;
        size = maghadir.size();
        while (maghadir.size() > 0)
        {
            sum += maghadir.get(0);
            maghadir.remove(maghadir.get(0));
        }
        avgs.add(sum/size);
        return sum/size;

    }

    int finalAvgs()
    {
        int sum = 0, todaySum = 0, todayAvg = 0;
        for (int i = 0; i < avgs.size(); i++)
        {
            sum += avgs.get(i);
        }
        for (int i = 0; i < maghadir.size(); i++)
        {
            if (maghadir.size() > 0)
            {
                todaySum += maghadir.get(i);
            }
        }
        if (maghadir.size() > 0)
            todayAvg = todaySum/maghadir.size();
        return (sum + todayAvg)/(avgs.size() + 1);
    }
}

class Customer
{
    int ID;
    ArrayList<Account> relAccs = new ArrayList<>();
    int dayOfReg = 0;
    int numOfTarakonesh = 0;
    int rank = 1000;
    int average;
    boolean wonLottery = false;

    Customer(int ID)
    {
        this.ID = ID;
    }

    void addAccount(Account account)
    {
        relAccs.add(account);
    }

    int getMin()
    {
        int i = 0;
        if (relAccs.size() > 0)
        {
            int min = relAccs.get(0).getMoney();
            for (i = 0; i < relAccs.size(); i++)
            {
                if (relAccs.get(i).getMoney() < min)
                    min = relAccs.get(i).getMoney();
            }
            return min;
        }
        return 10;
    }

    int getAverage()
    {
        int sum = 0;
        for (int i = 0; i < relAccs.size(); i++)
        {
            sum += relAccs.get(i).finalAvgs();
        }
        return sum/relAccs.size();
    }

    boolean putMoney(int accountId, int money, int size,  ArrayList<Account> accounts)
    {
        if (money > 0 && accountId < size && accountId >= 0)
        {
            if (relAccs.contains(accounts.get(accountId)))
            {
                return true;
            }
        }
        return false;
    }

    boolean takeMoney(int accountId, int money, int size, ArrayList<Account> accounts)
    {
        if (money > 0 && accountId < size && accountId >= 0)
        {
            if (relAccs.contains(accounts.get(accountId)))
            {
                if (accounts.get(accountId).getMoney() >= money)
                    return true;
            }
        }
        return false;
    }
}


public class Main {

    public  static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\\\d+)?");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean day = false, canOpen = false, takeSeen = false, putSeen = false, startSeen = false, endSeen = false, openSeen = false, lotterySeen = false, transferSeen = false;
        int i = 0, numOfCstms = 0, numOfIntsBeforeKey = 0, j = 0, customerIndex = 0, numOfDays = 0;
        String garbage = new String();
        ArrayList<Integer> oldCstms = new ArrayList();

        String[] order = new String[1000];
        String temporary = new String();
        Bank bank = new Bank();
        Customer[] customers = new Customer[100000];
        bank.customers = customers;

        while (1 == 1)
        {
            if (scanner.hasNextDouble())
            {
                Bank.nerkhSood = scanner.nextDouble();
                break;
            }
            else
                garbage = scanner.nextLine();
        }

        ArrayList<Integer> integers = new ArrayList<>();
        while (numOfDays < 7) {
            temporary = scanner.nextLine();
            for (String S : temporary.split(" ")) {
                order[i] = new String();
                order[i] = S;
                i++;
            }

            for (int k = 0; integers.size() > 0;)
            {
                integers.remove(k);
            }
            takeSeen = false;
            putSeen = false;
            startSeen = false;
            endSeen = false;
            openSeen = false;
            lotterySeen = false;
            transferSeen = false;
            numOfIntsBeforeKey = 0;

            for (j = 0; j < i; j++)
            {
                if (order[j].equals("start")) {
                    startSeen = true;
                }
                else if (order[j].equals("end")) {
                    endSeen = true;
                } else if (order[j].equals("open")) {
                    openSeen = true;
                } else if (order[j].equals("put")) {
                    putSeen = true;
                } else if (order[j].equals("take")) {
                    takeSeen = true;
                } else if (order[j].equals("transfer")) {
                    transferSeen = true;
                } else if (order[j].equals("lottery")) {
                    lotterySeen = true;
                } else if (isNumeric(order[j]) && Integer.parseInt(order[j]) >= 0) {
                    integers.add(Integer.parseInt(order[j]));
                    if (!startSeen && !endSeen && !lotterySeen && !openSeen && !takeSeen && !putSeen && !transferSeen)
                        numOfIntsBeforeKey++;
                }

            }

            customerIndex = -1;
            if (!day && startSeen && integers.size() == 0 && numOfIntsBeforeKey == 0) {
                day = true;
            } else if (day && endSeen && integers.size() == 0 && numOfIntsBeforeKey == 0) {
                bank.sood();
                numOfDays++;
                day = false;
            } else if (day && lotterySeen && integers.size() == 1 && numOfIntsBeforeKey == 0) {
                bank.lottery(integers.get(0));
            }
            else if (day && openSeen && integers.size() == 2 && numOfIntsBeforeKey == 1) {
                if (!oldCstms.contains(integers.get(0)) && integers.get(1) >= 10) {
                    canOpen = true;
                    customers[numOfCstms] = new Customer(integers.get(0));
                    customers[numOfCstms].dayOfReg = 7 - numOfDays;
                    oldCstms.add(integers.get(0));
                    customerIndex = numOfCstms;
                    numOfCstms++;
                }
                else if (oldCstms.contains(integers.get(0)))
                {
                    for (j = 0; j < customers.length; j++) {
                        if (customers[j] == null) {
                            continue;
                        }
                        if (customers[j].ID == integers.get(0)) {
                            customerIndex = j;
                            break;
                        }
                    }
                    if (integers.get(1) >= customers[customerIndex].getMin())
                    {
                        canOpen = true;
                    }
                    else
                    {
                        canOpen = false;
                    }
                }

                if (canOpen)
                {
                    customers[customerIndex].addAccount(bank.addNewAccount(integers.get(0), integers.get(1)));
                    bank.checkTurns();
                }
            } else if (day && takeSeen && integers.size() == 3  && numOfIntsBeforeKey == 1) {
                bank.takeMoney(integers.get(0), integers.get(2), integers.get(1));
            } else if (day && putSeen && integers.size() == 3  && numOfIntsBeforeKey == 1) {
                bank.putMoney(integers.get(0), integers.get(2), integers.get(1));
            } else if (day && transferSeen && integers.size() == 4  && numOfIntsBeforeKey == 1) {
                bank.transferMoney(integers.get(0), integers.get(2), integers.get(3), integers.get(1));
            }
            i = 0;
        }
        bank.showIt();
    }
}