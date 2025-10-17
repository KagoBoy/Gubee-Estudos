package factories;

import interfaces.UseCaseNotification;
import interfaces.NotificationFactory;
import interfaces.UseCaseNotification.PresenterNotification;
import proxy.UseCaseNotificationProxy;
import services.PoolingUseCaseNotification;;

public class StandardNotifcationFactory implements NotificationFactory{
    @Override
        public UseCaseNotification createUseCase() {
            UseCaseNotification realSubject = new PoolingUseCaseNotification();
            return new UseCaseNotificationProxy(realSubject);
        }

        @Override
        public UseCaseNotification.PresenterNotification createEmailPresenter() {
            return (message) -> System.out.printf("email %s", message);
        }

        @Override
        public UseCaseNotification.PresenterNotification createWhatsAppPresenter() {
            return (message) -> System.out.printf("whatApp %s", message);
        }

        @Override
        public UseCaseNotification.PresenterNotification createSmsPresenter() {
            return (message) -> System.out.printf("sms %s", message);
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
