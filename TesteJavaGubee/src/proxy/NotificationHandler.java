package proxy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import annotation.Transaction;

public class NotificationHandler implements InvocationHandler {


    //Dynamic Proxy
        private final Object target;

        public NotificationHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.isAnnotationPresent(Transaction.class)) {
                System.out.println("[Dynamic Proxy] Iniciando execução do método " + method.getName());

                try {
                    Object result = method.invoke(target, args);

                    System.out.println("[Dynamic Proxy] Sucesso!");
                    return result;
                } catch (Exception e) {
                    System.out.println("[Dynamica Proxy] Erro durante a execução: " + e.getMessage());
                    throw e;
                } finally{
                    System.out.println("[Dynamic Proxy] Finalizando execução do método " + method.getName());
                }
            } else {
                return method.invoke(target, args);
            }
        }
    }