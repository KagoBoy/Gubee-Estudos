package com.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import io.confluent.parallelconsumer.ParallelConsumerOptions;
import io.confluent.parallelconsumer.ParallelStreamProcessor;
import static io.confluent.parallelconsumer.ParallelConsumerOptions.ProcessingOrder.*;
import static io.confluent.parallelconsumer.ParallelStreamProcessor.createEosStreamProcessor;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;

public class ParallelConsumerTest {

    public static void main(String[] args) {

        final UUID consumerId = UUID.randomUUID();

        final Properties props = new Properties() {
            {
                put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
                put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
                put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
                put(GROUP_ID_CONFIG, "kafka-java-getting-started");
                put(AUTO_OFFSET_RESET_CONFIG, "earliest");
                put(ENABLE_AUTO_COMMIT_CONFIG, false);
            }
        };

        final String topic = "topic_8";

        try (final Consumer<String, String> consumer = new KafkaConsumer<>(props)) {

            final ParallelConsumerOptions<String, String> options = ParallelConsumerOptions.<String, String>builder()
                    .ordering(KEY)
                    .maxConcurrency(4)
                    .consumer(consumer)
                    .build();

            try (ParallelStreamProcessor<String, String> eosStreamProcessor = createEosStreamProcessor(options)) {

                eosStreamProcessor.subscribe(Arrays.asList(topic));

                System.out.println("Parallel Consumer iniciado. Processando mensagens...");

                eosStreamProcessor.poll(context -> {
                    var record = context.getSingleConsumerRecord();
                    System.out.printf(
                            "[Thread: %-10s, Consumer: %s] Evento consumido do topico %-10s chave = %-10s valor = %-10s particao = %-2d offset = %-5d%n",
                            Thread.currentThread().getName(), consumerId, record.topic(), record.key(), record.value(),
                            record.partition(), record.offset());
                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                });

                Thread.sleep(100000);

                System.out.println("Finalizando consumer...");
            }

        } catch (Exception e) {
            System.err.println("Erro no consumer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}