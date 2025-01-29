package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.concurrent.ExecutionException;


public class Main {


    public static void main(String[] args) throws ExecutionException, InterruptedException, JsonProcessingException {

        KafkaConsument kafkaConsument = new KafkaConsument(2);
        kafkaConsument.initConsumers();
        kafkaConsument.consumeTopicByAllConsumers();


    }
}