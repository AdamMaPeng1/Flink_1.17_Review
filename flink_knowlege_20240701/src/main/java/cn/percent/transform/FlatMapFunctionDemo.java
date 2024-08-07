package cn.percent.transform;

import cn.percent.bean.WaterSensor;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @Describe: flink-transform flatMapFunction
 * @Author: Ma Peng
 * @Date: 2024/7/24 星期三 14:26
 */
public class FlatMapFunctionDemo {
    public static void main(String[] args) throws Exception {
        //1.执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //2.source
        //2.1 createSource
        //2.2 env.fromSource
        env.fromElements(
                        new WaterSensor("A0001", 2024072401L, 10),
                        new WaterSensor("A0002", 2024072402L, 11),
                        new WaterSensor("A0003", 2024072403L, 12),
                        new WaterSensor("A0004", 2024072404L, 13),
                        new WaterSensor("A0005", 2024072405L, 14)
                )
                //3.transform
                .flatMap(new FlatMapFunction<WaterSensor, String>() {
                    @Override
                    public void flatMap(WaterSensor value, Collector<String> out) throws Exception {
                        Integer vc = null;
                        if (value != null) {
                            vc = value.getVc();
                        }

                        if (vc > 12) {
                            out.collect(vc.toString());
                        } else {
                            out.collect(value.getTs().toString());
                        }
                    }
                })
                //4.sink
                .print();
        //5.execute

        env.execute("FlatMapFunctionDemo： ");
    }
}
