package cn.percent.wc;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.Map;

/**
 * @Describe: 读取 socket 流式数据，进行 wordCount 统计
 * @Author: Ma Peng
 * @Date: 2024/7/9 星期二 22:26
 */
public class SocketWordCountDemo {
    public static void main(String[] args) throws Exception {
        // 1. 创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //2. 读取socket数据，创建数据源 Source
        DataStreamSource<String> socketTextStream = env.socketTextStream("hadoop102", 7777);

        //3. transform： flatMap
        socketTextStream.flatMap(new FlatMapFunction<String, Tuple2<String, Long>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Long>> out) throws Exception {
                String[] words = value.split(" ");
                for (String word : words) {
                    out.collect(Tuple2.of(word, 1L));
                }
            }
        })
        //4. transform： keyBy
                .keyBy(0)
        //5. transform： sum

        //6. Sink ： print
                .sum(1)
                .print("result");
        //7. execute
        env.execute();
    }
}
