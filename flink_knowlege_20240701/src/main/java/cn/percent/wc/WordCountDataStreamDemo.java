package cn.percent.wc;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @Describe: DataStream 方式 完成 word count
 * @Author: Ma Peng
 * @Date: 2024/7/4 星期四 22:27
 */
public class WordCountDataStreamDemo {
    public static void main(String[] args) throws Exception {
        //1.准备流执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setRuntimeMode(RuntimeExecutionMode.BATCH);

        //2.读取文件，创建数据源
        DataStreamSource<String> stringDataStreamSource = env.readTextFile("F:\\个人学习\\大数据复习\\2.尚硅谷大数据学科--核心框架\\Flink\\Flink_1.17_Review\\flink_knowlege_20240701\\src\\main\\resources\\file\\wc.txt");

        //3.flatMap : line -> (word,1)
        stringDataStreamSource.flatMap(new FlatMapFunction<String, Tuple2<String, Long>>() {
                    @Override
                    public void flatMap(String value, Collector<Tuple2<String, Long>> out) throws Exception {
                        String[] words = value.split(" ");
                        for (String word : words) {
                            out.collect(Tuple2.of(word, 1L));
                        }
                    }
                })

                //4.keyBy
                .keyBy(0)

                //5.sum
                .sum(1)
                .print("result");

        //6.execute
        env.execute();
    }
}
