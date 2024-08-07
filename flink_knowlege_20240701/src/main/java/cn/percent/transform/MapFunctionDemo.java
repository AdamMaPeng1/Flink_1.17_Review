package cn.percent.transform;

import cn.percent.bean.WaterSensor;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @Describe: 测试 flink-transform： mapFunction
 *     MapFunction 的效果：
 *         输入1一个A --》 返回一个B，（B也可以是A）
 * @Author: Ma Peng
 * @Date: 2024/7/24 星期三 10:36
 */
public class MapFunctionDemo {
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
                .map(new myMapFunction())
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

    public static class myMapFunction implements MapFunction<WaterSensor, String> {
        @Override
        public String map(WaterSensor value) throws Exception {
            return value.getId();
        }
    }

}
