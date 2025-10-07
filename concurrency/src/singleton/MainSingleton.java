package singleton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainSingleton {

    private static ExecutorService executor;
    public static void main(String[] args) throws InterruptedException {
        
        executor = Executors.newFixedThreadPool(2);
        // cs = ClasseSingleton.getInstance(4);
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
    @Override
    public void run() {
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

    }

}
