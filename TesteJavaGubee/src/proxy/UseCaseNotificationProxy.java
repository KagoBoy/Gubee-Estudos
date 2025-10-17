package proxy;

import interfaces.UseCaseNotification;

public class UseCaseNotificationProxy implements UseCaseNotification{

    //Proxy Pattern
    private final UseCaseNotification realSubject;


    public UseCaseNotificationProxy(UseCaseNotification realSubject){
        this.realSubject = realSubject;
    }


    @Override
    public void notifyEveryHour(String customerId, PresenterNotification presenter) {
        System.out.println("[Proxy Pattern] Iniciando execução");

        try {
            realSubject.notifyEveryHour(customerId, presenter);
            System.out.println("[Proxy Pattern] Sucesso!");
        } catch (Exception e){
            System.out.println("[Proxy Pattern] Erro: " + e.getMessage());
            throw e;
        } finally{
            System.out.println("[Proxy Pattern] Finalizando execução!");
        }
    }
}
