import com.fasterxml.jackson.core.JsonProcessingException;
import model.KafkaConsument;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

import static model.KafkaProducent.initProducer;
import static model.KafkaProducent.sendRentAsync;

public class Main {


    public static void main(String[] args) throws ExecutionException, InterruptedException, JsonProcessingException {

        KafkaConsument kafkaConsument = new KafkaConsument(2);
        kafkaConsument.initConsumers();
        kafkaConsument.consumeTopicByAllConsumers();


    }
}