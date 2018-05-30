package com.hongte.alms.finance.util.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;


import java.util.Properties;

public class KafkaUtils {
    private final static KafkaProducer<Integer, String> producer;

    static {
        Properties props = new Properties();
        props.put("bootstrap.servers", KafkaProperties.KAFKA_SERVER_URL + ":" + KafkaProperties.KAFKA_SERVER_PORT);
        props.put("client.id", "MyGroup");
        props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer(props);
    }

    public static void sendMessage(String topic, String value){
        int messageNo = 1;
            try {
                producer.send(new ProducerRecord(topic, messageNo, value)).get();
                Thread.sleep(500);
                messageNo++;
                System.err.println("product:"+value+"      总计为:"+value);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }


    public static void main(String[] args) {
        KafkaUtils.sendMessage("storm_demo","aaaa");
    }
}
