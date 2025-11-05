package com.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
// import static org.apache.kafka.clients.CommonClientConfigs.SECURITY_PROTOCOL_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
// import static org.apache.kafka.common.config.SaslConfigs.SASL_JAAS_CONFIG;
// import static org.apache.kafka.common.config.SaslConfigs.SASL_MECHANISM;

public class ConsumerTest {
    
    public static void main(String[] args) {
        final Properties props = new Properties() {{
            //Rodando com confluent cloud
            // put(BOOTSTRAP_SERVERS_CONFIG, "pkc-619z3.us-east1.gcp.confluent.cloud:9092");
            // put(SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required username = '6PT7TIABZ3QJBODQ' password='cfltAVG96Xbs6VyTXhEA4JjcxR8TIbWz6mjF6oZbdvqvFPyzPN0bccKUTE99CsoQ';");


            //Pra rodar localmente com imagem do kafka no docker
            put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

            put(KEY_DESERIALIZER_CLASS_CONFIG,   StringDeserializer.class.getName());
            put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            put(GROUP_ID_CONFIG,                 "kafka-java-getting-started");
            put(AUTO_OFFSET_RESET_CONFIG,        "earliest");
            // put(SECURITY_PROTOCOL_CONFIG,        "SASL_SSL");
            // put(SASL_MECHANISM,                  "PLAIN");
        }};

        final String topic = "topic_0"; //nome do topico criado no confluent cloud ou localmente

        try (final Consumer<String, String> consumer = new KafkaConsumer<>(props)){
            consumer.subscribe(Arrays.asList(topic));

            while (true){
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records){
                    String key = record.key();
                    String value = record.value();
                    System.out.println(String.format("[Thread %-10s] Evento consumido do topico %s: chave = %-10s valor = %-10s particao = %-10s offset = %s", 
                    Thread.currentThread().getName(), topic, key, value, record.partition(), record.offset()));
                    try {
                        Thread.sleep(1000); 
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
