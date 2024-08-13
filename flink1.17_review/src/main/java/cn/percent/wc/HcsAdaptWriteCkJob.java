package cn.percent.wc;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Random;

/**
 * @Describe:
 * @Author: Ma Peng
 * @Date: 2024/7/18 星期四 15:00
 */
public class HcsAdaptWriteCkJob {
    public static void main(String[] args) throws Exception {
        // 设置 Flink 执行环境
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 创建数据源，这里我们使用一个简单的随机数生成器
        DataStream<Tuple3<Integer, String, Double>> dataStream = env
                .fromSequence(0, 1000) // 生成从 0 到 1000 的数字序列
                .map(new MapFunction<Long, Tuple3<Integer, String, Double>>() {
                    private final Random random = new Random();
                    @Override
                    public Tuple3<Integer, String, Double> map(Long value) {
                        // 随机生成一些数据
                        String randomString = "name" + value;
                        double randomDouble = random.nextDouble() * 100;
                        return new Tuple3<>(value.intValue(), randomString, randomDouble);
                    }
                });

        // 定义 ClickHouse 的 JDBC URL
        String clickhouseUrl = "jdbc:clickhouse://172.24.205.6:21421/ta";

        // 定义写入 ClickHouse 的 SQL 语句
        String insertQuery = "INSERT INTO student (id, name, age) VALUES (?, ?, ?)";

        // 将数据流写入 ClickHouse
        dataStream.addSink(JdbcSink.sink(
                insertQuery,
                (ps, tuple) -> {
                    ps.setInt(1, tuple.f0);
                    ps.setString(2, tuple.f1);
                    ps.setDouble(3, tuple.f2);
                },
                new JdbcExecutionOptions.Builder().withBatchSize(1000).build(),
                new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                        .withUrl(clickhouseUrl)
                        .withDriverName("com.clickhouse.jdbc.ClickHouseDriver")
                        .withUsername("default")
                        .withPassword("Admin1234@")
                        .build()
        ));

        // 执行 Flink 作业
        env.execute("Flink Clickhouse Write Demo");

    }
}
