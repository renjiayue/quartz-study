package com.rjy.quartzmaven;

import java.util.GregorianCalendar;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.DirectSchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.MutableTrigger;

public class Test {

	public static void main(String[] args) throws Exception {
		/**
		 * 两个工厂都可以创建 Scheduler
		 */
		DirectSchedulerFactory directSchedulerFactory = DirectSchedulerFactory.getInstance();
		directSchedulerFactory.createVolatileScheduler(10);
		Scheduler directSchedule = directSchedulerFactory.getScheduler();
		
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity("simpleTrigger", "simpleTriggerGroup")
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(3).repeatForever())
				.startNow().endAt(new GregorianCalendar(2022, 10, 10).getTime()).build();
		// 不能用
		/**
		 * SimpleScheduleBuilder simpleSchedule =
		 * SimpleScheduleBuilder.simpleSchedule(); CronScheduleBuilder
		 * cronSchedule = CronScheduleBuilder.cronSchedule("* * * * * ?");
		 * SimpleScheduleBuilder CronScheduleBuilder
		 * 这两个构造器并不能直接调用build方法创建构造器,而是依赖 TriggerBuilder 使用 withSchedule构造(调用)
		 * Build the actual Trigger -- NOT intended to be invoked by end users,
		 * but will rather be invoked by a TriggerBuilder which this
		 * ScheduleBuilder is given to.
		 */

		/**
		 * 如果想使用上面的 ScheduleBuilder.build()方法创建trigger 则在补充triggerName后可以使用
		 */
		MutableTrigger mutableTrigger = CronScheduleBuilder.cronSchedule("* * * * * ?").build();
		mutableTrigger.setKey(TriggerKey.triggerKey("triggerName"));
		// mutableTrigger.setKey(TriggerKey.triggerKey("triggerName",
		// "triggerGroup"));

		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				.withSchedule(CronScheduleBuilder.cronSchedule("* * * * * ?")).build();
		JobDetail jobDetail = JobBuilder.newJob(MyJob.class)
				// .withIdentity("jobName")
				// .withIdentity("jobName", "jobGroup")
				// .withIdentity(JobKey.jobKey("jobName"))
				// .withIdentity(JobKey.jobKey("jobName", "jobGroup"))
				.build();
		// scheduler.scheduleJob(jobDetail, trigger);
		// scheduler.scheduleJob(jobDetail, mutableTrigger);
		scheduler.scheduleJob(jobDetail, cronTrigger);
		scheduler.start();
//		System.out.println(directSchedulerFactory.getAllSchedulers().size());
//		System.out.println(new StdSchedulerFactory().getAllSchedulers().size());
//		directSchedule.scheduleJob(jobDetail, trigger);
//		directSchedule.start();
	}
}
