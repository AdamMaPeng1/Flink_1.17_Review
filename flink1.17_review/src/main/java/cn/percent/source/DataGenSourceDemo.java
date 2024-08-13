package cn.percent.source;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.connector.source.util.ratelimit.RateLimiterStrategy;
import org.apache.flink.connector.datagen.source.DataGeneratorSource;
import org.apache.flink.connector.datagen.source.GeneratorFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.datagen.DataGenerator;
import org.apache.flink.streaming.api.functions.source.datagen.SequenceGenerator;

import java.util.function.Consumer;

/**
 * @Describe: DataGen 数据源
 * @Author: Ma Peng
 * @Date: 2024/7/23 星期二 14:58
 */
public class DataGenSourceDemo {
    public static void main(String[] args) throws Exception {
        //1.执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

//        env.setParallelism(1);
        //2.createSource
        DataGeneratorSource<String> generatorSource = new DataGeneratorSource<>(
                new GeneratorFunction<Long, String>() {
                    @Override
                    public String map(Long value) throws Exception {
                        return value + " ";
                    }
                }
                , 10000, RateLimiterStrategy.perSecond(10), Types.STRING);

        //3.fromSource
        env.fromSource(generatorSource, WatermarkStrategy.noWatermarks(), "dataGenerator")
        //4.transform
                .map(str -> "dg-" + str)
        //5.sink
                .print();

        //6.execute
        env.execute("dataGenSourceDemo");
    }
}
