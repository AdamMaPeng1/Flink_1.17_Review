package cn.percent.source;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;

/**
 * @Describe: 从集合中的读取数据
 * @Author: Ma Peng
 * @Date: 2024/7/23 星期二 15:15
 */
public class CollectionSourceDemo {
    public static void main(String[] args) throws Exception {
        //1.执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment(1);

        //2.Source
        env
//                方式一：fromCollection
//                .fromCollection(Arrays.asList(1, 2, 3, 4, 5))
                //方式二：fromElement
                .fromElements(1,2,3,4,5)

                //3.transform
                .map(num -> num * 2)

                //4.sink
                .print("double result : ");

        //5.env.execute
        env.execute("CollectionSourceDemo");
    }
}
