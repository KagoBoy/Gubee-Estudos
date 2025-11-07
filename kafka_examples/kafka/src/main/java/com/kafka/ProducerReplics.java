package com.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.clients.producer.Producer;

import java.util.Collections;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.*;

public class ProducerReplics {

    private static void createTopic(String topicName, int partitions, short replicationFactor) {
        Properties adminProps = new Properties();
        adminProps.put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");

        try (AdminClient adminClient = AdminClient.create(adminProps)) {

            if (topicExists(adminClient, topicName)) {
                System.out.println("Tópico '" + topicName + "' já existe!");
                return;
            }

            NewTopic newTopic = new NewTopic(topicName, partitions, replicationFactor);

            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
            System.out.println("Tópico '" + topicName + "' criado com sucesso!");
            System.out.println("   Partições: " + partitions + " | Fator de replicação: " + replicationFactor);
            Thread.sleep(2000);

        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Erro ao criar tópico: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean topicExists(AdminClient adminClient, String topicName) {
        try {
            ListTopicsResult topics = adminClient.listTopics();
            Set<String> topicNames = topics.names().get(5, TimeUnit.SECONDS);
            return topicNames.contains(topicName);
        } catch (Exception e) {
            System.out.println("Erro ao verificar tópicos: " + e.getMessage());
            return false;
        }
    }

    private static boolean isLocalEnvironment(Properties props) {
        String bootstrapServer = props.getProperty(BOOTSTRAP_SERVERS_CONFIG, "");
        return bootstrapServer.contains("localhost") ||
                bootstrapServer.contains("127.0.0.1") ||
                bootstrapServer.startsWith("localhost");
    }

    public static void main(String[] args) {
        final Properties props = new Properties() {
            {
                // Conecta aos 3 brokers do docker
                put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");

                put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); //Serializa a key para bytes para enviar ao broker
                put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); //Serializa a msg para bytes para enviar ao broker

                // Configurações para replicação e confiabilidade
                put(ACKS_CONFIG, "all"); // Espera confirmação de todos as replicas
                put(RETRIES_CONFIG, 10); // Número de tentativas em caso de falha
                put(RETRY_BACKOFF_MS_CONFIG, 1000); // Espera 1s entre retentativas
                put(MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1); // Garante ordenação
                put(ENABLE_IDEMPOTENCE_CONFIG, true); // Evita duplicação
                put(REQUEST_TIMEOUT_MS_CONFIG, 30000); // Timeout de 30 segundos

                // Configurações de batch e linger para melhor performance
                put(LINGER_MS_CONFIG, 10); //tempo maximo que deve esperar para enviar um lote de mensagens para o broker 10ms
                put(BATCH_SIZE_CONFIG, 16384); // tamanho maximo em bytes de cada lote de mensagens 16KB  (16.384 bytes)
                put(BUFFER_MEMORY_CONFIG, 33554432); // memoria total disponivel para mensagens ainda não enviadas 32mb (33.554.432 bytes)
                
                // Configurações de reconexão
                put(RECONNECT_BACKOFF_MS_CONFIG, 1000); // tempo de espera inicial antes de tentar reconectar a um broker depois de uma falha
                put(RECONNECT_BACKOFF_MAX_MS_CONFIG, 10000); // tempo maximo de esepera entre as tentativas de conexao
            }
        };

        System.out.println("Iniciando Producer com replicação...");
        System.out.println("Conectando aos brokers: localhost:9092, localhost:9093, localhost:9094");

        boolean isLocal = isLocalEnvironment(props);
        if (isLocal) {
            createTopic("topic_8", 8, (short) 3);
        }

        final String topic = "topic_8";

        String[] users = { "yan", "renato", "eduardo", "vitor", "gabriel", "andrei", "marcos", "lucas" };
        String[] items = { "livro", "camisa", "caneta", "bateria", "guitarra" };

        try (final Producer<String, String> producer = new KafkaProducer<>(props)) {
            final Random rnd = new Random();
            final int numMessages = 10;

            System.out.println("Produzindo " + numMessages + " mensagens com replicação...");

            for (int i = 0; i < numMessages; i++) {
                String user = users[rnd.nextInt(users.length)];
                String item = items[rnd.nextInt(items.length)];

                producer.send(
                        new ProducerRecord<>(topic, user, item),
                        (metadata, exception) -> {
                            if (exception != null) {
                                System.err.printf("Erro ao enviar mensagem: %s%n", exception.getMessage());
                            } else {
                                try (AdminClient admin = AdminClient.create(props)) {
                                    var desc = admin.describeTopics(Collections.singletonList(topic))
                                            .allTopicNames()
                                            .get()
                                            .get(topic);
                                    var partitionInfo = desc.partitions().get(metadata.partition());
                                    var leader = partitionInfo.leader() != null
                                            ? partitionInfo.leader().idString()
                                            : "N/A";

                                    System.out.printf(
                                            "[Broker Líder: %s] Produzido → Tópico: %s | Chave: %-10s | Valor: %-10s | Partição: %d | Offset: %d | Réplicas: %s%n",
                                            leader,
                                            metadata.topic(),
                                            user,
                                            item,
                                            metadata.partition(),
                                            metadata.offset(),
                                            "3" // Fator de replicação
                                    );

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            producer.flush();
            System.out.printf("%d eventos foram produzidos com replicação para o tópico %s%n", numMessages, topic);

        } catch (Exception e) {
            System.err.println("Erro no producer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}