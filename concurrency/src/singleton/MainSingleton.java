package singleton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class MainSingleton {

    private static ExecutorService executor;
    public static void main(String[] args) throws InterruptedException {
        
        executor = Executors.newFixedThreadPool(2);
        try {
            executor.submit(new DemoThreadCont());
            executor.submit(new DemoThreadCont());
        }finally{
            //Para de aceitar novas tarefas
            executor.shutdown();

            //Espera até que todas as tarefas terminem
            if(!executor.awaitTermination(30, TimeUnit.SECONDS)){
                System.out.println("forçando shutdown");
                executor.shutdownNow();
            }
            ClasseSingleton cs = ClasseSingleton.getInstance();
            System.out.println(cs);
        }
        

        

    }
}

class DemoThreadCont implements Runnable {
    private static ReentrantLock lock = new ReentrantLock();
    @Override
    public void run() {
        lock.lock();
        String threadName = Thread.currentThread().getName();
        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(700);
                ClasseSingleton.getInstance().incrementar(i);
                System.out.println(threadName + " incrementando");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } 

        }
        lock.unlock();

    }

}
