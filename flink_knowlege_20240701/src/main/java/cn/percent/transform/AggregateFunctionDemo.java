package cn.percent.transform;

import cn.percent.bean.WaterSensor;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.jupiter.api.Test;

/**
 * @Describe:
 * @Author: Ma Peng
 * @Date: 2024/7/24 星期三 14:33
 */
public class AggregateFunctionDemo {
    public static void main(String[] args) throws Exception {
        //1.执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //2.source
        //2.1 createSource
        //2.2 env.fromSource
        env.fromElements(
                        new WaterSensor("A0001", 2024072401L, 10),
                        new WaterSensor("A0001", 2024072401L, 1),
                        new WaterSensor("A0001", 2024072401L, 2),
                        new WaterSensor("A0001", 2024072401L, 3),
                        new WaterSensor("A0002", 2024072402L, 11),
                        new WaterSensor("A0003", 2024072403L, 12),
                        new WaterSensor("A0004", 2024072404L, 13),
                        new WaterSensor("A0005", 2024072405L, 14)
                )
                //3.transform
                .map(new MapFunctionDemo.myMapFunction())
                .map(new MapFunction<String, String>() {
                    @Override
                    public String map(String value) throws Exception {
                        return value + "againMap";
                    }
                })
                //4.sink
                .print();
        //5.execute

        env.execute("mapFunctionDemo： ");
    }



}
