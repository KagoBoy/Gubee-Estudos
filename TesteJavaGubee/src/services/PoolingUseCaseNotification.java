package services;

import java.util.Random;

import interfaces.UseCaseNotification;


public class PoolingUseCaseNotification implements UseCaseNotification{
    @Override
        public void notifyEveryHour(String customerId, PresenterNotification presenter) {
            System.out.println("processando regra de negocio");
            presenter.notification(
                    String.format("mensagem a ser enviada para %s: %s \n", customerId, new Random().nextInt()));
        }
}
