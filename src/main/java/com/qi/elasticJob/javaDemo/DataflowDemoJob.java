package com.qi.elasticJob.javaDemo;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.google.common.collect.Lists;
import com.qi.elasticJob.entity.Foo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataflowDemoJob implements DataflowJob {
	public DataflowDemoJob() {
		init();
	}
	private Map<Long, Foo> data = new ConcurrentHashMap<>(300, 1);
	private void init() {
		addData(0L, 100L, "Beijing");
		addData(100L, 200L, "Shanghai");
		addData(200L, 300L, "Guangzhou");
	}

	private void addData(final long idFrom, final long idTo, final String location) {
		for (long i = idFrom; i < idTo; i++) {
			data.put(i, new Foo(i, location, Foo.Status.TODO));
		}
	}
	@Override
	public List fetchData(ShardingContext shardingContext) {
		int count = 0;
		switch (shardingContext.getShardingItem()) {
			case 0:
				List<Foo> result0 = new ArrayList<>(10);
				for (Map.Entry<Long, Foo> each : data.entrySet()) {
					Foo foo = each.getValue();
					if (foo.getLocation().equals("Beijing") && foo.getStatus() == Foo.Status.TODO) {
						result0.add(foo);
						count++;
						if (count == 10) {
							break;
						}
					}
				}
				return result0;
			case 1:
				List<Foo> result1 = new ArrayList<>(20);
				for (Map.Entry<Long, Foo> each : data.entrySet()) {
					Foo foo = each.getValue();
					if (foo.getLocation().equals("Shanghai") && foo.getStatus() == Foo.Status.TODO) {
						result1.add(foo);
						count++;
						if (count == 20) {
							break;
						}
					}
				}
				return result1;
			case 2:
				List<Foo> result2 = new ArrayList<>(30);
				for (Map.Entry<Long, Foo> each : data.entrySet()) {
					Foo foo = each.getValue();
					if (foo.getLocation().equals("Guangzhou") && foo.getStatus() == Foo.Status.TODO) {
						result2.add(foo);
						count++;
						if (count == 30) {
							break;
						}
					}
				}
				return result2;
			// case n: ...
			default:
				return new ArrayList();
		}
	}

	@Override
	public void processData(ShardingContext shardingContext, List list) {
		/*System.out.println(String.format("jobName: %s | shardingItem: %s | shardingTotalCount: %d | taskId : %s | jobParameter : %s | shardingParameter : %s | %s",
				shardingContext.getJobName(),
				shardingContext.getShardingItem(),
				shardingContext.getShardingTotalCount(),
				shardingContext.getTaskId(),
				shardingContext.getJobParameter(),
				shardingContext.getShardingParameter()));*/
		System.out.println("jobName:"+shardingContext.getJobName());
		System.out.println("shardingItem:"+shardingContext.getShardingItem());
		System.out.println("shardingTotalCount:"+shardingContext.getShardingTotalCount());
		System.out.println("taskId:"+shardingContext.getTaskId());
		System.out.println("jobParameter:"+shardingContext.getJobParameter());
		System.out.println("shardingParameter:"+shardingContext.getShardingParameter());
		list.forEach(System.out::println);
	}
}
