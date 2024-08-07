package cn.percent.source;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @Describe: 从文件中读取数据
 * @Author: Ma Peng
 * @Date: 2024/7/23 星期二 15:23
 */
public class FileSourceDemo {
    public static void main(String[] args) throws Exception {
        //1.执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //2.source : flink1.12 以后的新接口，fromSource
        //2.1 创建FileSource
        FileSource<String> fileSource = FileSource.forRecordStreamFormat(new TextLineInputFormat("UTF-8"), new Path("flink_knowlege_20240701/src/main/resources/file/wc.txt"))
                .build();

        //2.2 env.fromSource
        DataStreamSource<String> source = env.fromSource(fileSource, WatermarkStrategy.noWatermarks(), "fileSource");

        //3.transform
        source.map(str -> str)

        //4.sink
                .print();
        //5.execute()
        env.execute("fileSourceDemo");
    }
}
