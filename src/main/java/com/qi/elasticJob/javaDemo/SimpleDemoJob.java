package com.qi.elasticJob.javaDemo;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

public class SimpleDemoJob implements SimpleJob {
	@Override
	public void execute(ShardingContext shardingContext) {
		System.out.println(shardingContext.getJobName());
		System.out.println(shardingContext.getShardingItem());
	}
}
