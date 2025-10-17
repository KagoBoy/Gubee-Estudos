package factories;

import interfaces.UseCaseNotification;
import interfaces.NotificationFactory;
import interfaces.UseCaseNotification.PresenterNotification;
import services.PoolingUseCaseNotification;;

public class StandardNotifcationFactory implements NotificationFactory{
    @Override
        public UseCaseNotification createUseCase() {
            return new PoolingUseCaseNotification();
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
