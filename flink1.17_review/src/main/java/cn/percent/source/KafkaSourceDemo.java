package cn.percent.source;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.time.Duration;


/**
 * @Describe: 从 Kafka 中读取数据
 * @Author: Ma Peng
 * @Date: 2024/7/23 星期二 16:37
 */
public class KafkaSourceDemo {
    public static void main(String[] args) throws Exception {
        //1.执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //2.createSource
        KafkaSource kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers("192.168.162.119:6667")
                .setGroupId("kafkaConsumer-01")
                .setTopics("119test")
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .setStartingOffsets(OffsetsInitializer.latest())
                .build();


        //3.fromSource
        env.fromSource(kafkaSource, WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(3)), "kafkaSource")

        //4.transform
        //5.sink
                .print();

        //6.execute
        env.execute("KafkaSourceDemo");
    }
}
