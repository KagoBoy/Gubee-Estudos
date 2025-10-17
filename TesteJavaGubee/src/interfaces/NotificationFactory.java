package interfaces;

import interfaces.UseCaseNotification.PresenterNotification;

public interface NotificationFactory {
    UseCaseNotification createUseCase();

    PresenterNotification createEmailPresenter();

    PresenterNotification createWhatsAppPresenter();

    PresenterNotification createSmsPresenter();

    PresenterNotification[] createAllPresenters();
}

