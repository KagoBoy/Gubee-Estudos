import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public interface UseCaseNotification {
   void notifyEveryHour(String customerId, PresenterNotification presenter);


   @FunctionalInterface
   interface PresenterNotification {
       void notification(String message);
   }


   class PoolingUseCaseNotification implements UseCaseNotification {


       @Override
       public void notifyEveryHour(String customerId, PresenterNotification presenter) {
           System.out.println("processando regra de negocio");
           presenter.notification(String.format("mensagem a ser enviada para %s: %s", customerId, new Random().nextInt()));
       }
   }




   static void main(String[] args) {
       ScheduledExecutorService controller = Executors.newSingleThreadScheduledExecutor();
       var notificationUseCase = new PoolingUseCaseNotification();
       PresenterNotification emailPresenter = (message) -> System.out.printf("email %s", message);
       PresenterNotification whatsAppPresenter = (message) -> System.out.printf("whatApp %s", message);
       PresenterNotification smsPresenter = (message) -> System.out.printf("sms %s", message);
       PresenterNotification[] notifications = {emailPresenter, whatsAppPresenter, smsPresenter};
       controller.scheduleAtFixedRate(() -> {
           var nextPos = Math.abs(new Random().nextInt()) % 3;
           notificationUseCase.notifyEveryHour(UUID.randomUUID().toString(), notifications[nextPos]);
           System.out.println();
       }, 1, 1, TimeUnit.SECONDS);
   }
}
