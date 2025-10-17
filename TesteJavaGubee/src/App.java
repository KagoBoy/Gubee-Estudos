import java.lang.reflect.Proxy;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import interfaces.NotificationFactory;
import interfaces.UseCaseNotification;
import interfaces.UseCaseNotification.PresenterNotification;
import proxy.NotificationHandler;
import factories.PremiumNotificationFactory;
import factories.StandardNotifcationFactory;

public class App {

    private static UseCaseNotification createProxy(UseCaseNotification target) {
        ClassLoader loader = target.getClass().getClassLoader();
        Class[] interfaces = target.getClass().getInterfaces();
        return (UseCaseNotification) Proxy.newProxyInstance(
                loader,
                interfaces,
                new NotificationHandler(target));
    }

    private static NotificationFactory createFactoryBasedOnEnvironment() {
        String environment = System.getProperty("notification.type", "premium");

        switch (environment.toLowerCase()) {
            case "premium":
                return new PremiumNotificationFactory();
            case "standard":
            default:
                return new StandardNotifcationFactory();
        }
    }

    public static void main(String[] args) throws Exception {
        NotificationFactory factory = createFactoryBasedOnEnvironment();

        UseCaseNotification patternProxy = factory.createUseCase();

        UseCaseNotification dynamicProxy = createProxy(patternProxy);
        PresenterNotification[] presenters = factory.createAllPresenters();

        ScheduledExecutorService controller = Executors.newSingleThreadScheduledExecutor();

        controller.scheduleAtFixedRate(() -> {
            try {
                var nextPos = Math.abs(new Random().nextInt()) % presenters.length;
                dynamicProxy.notifyEveryHour(UUID.randomUUID().toString(), presenters[nextPos]);
            } catch (Exception e) {
                System.out.println("Erro capturado no client: " + e.getMessage());
            }
            System.out.println("---");
        }, 1, 3, TimeUnit.SECONDS);
    }
}