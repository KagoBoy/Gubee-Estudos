package com.kafka;

import org.apache.kafka.clients.admin.AdminClient;
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
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class ParallelConsumerTest {

    public static void main(String[] args) {

        final UUID consumerId = UUID.randomUUID();

        final Properties props = new Properties() {
            {
                put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");
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
                    .maxConcurrency(8)
                    .consumer(consumer)
                    .build();

            try (ParallelStreamProcessor<String, String> eosStreamProcessor = createEosStreamProcessor(options)) {

                eosStreamProcessor.subscribe(Arrays.asList(topic));

                System.out.println("Parallel Consumer iniciado. Processando mensagens...");

                eosStreamProcessor.poll(context -> {
                    var record = context.getSingleConsumerRecord();

                    try (AdminClient admin = AdminClient.create(props)) {
                        var desc = admin.describeTopics(Collections.singletonList(topic))
                                .allTopicNames()
                                .get()
                                .get(topic);
                        var partitionInfo = desc.partitions().get(record.partition());
                        var leader = partitionInfo.leader() != null
                                ? partitionInfo.leader().idString()
                                : "N/A";
                        System.out.printf(
                                "[Thread: %-10s, Consumer: %s, Líder: %s] Evento consumido do topico %-10s chave = %-10s valor = %-10s particao = %-2d offset = %-5d%n",
                                Thread.currentThread().getName(), consumerId, leader, record.topic(),
                                record.key(), record.value(),
                                record.partition(), record.offset());
                        try {
                            Thread.sleep(3000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                });

                Thread.sleep(60000); 

                System.out.println("Finalizando consumer...");
            }

        } catch (Exception e) {
            System.err.println("Erro no consumer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}