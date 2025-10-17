package factories;

import interfaces.NotificationFactory;
import interfaces.UseCaseNotification;
import interfaces.UseCaseNotification.PresenterNotification;
import proxy.UseCaseNotificationProxy;
import services.PoolingUseCaseNotification;;

public class PremiumNotificationFactory implements NotificationFactory{
    @Override
        public UseCaseNotification createUseCase() {
            UseCaseNotification realSubject = new PoolingUseCaseNotification();
            return new UseCaseNotificationProxy(realSubject);
        }

        @Override
        public UseCaseNotification.PresenterNotification createEmailPresenter() {
            return (message) -> System.out.printf("email premium %s", message);
        }

        @Override
        public UseCaseNotification.PresenterNotification createWhatsAppPresenter() {
            return (message) -> System.out.printf("whatApp premium %s", message);
        }

        @Override
        public UseCaseNotification.PresenterNotification createSmsPresenter() {
            return (message) -> System.out.printf("sms premium %s", message);
        }

        @Override
        public UseCaseNotification.PresenterNotification[] createAllPresenters() {
            return new PresenterNotification[] {
                    createEmailPresenter(),
                    createWhatsAppPresenter(),
                    createSmsPresenter()
            };
        }
}
