import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {

    public static void main(String[] args) throws InterruptedException {
        // quanto mais nThreads, mais tarefas simultaneas podem ser feitas
        // nesse caso, a tarefa 1 e 2 são executadas simultaneamente, enquanto a tarefa
        // 3 precisa esperar
        // por conta de ter somente 2 threads
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        executor.submit(() -> {
            Thread.sleep(1000);
            System.out.println("tarefa 1");
            return null;
        });
        executor.submit(() -> {
            Thread.sleep(1000);
            System.out.println("tarefa 2");
            return null;
        });
        executor.submit(() -> {
            Thread.sleep(1000);
            System.out.println("tarefa 3");
            return null;
        });

        System.out.println(executor.getQueue().size());

        ScheduledExecutorService executorScheduled = Executors.newScheduledThreadPool(5);
        executorScheduled.schedule(() -> {
            System.out.println("Hello World");
        }, 2000, TimeUnit.MILLISECONDS);


        CountDownLatch lock = new CountDownLatch(3);

        executorScheduled = Executors.newScheduledThreadPool(5);
        ScheduledFuture<?> future = executorScheduled.scheduleAtFixedRate(() -> {
            System.out.println("Agendamento");
            lock.countDown();
        }, 500, 100, TimeUnit.MILLISECONDS);

        lock.await(1000, TimeUnit.MILLISECONDS);
        future.cancel(true);

    }

}
