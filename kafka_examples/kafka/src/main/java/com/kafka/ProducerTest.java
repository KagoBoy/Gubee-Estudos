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
// import static org.apache.kafka.clients.CommonClientConfigs.SECURITY_PROTOCOL_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.*;
// import static org.apache.kafka.common.config.SaslConfigs.*;

public class ProducerTest {

    private static void createTopic(String topicName, int partitions, short replicationFactor) {
        Properties adminProps = new Properties();
        adminProps.put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        try (AdminClient adminClient = AdminClient.create(adminProps)) {

            // if (topicExists(adminClient, topicName)) {
            //     adminClient.deleteTopics(Collections.singletonList(topicName));
            //     System.out.println("Topico '" + topicName + "' deletado e será recriado!");
            //     Thread.sleep(3000);
            // }

            if (topicExists(adminClient, topicName)) {
                System.out.println("Topico '" + topicName + "' ja existe!");
                return;
            }

            NewTopic newTopic = new NewTopic(topicName, partitions, replicationFactor);
            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
            System.out.println("Topico '" + topicName + "' criado com sucesso!");
        } catch (InterruptedException | ExecutionException e) {
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
                // Testando com confluent cloud

                // put(BOOTSTRAP_SERVERS_CONFIG, "pkc-619z3.us-east1.gcp.confluent.cloud:9092");
                // put(SASL_JAAS_CONFIG,
                // "org.apache.kafka.common.security.plain.PlainLoginModule required username =
                // '6PT7TIABZ3QJBODQ'
                // password='cfltAVG96Xbs6VyTXhEA4JjcxR8TIbWz6mjF6oZbdvqvFPyzPN0bccKUTE99CsoQ';");
                // put(SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
                // put(SASL_MECHANISM, "PLAIN");

                // Pra rodar localmente com imagem do kafka no docker
                put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");

                put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
                put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
                put(ACKS_CONFIG, "all");
            }
        };


        boolean isLocal = isLocalEnvironment(props);
        if (isLocal) {
            createTopic("topic_8", 8, (short) 1);
        }

        final String topic = "topic_8"; // nome do topico criado no confluent cloud ou localmente

        String[] users = { "yan", "renato", "eduardo", "vitor", "gabriel", "andrei", "marcos", "lucas" };
        String[] items = { "livro", "camisa", "caneta", "bateria", "guitarra" };
        try (final Producer<String, String> producer = new KafkaProducer<>(props)) {
            final Random rnd = new Random();
            final int numMessages = 10;
            for (int i = 0; i < numMessages; i++) {
                String user = users[rnd.nextInt(users.length)];
                String item = items[rnd.nextInt(items.length)];

                producer.send(
                        new ProducerRecord<>(topic, user, item),
                        (event, ex) -> {
                            if (ex != null)
                                ex.printStackTrace();
                            else
                                System.out.printf(
                                        "Evento produzido para o topico %s: chave = %-10s valor = %-10s particao = %-10s offset = %s%n",
                                        event.topic(), user, item, event.partition(), event.offset());

                        });
            }
            System.out.printf("%s eventos foram produzidos para o topico %s%n", numMessages, topic);
        }

    }
}
