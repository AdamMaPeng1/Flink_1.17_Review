package cn.percent.wc;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.operators.UnsortedGrouping;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * @Describe: word count - DataSet 批处理方式实现
 * @Author: Ma Peng
 * @Date: 2024/7/2 星期二 22:36
 */
public class WordCountDataSetDemo {
    public static void main(String[] args) throws Exception{
/*
        //TODO 1.创建执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //TODO 2.创建文件源
        DataSource<String> stringDataSource = env.readTextFile("F:\\个人学习\\大数据复习\\2.尚硅谷大数据学科--核心框架\\Flink\\Flink_1.17_Review\\input\\wc.txt");

        //TODO 3.words -> (word,1) flatMap 处理
        AggregateOperator<Tuple2<String, Integer>> sumResult = stringDataSource.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                        //3.1 value -> (word,1)
                        String[] words = value.split(" ");
                        //3.2 创建Tuple2 对象
                        for (String word : words) {
                            out.collect(Tuple2.of(word, 1));
                        }
                    }
                })

                //TODO 4.group by->word
                .groupBy(0)

                //TODO 5.sum
                .sum(1);


        //TODO 6.print
        sumResult.print();
*/

        dataSetWordCount();

    }

    public static void dataSetWordCount() throws Exception {
        // 1. 创建执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // 2. 从文件读取数据 按行读取(存储的元素就是每行的文本)
        DataSource<String> lineDS =
                env.readTextFile("F:\\个人学习\\大数据复习\\2.尚硅谷大数据学科--核心框架\\Flink\\Flink_1.17_Review\\flink_knowlege_20240701\\src\\main\\resources\\file\\wc.txt");

        // 3. 转换数据格式
        FlatMapOperator<String, Tuple2<String, Long>> wordAndOne =
                lineDS.flatMap(new FlatMapFunction<String, Tuple2<String, Long>>() {

                    @Override
                    public void flatMap(String line, Collector<Tuple2<String,
                            Long>> out) throws Exception {

                        String[] words = line.split(" ");

                        for (String word : words) {
                            out.collect(Tuple2.of(word,1L));
                        }
                    }
                });

        // 4. 按照 word 进行分组
        UnsortedGrouping<Tuple2<String, Long>> wordAndOneUG =
                wordAndOne.groupBy(0);

        // 5. 分组内聚合统计
        AggregateOperator<Tuple2<String, Long>> sum =
                wordAndOneUG.sum(1);

        // 6. 打印结果
        sum.print();

    }
}
