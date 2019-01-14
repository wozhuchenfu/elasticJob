package com.qi.elasticJob.simpleSpringbootElasticJob;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventBus;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.qi.elasticJob.javaDemo.SimpleJobDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置JobConfiuration，配置job随容器一起启动
 * 作业配置
 * 一个作业( ElasticJob )的调度，需要配置独有的一个作业调度器( JobScheduler )，两者是 1 : 1 的关系。这点大家要注意下，当然下文看代码也会看到。
 * 作业调度器的创建可以配置四个参数：
 * 注册中心( CoordinatorRegistryCenter )：用于协调分布式服务。必填。
 * Lite作业配置( LiteJobConfiguration )：必填。
 * 作业事件总线( JobEventBus )：对作业事件异步监听。选填。
 * 作业监听器( ElasticJobListener )：对作业执行前，执行后进行同步监听。选填。
 * http://www.iocoder.cn/Elastic-Job/election/?github&1604  源码解读地址
 */
@Configuration
public class ElasticJobConfig {
	@Autowired
	private ZookeeperRegistryCenter regCenter; //注册中心( CoordinatorRegistryCenter )：用于协调分布式服务。必填。
	/**
	 * 配置任务监听器
	 * 作业监听器( ElasticJobListener )：对作业执行前，执行后进行同步监听。选填。
	 * @return
	 */
	@Bean
	public ElasticJobListener elasticJobListener() {
		return new MyElasticJobListener();
	}

	/**
	 * 配置任务详细信息
	 * @param jobClass
	 * @param cron
	 * @param shardingTotalCount
	 * @param shardingItemParameters
	 * @return
	 */
	private LiteJobConfiguration getLiteJobConfiguration(final Class<? extends SimpleJob> jobClass,
														 final String cron,
														 final int shardingTotalCount,
														 final String shardingItemParameters,
														 final String jobParameter) {
		return LiteJobConfiguration.newBuilder(new SimpleJobConfiguration(
				//作业名字jobClass.getName()
				JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shardingTotalCount).shardingItemParameters(shardingItemParameters).jobParameter(jobParameter).build()
				, jobClass.getCanonicalName())
		).overwrite(true).build();
	}

	/**
	 * 一个作业( ElasticJob )的调度，需要配置独有的一个作业调度器( JobScheduler )，两者是 1 : 1 的关系
	 * @param simpleJob
	 * @param cron
	 * @param shardingTotalCount
	 * @param shardingItemParameters
	 * @return
	 */
	@Bean(initMethod = "init")
	public JobScheduler simpleJobScheduler(final SimpleJobDemo simpleJob,
										   @Value("${simpleJob.cron}") final String cron,
										   @Value("${simpleJob.shardingTotalCount}") final int shardingTotalCount,
										   @Value("${simpleJob.shardingItemParameters}") final String shardingItemParameters,
										   @Value("${simpleJob.parameter}") String jobParameter) {
		MyElasticJobListener elasticJobListener = new MyElasticJobListener();
		return new SpringJobScheduler(simpleJob, regCenter,
				getLiteJobConfiguration(simpleJob.getClass(), cron, shardingTotalCount, shardingItemParameters,jobParameter),
				elasticJobListener);
	}
}
