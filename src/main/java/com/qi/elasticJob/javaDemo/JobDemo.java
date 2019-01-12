package com.qi.elasticJob.javaDemo;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobRootConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

public class JobDemo {

	public static void main(String[] args) {
		new JobScheduler(createRegistryCenter(), createJobConfiguration()).init();
	}

	private static CoordinatorRegistryCenter createRegistryCenter() {
		CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(new ZookeeperConfiguration("127.0.0.1:2181", "elastic-job-demo"));
		regCenter.init();
		return regCenter;
	}

	private static LiteJobConfiguration createJobConfiguration() {
		// 定义作业核心配置（Simple类型作业 意为简单实现，未经任何封装的类型。需实现SimpleJob接口。该接口仅提供单一方法用于覆盖，此方法将定时执行。与Quartz原生接口相似，但提供了弹性扩缩容和分片等功能）
		JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder("demoSimpleJob", "0/15 * * * * ?", 3).build();
		// 定义SIMPLE类型配置
		SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig, SimpleDemoJob.class.getCanonicalName());
		// 定义Lite作业根配置
		JobRootConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).build();
//		return (LiteJobConfiguration) simpleJobRootConfig;

		// 定义作业核心配置(Dataflow类型 Dataflow类型用于处理数据流，需实现DataflowJob接口。该接口提供2个方法可供覆盖，分别用于抓取(fetchData)和处理(processData)数据。)
		JobCoreConfiguration dataflowCoreConfig = JobCoreConfiguration.newBuilder("demoDataflowJob", "0/30 * * * * ?", 3).build();
		// 定义DATAFLOW类型配置
		DataflowJobConfiguration dataflowJobConfig = new DataflowJobConfiguration(dataflowCoreConfig, DataflowDemoJob.class.getCanonicalName(), true);
		// 定义Lite作业根配置
		JobRootConfiguration dataflowJobRootConfig = LiteJobConfiguration.newBuilder(dataflowJobConfig).build();

		return (LiteJobConfiguration) dataflowJobRootConfig;
		/*// 定义作业核心配置配置（script类型基本用不到）
		JobCoreConfiguration scriptCoreConfig = JobCoreConfiguration.newBuilder("demoScriptJob", "0/45 * * * * ?", 10).build();
		// 定义SCRIPT类型配置
		ScriptJobConfiguration scriptJobConfig = new ScriptJobConfiguration(scriptCoreConfig, "test.sh");
		// 定义Lite作业根配置
		JobRootConfiguration scriptJobRootConfig = LiteJobConfiguration.newBuilder(scriptCoreConfig).build();*/
	}
}
