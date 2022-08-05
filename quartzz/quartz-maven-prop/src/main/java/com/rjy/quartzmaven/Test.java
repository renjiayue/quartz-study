package com.rjy.quartzmaven;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class Test {

	public static void main(String[] args) throws Exception {
		// DirectSchedulerFactory directSchedulerFactory =
		// DirectSchedulerFactory.getInstance();
		/**
		 * 使用createScheduler方法时需要指定schedulerName schedulerInstanceId 而且不能错
		 */
		// SimpleThreadPool threadPool = new SimpleThreadPool(20,
		// Thread.NORM_PRIORITY);
		// threadPool.initialize();
		// directSchedulerFactory.createScheduler("SimpleQuartzScheduler",
		// "SIMPLE_NON_CLUSTERED", threadPool, new RAMJobStore());

		// directSchedulerFactory.createVolatileScheduler(20);
		// Scheduler directScheduler = directSchedulerFactory.getScheduler();

		/**
		 * StdSchedulerFactory 是基于配置文件生成 Scheduler *
		 * <p>
		 * An implementation of <code>{@link org.quartz.SchedulerFactory}</code>
		 * that does all of its work of creating a <code>QuartzScheduler</code>
		 * instance based on the contents of a <code>Properties</code> file.
		 * </p>
		 * 而且默认文件名为 quartz.properties 或者 通过 系统property system property的
		 * org.quartz.properties 指定文件名 java 虚拟机参数
		 * -Dorg.quartz.properties=quartz.properties(在这里修改名称)
		 * 
		 * getScheduler方法中的 initialize方法调用将加载配置文件 如果没有指定配置文件 则按顺序从
		 * quartz.properties /quartz.properties org/quartz/quartz.properties
		 * 文件中加载配置 最后一个是默认配置 下面贴出具体内容
		 * 
		 * 
		 * # Default Properties file for use by StdSchedulerFactory # to create
		 * a Quartz Scheduler Instance, if a different # properties file is not
		 * explicitly specified. #
		 * 
		 * org.quartz.scheduler.instanceName: DefaultQuartzScheduler
		 * org.quartz.scheduler.rmi.export: false
		 * org.quartz.scheduler.rmi.proxy: false
		 * org.quartz.scheduler.wrapJobExecutionInUserTransaction: false
		 * 
		 * org.quartz.threadPool.class: org.quartz.simpl.SimpleThreadPool
		 * org.quartz.threadPool.threadCount: 10
		 * org.quartz.threadPool.threadPriority: 5
		 * org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread:
		 * true
		 * 
		 * org.quartz.jobStore.misfireThreshold: 60000
		 * 
		 * org.quartz.jobStore.class: org.quartz.simpl.RAMJobStore
		 * 
		 * 
		 * 
		 * 使用
		 * Thread.currentThread().getContextClassLoader().getResourceAsStream("quartz.properties");
		 * 或者 new BufferedInputStream(new FileInputStream("quartz.properties"))
		 * 
		 * 
		 * <p>
		 * By default a properties file named "quartz.properties" is loaded from
		 * the 'current working directory'. If that fails, then the
		 * "quartz.properties" file located (as a resource) in the org/quartz
		 * package is loaded. If you wish to use a file other than these
		 * defaults, you must define the system property 'org.quartz.properties'
		 * to point to the file you want.
		 * </p>
		 */

		InputStream resourceAsStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("quartz.properties");
		// 下面方法适用于文件为全路径名称
		BufferedInputStream bufferedInputStream = new BufferedInputStream(
				new FileInputStream("target/classes/quartz.properties"));
		Properties props = new Properties();
		// props.load(resourceAsStream);
		props.load(bufferedInputStream);
		props.forEach((key, value) -> {
			System.out.println(key + "   " + value);
		});

		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		SimpleTrigger trigger = TriggerBuilder.newTrigger()
				// .withIdentity("name", "group")
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(30).repeatForever())
				.startNow().endAt(new GregorianCalendar(2022, 10, 10).getTime()).build();
		JobDetail jobDetail = JobBuilder.newJob(MyJob.class)
				// .withIdentity("name")
				.build();
		// directScheduler.scheduleJob(jobDetail, trigger);
		// directScheduler.start();
		scheduler.scheduleJob(jobDetail, trigger);
		// 查看打印的scheduler名称 可以看出读取了配置文件
		System.out.println("getSchedulerName " + scheduler.getSchedulerName());
		scheduler.start();
	}
}
