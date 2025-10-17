package interfaces;
import annotation.Transaction;

public interface UseCaseNotification {
    @Transaction
    void notifyEveryHour(String customerId, PresenterNotification presenter);

    @FunctionalInterface
    public interface PresenterNotification {
        void notification(String message);
    }
}