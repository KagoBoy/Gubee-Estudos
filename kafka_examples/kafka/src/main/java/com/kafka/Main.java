package com.kafka;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando sistema Kafka com múltiplos consumers");

        Thread producerThread = new Thread(() -> {
            System.out.println("Iniciando Producer");
            for (int i = 0; i < 4; i++){
                ProducerTest.main(new String[]{});
            }   
        });

        Thread consumer1Thread = new Thread(() -> {
            System.out.println("Iniciando Consumer 1...");
            ParallelConsumerTest.main(new String[]{});
        });

        Thread consumer2Thread = new Thread(() -> {
            System.out.println("Iniciando Consumer 2...");
            ParallelConsumerTest.main(new String[]{});
        });
        
        producerThread.start();
        
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        consumer1Thread.start();
        consumer2Thread.start();

        try {
            producerThread.join();
            consumer1Thread.join();
            consumer2Thread.join();
        } catch (InterruptedException e) {
            System.out.println("Execução interrompida");
            Thread.currentThread().interrupt();
        }

        System.out.println("Todos os componentes finalizados");

    }
}
