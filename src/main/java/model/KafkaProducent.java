package model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Getter
public class KafkaProducent {
    static KafkaProducer<String, String> kafkaProducer;
    private static final String RENT_TOPIC = "rents";

    public KafkaProducent() throws ExecutionException, InterruptedException {
        initProducer();
    }

    public static void initProducer() throws ExecutionException, InterruptedException {
        Properties producerConfig = new Properties();
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerConfig.put(ProducerConfig.CLIENT_ID_CONFIG, "local");
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "kafka1:9192,kafka2:9292,kafka3:9392");
        producerConfig.put(ProducerConfig.ACKS_CONFIG, "all");
        producerConfig.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        kafkaProducer = new KafkaProducer<String, String>(producerConfig);
    }
    public KafkaProducer<String, String> getKafkaProducer(){
        return kafkaProducer;
    }
    public static void sendRentAsync(Rent rent) throws InterruptedException, JsonProcessingException {
        createTopic();
        ObjectMapper om = JsonMapper.builder().addModule(new JavaTimeModule()).build();
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        //Jsonb jsonb = JsonbBuilder.create();
        // String jsonClient = jsonb.toJson (rent +"nazwa - wypozyczalni");
        String jsonClient = om.writeValueAsString(rent);

        System.out.println(jsonClient);
        ProducerRecord<String, String> record = new ProducerRecord<>(RENT_TOPIC, rent.getId(), jsonClient);
        kafkaProducer.send(record);
    }

    private void onCompletion (RecordMetadata data, Exception exception) {
        if (exception == null) {
            System.out.println(data.offset());
        } else {
            System.out.println(exception);
        }
    }

    public static void createTopic() throws InterruptedException { Properties properties = new Properties();
        properties.put
                (AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192, kafka1:9292, kafka1:9392");
        int partitionsNumber = 5;
        short replicationFactor = 3;
        try (Admin admin = Admin.create(properties)) {
            NewTopic newTopic = new NewTopic(RENT_TOPIC, partitionsNumber, replicationFactor);
            CreateTopicsOptions options = new CreateTopicsOptions()
                    .timeoutMs (1000)
                    .validateOnly (false)
                    .retryOnQuotaViolation (true);
            CreateTopicsResult result = admin.createTopics (List.of (newTopic), options);
            KafkaFuture<Void> futureResult = result.values().get(RENT_TOPIC); futureResult.get();
        } catch (ExecutionException e) {
            System.out.println("Topic already exists"+e.getCause());
        }
    }
}