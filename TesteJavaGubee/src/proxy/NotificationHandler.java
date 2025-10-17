package proxy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import annotation.Transaction;

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