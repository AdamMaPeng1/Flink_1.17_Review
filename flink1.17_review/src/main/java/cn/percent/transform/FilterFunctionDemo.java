package cn.percent.transform;

import cn.percent.bean.WaterSensor;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichFilterFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @Describe: flink-transform
 * @Author: Ma Peng
 * @Date: 2024/7/24 星期三 11:06
 */
public class FilterFunctionDemo {
    /**
     *  过滤 VC 大于 12 的数据
     * @param args
     */
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
                // 方式1： 匿名内部类
                /*.filter(new FilterFunction<WaterSensor>() {
                    @Override
                    public boolean filter(WaterSensor value) throws Exception {
                        return value.getVc() > 12;
                    }
                })*/
                //方式2： λ表达式
//                .filter(waterSensor -> waterSensor.getVc() > 12)
                //方式3： FilterFunction的实现类
                .filter(new myFilterFunction())
                //4.sink
                .print();
        //5.execute

        env.execute("mapFunctionDemo： ");
    }

    public static class myFilterFunction extends RichFilterFunction<WaterSensor> {
        @Override
        public boolean filter(WaterSensor value) throws Exception {
            return value.getVc() > 12;
        }
    }
}
