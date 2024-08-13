package cn.percent.wc;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @Describe: MapFunctionTest
 * @Author: Ma Peng
 * @Date: 2024/7/18 星期四 10:20
 */
public class FlinkDemo {
    public static void main(String[] args) throws Exception {
        //1.执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //2.创建数据源
        env.fromElements(1, 2, 3, 4)
                .map(number -> number * 5)
                .print();

        env.execute();
    }
}
