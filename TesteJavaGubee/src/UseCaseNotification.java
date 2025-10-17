import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public interface UseCaseNotification {
    @Transaction
    void notifyEveryHour(String customerId, PresenterNotification presenter);

    @FunctionalInterface
    interface PresenterNotification {
        void notification(String message);
    }

    class PoolingUseCaseNotification implements UseCaseNotification {

        @Override
        public void notifyEveryHour(String customerId, PresenterNotification presenter) {
            System.out.println("processando regra de negocio");
            presenter.notification(
                    String.format("mensagem a ser enviada para %s: %s \n", customerId, new Random().nextInt()));
        }
    }

    public class NotificationHandler implements InvocationHandler {

        private final Object target;

        public NotificationHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.isAnnotationPresent(Transaction.class)) {
                System.out.println("Iniciando execução do método " + method.getName());

                try {
                    Object result = method.invoke(target, args);

                    System.out.println("Sucesso!");
                    System.out.println("Finalizando execução do método " + method.getName());
                    return result;
                } catch (Exception e) {
                    System.out.println("Erro durante a execução: " + e.getMessage());
                    System.out.println("Finalizando execução do método " + method.getName());
                    throw e;
                }
            } else {
                return method.invoke(target, args);
            }
        }
    }

    static void main(String[] args) {
        ScheduledExecutorService controller = Executors.newSingleThreadScheduledExecutor();
        var notificationUseCase = new PoolingUseCaseNotification();
        ClassLoader notificationLoader = notificationUseCase.getClass().getClassLoader();
        Class[] interfaces = notificationUseCase.getClass().getInterfaces();

        UseCaseNotification proxyNotification = (UseCaseNotification) Proxy.newProxyInstance(notificationLoader,
                interfaces, new NotificationHandler(notificationUseCase));

        PresenterNotification emailPresenter = (message) -> System.out.printf("email %s", message);
        PresenterNotification whatsAppPresenter = (message) -> System.out.printf("whatApp %s", message);
        PresenterNotification smsPresenter = (message) -> System.out.printf("sms %s", message);
        PresenterNotification[] notifications = { emailPresenter, whatsAppPresenter, smsPresenter };
        controller.scheduleAtFixedRate(() -> {
            try {
                var nextPos = Math.abs(new Random().nextInt()) % 3;
                proxyNotification.notifyEveryHour(UUID.randomUUID().toString(), notifications[nextPos]);
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
            System.out.println();
        }, 1, 3, TimeUnit.SECONDS);
    }
}
