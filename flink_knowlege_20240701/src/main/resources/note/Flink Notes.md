# 创建 Flink 的每日笔记
2024.07.01 
第 1 章 Flink 概述
1. 使用Flink 的企业有哪些？
    阿里，滴滴，华为，快手，OPPO, 腾讯，UBER, 唯品会，小米
    Capital One：美国第一资本金融公司； 
    BetterCloud： Saas 运营的市场领导企业
    等众多企业在使用 Flink 

2. Flink 的特点
   1) 流批一体
       同一套代码 、 同一套 SQL ，既可以处理流数据，又可以处理批数据
   2) 性能强悍
       低延迟，高吞吐（每秒钟处理百万事件，并且以毫秒级延时提供）
   3) 规模计算
        Flink 本身是分布式的，可以做到动态扩容，水平扩展
        支持超大状态（若干TB）与增量检查点机制
   4) 兼容性生态
         YARN, K8S , Mesos，Standalone
   5) 高容错性
        一致性的检查点；保证故障场景下的精确一次的状态一致性；故障自动重试
   
3. Flink是什么？ 
   1) 核心目标： 
        |--数据流上的有状态计算
   2) 是什么？ 
        |-- Flink 是一个分布式的框架，用来对流式数据和批量数据进行有状态计算
   3) 何为有状态的流计算
        |-- 在计算的过程中，依赖的其他数据称之为状态。 在数据计算时，更新状态，称之为有状态的流计算
   
4. 发展历史
   1) 2014年起源于德国的两所高校的一个项目，项目名称为 stratosphere
   2) 2014年 4 月捐献给apache 基金会 
   3) 2014.12 孵化为Apache 顶级项目
   4) 2019年阿里以9000w 欧元收购了Flink 的商业公司 Data Artisans
   5) 2019年，阿里将自己开源的Blink 合并进Flink，发布了Flink 1.9.0 版本
   
6. 设计理念
   1) 低延迟，高吞吐，结果的准确性，良好的容错性 --》 符合我们对数据处理框架的要求，符合数据处理的目标
   
7. Flink 的优势
   1) 流批一体
      同一套代码，SQL ，针对流、批均可处理
   2) 高性能
      低延迟，高吞吐
   3) 高容错
      自动故障重试
      一致性的检查点
      保障故障时的精确一次性的状态一致性
   4) 规模计算
      自身分布式水平扩展
      支持超大的状态，状态支持增量检查点机制
   5) 生态兼容性
       YARN, K8S, Mesos， Standalone
   
8. Flink 与 SparkStreaming 的区别
   1) 是否为流式引擎：
        Flink : 流式的框架，来一条数据计算一条数据，由时间驱动
            数据模型  ： 数据流，以及Event 事件序列
            运行时架构： 流式计算，一个Event在一个节点计算完成，才会发往下一个节点
        SparkStreaming：微批次的框架
            数据模型  ： Spark 处理的是RDD模型，SparkStreaming处理的是DStream 
            运行时架构： 将DAG 划分成多个 Stage，一个完成才计算下一个
   2) 是否支持状态
        Flink ： 支持状态
        SparkStreaming: 不支持状态
   3) 是否支持流式SQL 
        Flink ： 支持
        SparkStreaming ： 不支持
   4) 窗口情况
        Flink ： 多种
            1）滚动，滑动，会话，时间
            2）按键
        SparkStreaming：
            窗口的整数倍
   5) 时间语义
        Flink： 事件时间，处理时间
        SparkStreaming： 处理时间

第 2 章 Flink 快速上手
1. 创建 Maven 项目，引入依赖：
   1) flink-java
   2) flink-streaming-java
   3) flink-clients
      <groupId>flink.apache.org</groupId>
      <artifactId>flink-java</artifactId>
      <version>1.17.0</version> 
2. 上手Demo 
   1） DataSet 读取文件完成 wordCount
   2） DataStream 读取文件完成 wordCount
   3） DataStream 读取socket 数据流完成 wordCount
3. 注意事项
    1） DataSet 读取文件完成wordCount VS DataStream读取文件完成wordCount
        DataSet ： 
            ① 创建执行环境：ExecutionEnvironment
            ② groupBy 进行分组
            ③ 不需要调用 execute() 方法
            ④ print(); 不能传参
        DataStream:
            ① 创建执行环境：StreamExecutionEnvironment
            ② 需要调用 execute() 来触发程序执行
            ③ keyBy 进行分组
            ④ print(); 有参，无参无所谓
    2） 如果调用了算子，实现其方法采用 λ 表达式，如果内部存在泛型的话，在Java中会存在泛型擦除问题，则需要在调用完算子之后，调用 .returns(Types.XXX) 进行约定
    3） Flink1.12之后，采用DataStream API 完成流批一体。处理流式数据和批量数据，只需要写一套代码/SQL即可
        ① env.setRuntimeMode(RuntimeExecutionMode.BATCH)
        ② 命令行方式提交代码：bin/flink run -Dexecution.runtime-mode=BATCH

第 3 章 Flink 的部署
1. 安装部署注意事项
   1）Flink 项目打包的插件： maven-shade-plugin 
     Java 项目打包的插件：   maven-assembly-plugin

2. 通常自己引入的新的依赖，如果上生产环境，建议针对对应的依赖添加<scope>provided</scope>, 这样在生产环境上执行时，就不会打包该依赖到包中。
   但是这样会导致本地跑该依赖下的类的程序不可用，可通过：run -> Eidit Configuration --> 勾选 include dependenceis with "Provided" score --> default 配置模板
3. 采用Flink1.17版本的 YARN 会话模式，启动 yarn-session.sh 后，Flink rest界面的port等会重写 flink 集群中配置的 properties 

1. 集群角色
   1) Client 客户端： 提交flink 程序给JobManager, 解析参数，封装参数
   2) JobManager： 集群的总管理者。 任务的分配，资源的调度
   3) TaskManager: 真正执行任务的角色

2. Flink 集群的搭建
   1)  官网下载Flink 的安装包 
   2)  上传到 Linux 服务器上， tar -zxvf flink-xxx.tgz 
   3)  cd /opt/flink/conf
   4)  修改配置文件： vim flink-conf.yaml
           jobmanager.rpc.address: hadoop102
           jobmanager.bind-host: 0.0.0.0
           rest.address: hadoop102
           rest.bind-address: 0.0.0.0
           taskmanager.host: hadoop102
           taskmanager.bind-host: 0.0.0.0
   5) :wq,  将 flink-conf.yaml 分发到集群中其他的服务器
   6) 修改其他服务器的 taskmanager.host 为本服务器的 ip
   7) Flink 集群安装完毕
   8) 可以修改其他的配置参数，如： 
            jobmanager.memory.process.size : 1600m （默认）
            taskmanager.memory.process.size: 1728m (默认)
            taskmanager.numberOfTaskSlots: 1（默认）
            parallelism.default: 1 (默认)
   9) 测试安装的集群是否成功： 
      bin/start-cluster.sh （standalone 会话模式）
      浏览器中输入： http://hadoop102:8081 
      提交任务： 1）webUI 中提交 2）flink命令行方式提交：bin/flink run -m hadoop102:8081 -c xxxx.wordCount xxx.jar
   
3. 部署模式
    分类： 
        会话模式（session mode） 
            优点： 适用于单个规模小，执行时间短的大量作业
        单作业模式（per-job mode）
            优点： 一个作业，一个集群； 需要借助第三方资源管理工具
        应用模式（application mode）
            优点： 没有Client，作业直接提交到JobManager上。省去了Client 占用大量的网络带宽（传输job）
    区别：
        1） 集群的生命周期
        2） main 方法执行的位置不同
        3） 资源的分配方式

4. 运行模式
   独立模式（Standalone Mode）
        会话模式： 
           集群启停命令： bin/start-cluster.sh  ； bin/stop-cluster.sh
           任务提交： bin/flink run -d -m hadoop102:8081 -c xxxWordCount xxx.jar
        单作业模式： 单作业模式需要借助第三方资源管理工具，当前无法使用
        应用模式： 
          启动集群，提交任务：
           1) 上传jar 包到flink/lib 目录下（必须） 
           2） 提交任务：bin/standalone-job.sh start --job-classname xxxWordCount  
           3） 启动TaskManager： bin/taskmanager.sh start 
         停掉集群：
            1）bin/taskmanager.sh stop
            2) bin/standalone-job.sh stop
   YARN模式
       前提： Linux 中Hadoop 配置正确
            1）vim /etc/profile.d/my_env.sh 
               HADOOP_HOME=/etc/hadoop-3.1.2/
               export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin
               export HADOOP_CONF_DIR=${HADOOP_HOME}/etc/hadoop
               export HADOOP_CLASSPATH=`hadoop classpath`
       会话模式：
            1）开启集群：bin/yarn-session.sh -nm test
               -nm : 给yarn 集群起个名
               -jm : jobmanager memory
               -tm : taskmanager memory
               -d  : 分离模式,即使关掉对话窗口，Flink程序依然会执行
               -qu : 指定yarn 队列
               注： Flink 1.11 后，去除了 -n:执行TaskManager的数量，-s:指定slot的数量
            2）提交作业（同Standalone的会话模式）：bin/flink run -c xxxWordCount xxx.jar
       单作业模式: 由于有yarn做资源调度的管理，所以可以直接向yarn提交一个单独的作业，从而开启一个Flink集群
            1）提交作业：bin/flink run -t yarn-per-job -c xxxwordCount xxx.jar
               注：如果报错'classloader.check-leaked-classloader',则可以在flink-conf.yaml中添加：  
                  classloader.check-leaked-classloader: false
            2) 查看有哪些作业：bin/flink list -t yarn-per-job -Dyarn.application.id=xxId
            3) 取消某个作业 ：bin/flink cancel -t yarn-per-job -Dyarn.application.id=xxId <JobId>
       应用模式
            1)提交作业：bin/flink run-application -t yarn-application -c xxxWordCount xx.jar
            2)查看作业：bin/flink list -t yarn-application -Dyarn.application=xxId
            3)取消作业：bin/flink cancel -t yarn-application -Dyarn.application=xxId <jobId>
          升级：由于如上方式，依然需要将Flink相关依赖包和数据流jar包通过开启的客户端上传到HDFS，依然会占用带宽，所以可以将flink的依赖jar包和flink 应用程序的jar包提前上传到HDFS 上，这样更加轻便
            1）创建HDFS文件，供Flink依赖包上传：hdfs dfs -mkdir /flink-dist 
              上传lib,plugins文件夹内容到HDFS: hdfs dfs -put /opt/flink/lib/ /flink-dist;hdfs dfs -put /opt/flink/plugins /flink-dist
            2) 创建hdfs文件，供Flink应用程序上传： hdfs dfs -mkdir /flink-jars
               上传自定义数据包：hdfs dfs -put xxxWordCount.jar /flink-jars
            3)提交作业：bin/flink run-application -t yarn-application -Dyarn.provided.lib.dirs="hdfs://hadoop102:8020/flink-dist" hdfs://hadoop102:8020/flink-jars/xxx.jar
   K8S 模式
   MESOS模式 
5. 配置历史服务器
   1）hdfs创建日志存储位置：hdfs dfs -mkdir /log/flink-job
   2) 编辑flink-conf.yaml,配置历史服务器：vim /opt/flink/etc/fink-conf.yaml
   3) xxxx 
   4) 开启历史服务器：bin/historyserver.sh start 
   5) 关闭历史服务器：bin/historyserver.sh stop 








































