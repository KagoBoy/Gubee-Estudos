
public class MainSingleton {
    public static void main(String[] args) throws InterruptedException {

        // cs = ClasseSingleton.getInstance(4);
        Thread t1 = new Thread(new DemoThreadCont(), "Thread 1");
        Thread t2 = new Thread(new DemoThreadCont(), "Thread 2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        ClasseSingleton cs = ClasseSingleton.getInstance();
        System.out.println(cs);

    }
}

class DemoThreadCont implements Runnable {
    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(1000);
                ClasseSingleton.getInstance().incrementar(i);
                System.out.println(threadName + " incrementando");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

}
