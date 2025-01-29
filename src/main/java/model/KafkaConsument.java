package model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.DefaultBaseTypeLimitingValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import repositories.RentRepo;


import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KafkaConsument {
    private final List<KafkaConsumer<UUID, String>> kafkaConsumers = new ArrayList<>();
    private final String RENT_TOPIC = "rents";
    private final int numConsumers;
    private final RentRepo rentRepo;
    private final ObjectMapper objectMapper;

    public KafkaConsument(int numConsumers) {
        this.numConsumers = numConsumers;
        this.rentRepo = new RentRepo();
        this.objectMapper = new ObjectMapper();

        // Obsługa typów hierarchicznych
        this.objectMapper.activateDefaultTyping(
                new DefaultBaseTypeLimitingValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL
        );

        // Obsługa LocalDateTime
        this.objectMapper.registerModule(new JavaTimeModule());

        // Ignorowanie brakujących pól
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public void initConsumers() {
        Properties consumerConfig = new Properties();
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class.getName());
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "group-rents");
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192,kafka2:9292,kafka3:9392");
        consumerConfig.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        if (kafkaConsumers.isEmpty()) {
            for (int i = 0; i < numConsumers; i++) {
                KafkaConsumer<UUID, String> kafkaConsumer = new KafkaConsumer<>(consumerConfig);
                kafkaConsumer.subscribe(Collections.singleton(RENT_TOPIC));
                kafkaConsumers.add(kafkaConsumer);
                System.out.println("Creating consumer " + i);
            }
        }
    }

    public void consume(KafkaConsumer<UUID, String> consumer) {
        try {
            consumer.poll(0);
            Set<TopicPartition> consumerAssignment = consumer.assignment();
            consumer.seekToBeginning(consumerAssignment);
            Duration timeout = Duration.of(100, ChronoUnit.MILLIS);

            while (true) {
                ConsumerRecords<UUID, String> records = consumer.poll(timeout);

                for (ConsumerRecord<UUID, String> record : records) {
                    try {
                        Rent rent = objectMapper.readValue(record.value(), Rent.class);
                        rentRepo.create(rent);
                        System.out.println("Saved rent: " + rent.getId());
                    } catch (Exception e) {
                        System.err.println("Failed to process record: " + record.value() + ", error: " + e.getMessage());
                    }
                    consumer.commitAsync();
                }
            }
        } catch (WakeupException we) {
            System.out.println("Consumer shutdown signal received.");
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    public void consumeTopicByAllConsumers() {
        ExecutorService executorService = Executors.newFixedThreadPool(numConsumers);
        for (KafkaConsumer<UUID, String> consumer : kafkaConsumers) {
            executorService.execute(() -> consume(consumer));
        }
    }
}