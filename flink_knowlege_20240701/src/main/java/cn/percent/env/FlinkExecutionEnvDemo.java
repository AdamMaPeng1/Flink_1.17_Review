package cn.percent.env;

import cn.percent.functions.operators.StringToWordsFlatMap;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @Describe: flink 的执行环境
 * @Author: Ma Peng
 * @Date: 2024/7/23 星期二 10:08
 */
public class FlinkExecutionEnvDemo {
    public static void main(String[] args) throws Exception {
        //1.执行环境1：批处理的执行环境,该方式不需要调用 env.execute();
//        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        //2.执行环境2：流处理的执行环境
        //2.1 getExecutionEnvironment
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //2.2 createLocalEnvironmentWithWebUI: 创建执行环境带WebUI
        Configuration conf = new Configuration();
        conf.set(RestOptions.BIND_PORT, "8008");
        StreamExecutionEnvironment env = StreamExecutionEnvironment
                .getExecutionEnvironment(conf);
//                .createLocalEnvironmentWithWebUI(conf);
        //2.3 createLocalEnvironment
//                .createLocalEnvironment();
        //2.4 createRemoteEnvironment
//            .createRemoteEnvironment("hadoop102", 9999, "/opt/software/Flink/lib/xxx.jar");

        /*流处理和批处理之间的切换：代码中
        *  通常对流或者批进行设置，是在代码提交的过程中进行设置的：
        *   -Dexecution.runtime-mode=BATCH
        * */
//        env.setRuntimeMode(RuntimeExecutionMode.BATCH);

        //3. source: 读取文件
        env
//                .readTextFile("flink_knowlege_20240701/src/main/resources/file/wc.txt")
                .socketTextStream("172.26.1.65", 7777)
                //4. transform: flatMap 进行处理
                .flatMap(new StringToWordsFlatMap(" "))
                //5. keyBy
                //5.1 批量
//                .groupBy(0)
                //5.2 流式
                .keyBy(0)
//                .sum(1)
                //6. sink: print
                .print();

        //由于Flink是懒执行的（数据到来后才会触发执行），main方法调用只是让程序启动。并不会完成数据来了才处理这一操作，所以需要调用execute()，来触发程序来一条数据处理一条数据。
        env.execute();

        /*
        *  env.excute() 也有返回值，可以获取程序执行过程中的一些信息，但是得等程序完毕后才能得到
        * */
        JobExecutionResult execute = env.execute();
        execute.getJobID();
        execute.getAllAccumulatorResults();
        execute.getJobExecutionResult();

        /*可以调用多次execute(),但是得等到前面的execute执行结束，才会执行后续的execute
        *
        * Flink 1.12 后，提供了 env.executeAsync,异步执行，可以同时执行多个 executeAsync， 而不会被上一个 executeAsync 卡住
        * */
        env.executeAsync("wordCountAsyncJob");

    }
}
