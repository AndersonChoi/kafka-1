import org.apache.kafka.clients.consumer.*;

import java.util.Arrays;
import java.util.Properties;

public class ConsumerAsyncCallback {
    // single object
    static PeterAsyncConsumerCallback peterAsyncConsumerCallback = new PeterAsyncConsumerCallback();

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "peter-kafka001.foo.bar:9092,peter-kafka002.foo.bar:9092,peter-kafka003.foo.bar:9092");
        props.put("group.id", "peter-consumer");
        props.put("enable.auto.commit", "false");
        props.put("auto.offset.reset", "latest");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("peter-topic"));
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(1000);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("Topic: %s, Partition: %s, Offset: %d, Key: %s, Value: %s\n",
                            record.topic(), record.partition(), record.offset(), record.key(), record.value());
                }
                consumer.commitAsync(peterAsyncConsumerCallback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
}