package com.qi.elasticJob.javaDemo;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SimpleJobDemo1 implements SimpleJob {
    private final Logger logger = LoggerFactory.getLogger(SimpleJobDemo1.class);
    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println("==========SimpleJobDemo1-start==========");
        System.out.println("====================="+shardingContext.getShardingItem()+"=====================");
        System.out.println("====================="+shardingContext.getJobName()+"=====================");
        System.out.println("==========SimpleJobDemo1-end==========");
        logger.info("====================SimpleJobDemo1-begin=========================");
        System.out.println(String.format("SimpleJobDemo1------Thread ID: %s, 任務總片數: %s, " +
                        "当前分片項: %s,当前參數: %s," +
                        "当前任務名稱: %s,当前任務參數: %s"	,
                Thread.currentThread().getId(),
                shardingContext.getShardingTotalCount(),
                shardingContext.getShardingItem(),
                shardingContext.getShardingParameter(),
                shardingContext.getJobName(),
                shardingContext.getJobParameter()
        ));
        logger.info("====================SimpleJobDemo1-end=========================");
    }
}
