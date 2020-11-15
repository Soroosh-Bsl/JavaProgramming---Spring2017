import java.util.*;

public class Main
{
    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);
        int t = scanner.nextInt();

        long tStart = System.nanoTime();
        Integral integral = new Integral(t);

        FirstThread firstThread = new FirstThread(integral, t);
        SecondThread secondThread = new SecondThread(integral, t);


        firstThread.start();
        secondThread.start();
        try{
            firstThread.join();
            secondThread.join();
        }catch(Exception e){
            e.printStackTrace();
        }

        long tFinish = System.nanoTime();

        System.out.println("Calculated = " + integral.sum);
        System.out.println("Real = " + (1-Math.cos((double)t)));
        System.out.println("Time = " + (tFinish-tStart)/1000000 + " ms");
    }
}


class Integral{
    double sum;
    int t;

    Integral(int t){
        this.t = t;
    }

    public synchronized void safeCalculate(int down, int up){
        for (int i = down; i <= up; i++){
            sum += Math.sin((double) i/1000) * 1/1000;
        }
    }
    public void calculate(int down, int up){
        for (int i = down; i <= up; i++){
            sum += Math.sin((double) i/1000) * 1/1000;
        }
    }
}


class FirstThread extends Thread{
    Integral integral;
    int t;
    FirstThread(Integral integral, int t){
        this.integral = integral;
        this.t = t;
    }

    @Override
    public void run() {
        integral.safeCalculate(1, t*250);
        integral.calculate(t*250 + 1, t*500);
    }
}


class SecondThread extends Thread{
    Integral integral;
    int t;
    SecondThread(Integral integral, int t){
        this.integral = integral;
        this.t = t;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        integral.safeCalculate(t*500 + 1, t*1000);
    }
}