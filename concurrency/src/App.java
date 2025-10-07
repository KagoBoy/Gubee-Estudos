public class App implements Runnable{

    public static Thread t1;
    public static void main(String[] args) throws Exception {
        t1 = new Thread(new App());
        //New Thread
        System.out.println("State da Thread 1 assim que foi criada: " + t1.getState()); //Assim que a thread é criada, tem state de NEW
        t1.start();
        //Runnable
        System.out.println("State da Thread 1 assim que foi iniciada: " + t1.getState()); //Depois de startar a thread, state = Runnable
        
    }

    public void run() {
        Thread t2 = new Thread(new DemoWaitingStateRunnable());
        t2.start();
        try {
            t2.join();
            System.out.println("t2.isAlive() antes da T2 ser finalizada: " + t2.isAlive());
            Thread.sleep(3000);
            System.out.println("t2.isAlive() depois da T2 ser finalizada: " + t2.isAlive());
            //Terminated
            System.out.println("State da Thread 2 depois de finalizar: " + t2.getState());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}

class DemoWaitingStateRunnable implements Runnable {
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        //Waiting
        System.out.println("State da thread 1, enquanto a thread 2 esta rodando: " + App.t1.getState());
    }
}
