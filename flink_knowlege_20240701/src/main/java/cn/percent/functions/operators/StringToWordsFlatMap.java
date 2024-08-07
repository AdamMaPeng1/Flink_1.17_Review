package cn.percent.functions.operators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * @Describe:
 * @Author: Ma Peng
 * @Date: 2024/7/23 星期二 10:58
 */
@Data
@NoArgsConstructor
public class StringToWordsFlatMap extends RichFlatMapFunction<String, Tuple2<String, Long>> {
    // 定义每行字符串单词之间的分隔符
    private String delimiter;

    public StringToWordsFlatMap(String delimiter) {
        this.delimiter = delimiter;
    }


    @Override
    public void flatMap(String value, Collector<Tuple2<String, Long>> out) throws Exception {
        //1. 将读取到的 string 按照 delimiter 进行分割
        String[] words = value.split(delimiter);

        //2. words -> tuple2(word,1)
        for (String word : words) {
            out.collect(Tuple2.of(word, 1L));
        }
    }
}
